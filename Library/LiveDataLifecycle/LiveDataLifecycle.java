package com.holderzone.intelligencestore.mvp.model.network;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Lifecycle Manager which acting as LiveData in Lifecycle Component.
 * Support Observable and Flowable both, the best version.
 * Created by terry on 18-1-16.
 */

public final class LiveDataLifecycle<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T> {

    private static final int START_VERSION = -1;

    private final LifecycleOwner mLifecycleOwner;

    public static <T> ObservableTransformer<T, T> bindToLifecycle(LifecycleOwner lifecycleOwner) {
        return new LiveDataLifecycle<>(lifecycleOwner);
    }

    public static <T> FlowableTransformer<T, T> bindFlowableToLifecycle(LifecycleOwner lifecycleOwner) {
        return new LiveDataLifecycle<>(lifecycleOwner);
    }

    private LiveDataLifecycle(LifecycleOwner lifecycleOwner) {
        mLifecycleOwner = lifecycleOwner;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        LastSeen<T> lastSeen = new LastSeen<>();
        return new LastSeenObservable<>(mLifecycleOwner, upstream.doOnNext(lastSeen).share(), lastSeen);
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        LastSeen<T> lastSeen = new LastSeen<>();
        return new LastSeenFlowable<>(mLifecycleOwner, upstream.doOnNext(lastSeen).share(), lastSeen);
    }

    private static final class LastSeen<T> implements Consumer<T> {
        volatile T value;
        volatile int version = START_VERSION;

        @Override
        public void accept(T latest) {
            value = latest;
            ++version;
        }
    }

    private static final class LastSeenObservable<T> extends Observable<T> {
        private final LifecycleOwner lifecycleOwner;
        private final Observable<T> upstream;
        private final LastSeen<T> lastSeen;

