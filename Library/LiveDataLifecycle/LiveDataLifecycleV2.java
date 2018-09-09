package com.holderzone.intelligencestore.mvp.model.network;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Lifecycle Manager which acting as LiveData in Lifecycle Component.
 * Support the Observable only, not the best version.
 * Created by terry on 18-1-16.
 */

public final class LiveDataLifecycleV2<T> implements ObservableTransformer<T, T> {

    private static final int START_VERSION = -1;

    private final LifecycleOwner mLifecycleOwner;

    public static <T> ObservableTransformer<T, T> bindToLifecycle(LifecycleOwner lifecycleOwner) {
        return new LiveDataLifecycleV2<>(lifecycleOwner);
    }

    private LiveDataLifecycleV2(LifecycleOwner lifecycleOwner) {
        mLifecycleOwner = lifecycleOwner;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        LastSeen<T> lastSeen = new LastSeen<>();
        return new LastSeenObservable<>(mLifecycleOwner, upstream.doOnNext(lastSeen).share(), lastSeen);
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

            T value = lastSeen.value;
            if (value != null && !downstreamDisposable.isDisposed()) {
                downstreamDisposable.onNext(value);
            }
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
                    if (value != null && !downstreamDisposable.isDisposed()) {
                        downstreamDisposable.onNext(value);
                    }
                } else if (error != null || completed) {
                    if (error != null) {
                        if (!downstreamDisposable.isDisposed()) {
                            downstreamDisposable.onError(error);
                        }
                    } else {
                        if (!downstreamDisposable.isDisposed()) {
                            downstreamDisposable.onComplete();
                        }
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
