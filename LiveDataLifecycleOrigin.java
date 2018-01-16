package com.holderzone.intelligencestore.mvp.model.network;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Lifecycle.State;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.STARTED;

/**
 * Lifecycle Manager which acting as LiveData in Lifecycle Component.
 * Not the best version.
 * Created by terry on 18-1-16.
 */

public final class LiveDataLifecycleOrigin<T> implements ObservableTransformer<T, T> {

    private static final int START_VERSION = -1;

    private final LifecycleOwner mLifecycleOwner;

    public static <T> ObservableTransformer<T, T> bindToLifecycle(LifecycleOwner lifecycleOwner) {
        return new LiveDataLifecycleOrigin<>(lifecycleOwner);
    }

    private LiveDataLifecycleOrigin(LifecycleOwner lifecycleOwner) {
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
        private Disposable disposable = null;
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
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
                removeObserver();
                return;
            }
            activeStateChanged(isActiveState(lifecycleOwner.getLifecycle().getCurrentState()));
        }

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
            downstream.onSubscribe(d);

            T value = lastSeen.value;
            if (value != null && !disposable.isDisposed()) {
                downstream.onNext(value);
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
                    if (value != null && !disposable.isDisposed()) {
                        downstream.onNext(value);
                    }
                } else if (error != null || completed) {
                    if (error != null) {
                        if (!disposable.isDisposed()) {
                            downstream.onError(error);
                        }
                    } else {
                        if (!disposable.isDisposed()) {
                            downstream.onComplete();
                        }
                    }
                    removeObserver();
                }
            });
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