        LastSeenObservable(LifecycleOwner lifecycleOwner, Observable<T> upstream, LastSeen<T> lastSeen) {
            this.lifecycleOwner = lifecycleOwner;
            this.upstream = upstream;
            this.lastSeen = lastSeen;
        }

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            LastSeenObserver<T> lastSeenObserver = new LastSeenObserver<>(lifecycleOwner, observer, lastSeen);
            TaskExecutor.getInstance().executeOnMainThread(() -> lifecycleOwner.getLifecycle().addObserver(lastSeenObserver));
            upstream.subscribe(lastSeenObserver);
        }
    }

    private static final class LastSeenObserver<T> implements Observer<T>, GenericLifecycleObserver {
        private final LifecycleOwner lifecycleOwner;
        private final Observer<? super T> downstream;
        private final LastSeen<T> lastSeen;
        private Disposable upStreamDisposable = null;
        private PublishDisposable<T> downstreamDisposable = null;
        private boolean active = false;
        private boolean completed = false;
        private Throwable error = null;
        private int lastVersion = START_VERSION;

        LastSeenObserver(LifecycleOwner lifecycleOwner, Observer<? super T> downstream, LastSeen<T> lastSeen) {
            this.lifecycleOwner = lifecycleOwner;
            this.downstream = downstream;
            this.lastSeen = lastSeen;
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            if (lifecycleOwner.getLifecycle().getCurrentState() == DESTROYED) {
                disposeDownStreams();
                return;
            }
            activeStateChanged(isActiveState(lifecycleOwner.getLifecycle().getCurrentState()));
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            upStreamDisposable = disposable;

            downstreamDisposable = new PublishDisposable<T>(downstream, this);
            downstream.onSubscribe(downstreamDisposable);

            considerNotify();
        }

        @Override
        public void onNext(T value) {
            considerNotify();
        }

        @Override
        public void onComplete() {
            completed = true;
            considerNotify();
        }

        @Override
        public void onError(Throwable e) {
            error = e;
            considerNotify();
        }

        private void disposeDownStreams() {
            if (downstreamDisposable != null && !downstreamDisposable.isDisposed()) {
                downstreamDisposable.dispose();
            }
        }

        private void disposeUpStreams() {
            if (upStreamDisposable != null && !upStreamDisposable.isDisposed()) {
                upStreamDisposable.dispose();
            }
        }

        private void removeObserver() {
            lifecycleOwner.getLifecycle().removeObserver(this);
            activeStateChanged(false);
        }

        private void activeStateChanged(boolean newActive) {
            if (newActive == active) {
                return;
            }
            active = newActive;
            if (active) {
                considerNotify();
            }
        }

        private void considerNotify() {
            TaskExecutor.getInstance().executeOnMainThread(() -> {
                if (!active) {
                    return;
                }
                if (!isActiveState(lifecycleOwner.getLifecycle().getCurrentState())) {
                    activeStateChanged(false);
                    return;
                }
                if (lastVersion < lastSeen.version) {
                    lastVersion = lastSeen.version;
                    T value = lastSeen.value;
                    if (value != null) {
                        downstreamDisposable.onNext(value);
                    }
                } else if (error != null || completed) {
                    if (error != null) {
                        downstreamDisposable.onError(error);
                    } else {
                        downstreamDisposable.onComplete();
                    }
                    disposeDownStreams();
                }
            });
        }
    }

    private static final class PublishDisposable<T> extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 3562861878281475070L;
        final Observer<? super T> actual;
        final LastSeenObserver<T> parent;

        PublishDisposable(Observer<? super T> actual, LastSeenObserver<T> parent) {
            this.actual = actual;
            this.parent = parent;
        }

        public void onNext(T t) {
            if (!get()) {
                actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            if (get()) {
                RxJavaPlugins.onError(t);
            } else {
                actual.onError(t);
            }
        }

        public void onComplete() {
            if (!get()) {
                actual.onComplete();
            }
        }

        @Override
        public void dispose() {
            if (compareAndSet(false, true)) {
                parent.disposeUpStreams();
                parent.removeObserver();
            }
        }

        @Override
        public boolean isDisposed() {
            return get();
        }
    }

    private static final class LastSeenFlowable<T> extends Flowable<T> {
        private final LifecycleOwner lifecycleOwner;
        private final Flowable<T> upstream;
        private final LastSeen<T> lastSeen;

        LastSeenFlowable(LifecycleOwner lifecycleOwner, Flowable<T> upstream, LastSeen<T> lastSeen) {
            this.lifecycleOwner = lifecycleOwner;
            this.upstream = upstream;
            this.lastSeen = lastSeen;
        }

        @Override
        protected void subscribeActual(Subscriber<? super T> subscriber) {
            LastSeenSubscriber<T> lastSeenSubscriber = new LastSeenSubscriber<>(lifecycleOwner, subscriber, lastSeen);
            TaskExecutor.getInstance().executeOnMainThread(() -> lifecycleOwner.getLifecycle().addObserver(lastSeenSubscriber));
            upstream.subscribe(lastSeenSubscriber);
        }
    }

    private static final class LastSeenSubscriber<T> implements Subscriber<T>, GenericLifecycleObserver {
        private final LifecycleOwner lifecycleOwner;
        private final Subscriber<? super T> downstream;
        private final LastSeen<T> lastSeen;
        private Subscription upStreamSubscription = null;
        private PublishSubscription<T> downStreamSubscription = null;
        private AtomicBoolean first = new AtomicBoolean(false);
        private boolean active = false;
        private boolean completed = false;
        private Throwable error = null;
        private int lastVersion = START_VERSION;

        LastSeenSubscriber(LifecycleOwner lifecycleOwner, Subscriber<? super T> downstream, LastSeen<T> lastSeen) {
            this.lifecycleOwner = lifecycleOwner;
            this.downstream = downstream;
            this.lastSeen = lastSeen;
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            if (lifecycleOwner.getLifecycle().getCurrentState() == DESTROYED) {
                unsubscribeDownStreams();
                return;
            }
            activeStateChanged(isActiveState(lifecycleOwner.getLifecycle().getCurrentState()));
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            upStreamSubscription = subscription;

            downStreamSubscription = new PublishSubscription<>(downstream, this);
            downstream.onSubscribe(downStreamSubscription);
        }

        @Override
        public void onNext(T value) {
            considerNotify();
        }

        @Override
        public void onComplete() {
            completed = true;
            considerNotify();
        }

        @Override
        public void onError(Throwable t) {
            error = t;
            considerNotify();
        }

        private void request(final long amount) {
            if (amount == 0) {
                return;
            }

            if (!first.getAndSet(true)) {
                TaskExecutor.getInstance().executeOnMainThread(() -> {
                    if (!active) {
                        upStreamSubscription.request(amount);
                        return;
                    }
                    if (!isActiveState(lifecycleOwner.getLifecycle().getCurrentState())) {
                        activeStateChanged(false);
                        upStreamSubscription.request(amount);
                        return;
                    }
                    long varAmout = amount;
                    if (lastVersion < lastSeen.version) {
                        lastVersion = lastSeen.version;
                        T value = lastSeen.value;
                        if (value != null) {
                            downStreamSubscription.onNext(value);

                            if (varAmout != Long.MAX_VALUE && --varAmout == 0) {
                                return;
                            }
                        }
                    }
                    upStreamSubscription.request(varAmout);
                });
            } else {
                upStreamSubscription.request(amount);
            }
        }

        private void cancel() {
            if (upStreamSubscription != null) {
                upStreamSubscription.cancel();
            }
        }

        private void unsubscribeDownStreams() {
            if (downStreamSubscription != null && !downStreamSubscription.isCancelled()) {
                downStreamSubscription.cancel();
            }
        }

        private void removeObserver() {
            lifecycleOwner.getLifecycle().removeObserver(this);
            activeStateChanged(false);
        }

        private void activeStateChanged(boolean newActive) {
            if (newActive == active) {
                return;
            }
            active = newActive;
            if (active) {
                considerNotify();
            }
        }

        private void considerNotify() {
            TaskExecutor.getInstance().executeOnMainThread(() -> {
                if (!active) {
                    return;
                }
                if (!isActiveState(lifecycleOwner.getLifecycle().getCurrentState())) {
                    activeStateChanged(false);
                    return;
                }
                if (lastVersion < lastSeen.version) {
                    lastVersion = lastSeen.version;
                    T value = lastSeen.value;
                    if (value != null) {
                        downStreamSubscription.onNext(value);
                    }
                } else if (error != null || completed) {
                    if (error != null) {
                        downStreamSubscription.onError(error);
                    } else {
                        downStreamSubscription.onComplete();
                    }
                    unsubscribeDownStreams();
                }
            });
        }
    }

    private static final class PublishSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 3658216782818740507L;
        private final Subscriber<? super T> actual;
        private final LastSeenSubscriber<T> parent;

        PublishSubscription(Subscriber<? super T> actual, LastSeenSubscriber<T> parent) {
            this.actual = actual;
            this.parent = parent;
        }

        @Override
        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
                parent.request(n);
            }
        }

        @Override
        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                parent.cancel();
                parent.removeObserver();
            }
        }

        private void onNext(T t) {
            long r = get();
            if (r == Long.MIN_VALUE) {
                return;
            }
            if (r != 0L) {
                actual.onNext(t);
                BackpressureHelper.producedCancel(this, 1);
            } else {
                cancel();
                actual.onError(new MissingBackpressureException("Could not emit value due to lack of requests"));
            }
        }

        private void onError(Throwable t) {
            if (get() != Long.MIN_VALUE) {
                actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        private void onComplete() {
            if (get() != Long.MIN_VALUE) {
                actual.onComplete();
            }
        }

        private boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }

    private static boolean isActiveState(State state) {
        return state.isAtLeast(STARTED);
    }

    private static final class TaskExecutor {

        private static volatile TaskExecutor sInstance;

        public static TaskExecutor getInstance() {
            if (sInstance != null) {
                return sInstance;
            }
            synchronized (TaskExecutor.class) {
                if (sInstance == null) {
                    sInstance = new TaskExecutor();
                }
            }
            return sInstance;
        }

        private final Object mLock = new Object();

        @Nullable
        private volatile Handler mMainHandler;

        void executeOnMainThread(Runnable runnable) {
            if (isMainThread()) {
                runnable.run();
            } else {
                postToMainThread(runnable);
            }
        }

        private boolean isMainThread() {
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        }

        private void postToMainThread(Runnable runnable) {
            if (mMainHandler == null) {
                synchronized (mLock) {
                    if (mMainHandler == null) {
                        mMainHandler = new Handler(Looper.getMainLooper());
                    }
                }
            }
            mMainHandler.post(runnable);
        }
    }
}
