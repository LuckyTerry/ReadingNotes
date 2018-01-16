package com.holderzone.intelligencestore.mvp.model.network;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Copy of LiveDataReactiveStreams.
 * Except that subscribe and unsubscribe were Modified.
 * Created by terry on 18-1-15.
 */

public final class LiveDataRxStreams {
    private LiveDataRxStreams() {
    }

    public static <T> ObservableTransformer<T, T> bindToLifecycle(LifecycleOwner lifecycleOwner) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                LiveData<T> liveData = LiveDataRxStreams.fromPublisher(upstream.toFlowable(BackpressureStrategy.BUFFER));
                return Flowable.fromPublisher(LiveDataRxStreams.toPublisher(lifecycleOwner, liveData)).toObservable();
            }
        };
    }

    private static <T> Publisher<T> toPublisher(
            final LifecycleOwner lifecycle, final LiveData<T> liveData) {

        return new LiveDataPublisher<>(lifecycle, liveData);
    }

    private static final class LiveDataPublisher<T> implements Publisher<T> {
        final LifecycleOwner mLifecycle;
        final LiveData<T> mLiveData;

        LiveDataPublisher(final LifecycleOwner lifecycle, final LiveData<T> liveData) {
            this.mLifecycle = lifecycle;
            this.mLiveData = liveData;
        }

        @Override
        public void subscribe(Subscriber<? super T> subscriber) {
            subscriber.onSubscribe(new LiveDataSubscription<T>(subscriber, mLifecycle, mLiveData));
        }

        static final class LiveDataSubscription<T> implements Subscription, Observer<T> {
            final Subscriber<? super T> mSubscriber;
            final LifecycleOwner mLifecycle;
            final LiveData<T> mLiveData;

            volatile boolean mCanceled;
            // used on main thread only
            boolean mObserving;
            long mRequested;
            // used on main thread only
            @Nullable
            T mLatest;

            LiveDataSubscription(final Subscriber<? super T> subscriber,
                                 final LifecycleOwner lifecycle, final LiveData<T> liveData) {
                this.mSubscriber = subscriber;
                this.mLifecycle = lifecycle;
                this.mLiveData = liveData;
            }

            @Override
            public void onChanged(T t) {
                if (mCanceled) {
                    return;
                }
                if (mRequested > 0) {
                    mLatest = null;
                    mSubscriber.onNext(t);
                    if (mRequested != Long.MAX_VALUE) {
                        mRequested--;
                    }
                } else {
                    mLatest = t;
                }
            }

            @Override
            public void request(final long n) {
                if (mCanceled) {
                    return;
                }
                TaskExecutor.getInstance().executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCanceled) {
                            return;
                        }
                        if (n <= 0L) {
                            mCanceled = true;
                            if (mObserving) {
                                mLiveData.removeObserver(LiveDataSubscription.this);
                                mObserving = false;
                            }
                            mLatest = null;
                            mSubscriber.onError(
                                    new IllegalArgumentException("Non-positive request"));
                            return;
                        }

                        // Prevent overflowage.
                        mRequested = mRequested + n >= mRequested
                                ? mRequested + n : Long.MAX_VALUE;
                        if (!mObserving) {
                            mObserving = true;
                            mLiveData.observe(mLifecycle, LiveDataSubscription.this);
                        } else if (mLatest != null) {
                            onChanged(mLatest);
                            mLatest = null;
                        }
                    }
                });
            }

            @Override
            public void cancel() {
                if (mCanceled) {
                    return;
                }
                mCanceled = true;
                TaskExecutor.getInstance().executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mObserving) {
                            mLiveData.removeObserver(LiveDataSubscription.this);
                            mObserving = false;
                        }
                        mLatest = null;
                    }
                });
            }
        }
    }

    private static <T> LiveData<T> fromPublisher(final Publisher<T> publisher) {
        return new PublisherLiveData<>(publisher);
    }

    private static class PublisherLiveData<T> extends LiveData<T> {
        private final Publisher mPublisher;
        final AtomicReference<LiveDataSubscriber> mSubscriber;

        PublisherLiveData(@NonNull final Publisher publisher) {
            mPublisher = publisher;
            mSubscriber = new AtomicReference<>();
        }

        @Override
        protected void onActive() {
            super.onActive();
            if (mSubscriber.get() == null) {
                LiveDataSubscriber s = new LiveDataSubscriber();
                mSubscriber.set(s);
                mPublisher.subscribe(s);
            }
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            if (!hasObservers()) {
                LiveDataSubscriber s = mSubscriber.getAndSet(null);
                if (s != null) {
                    s.cancelSubscription();
                }
            }
        }

        final class LiveDataSubscriber extends AtomicReference<Subscription>
                implements Subscriber<T> {

            @Override
            public void onSubscribe(Subscription s) {
                if (compareAndSet(null, s)) {
                    s.request(Long.MAX_VALUE);
                } else {
                    s.cancel();
                }
            }

            @Override
            public void onNext(T item) {
                postValue(item);
            }

            @Override
            public void onError(final Throwable ex) {
                mSubscriber.compareAndSet(this, null);

                TaskExecutor.getInstance().executeOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        // Errors should be handled upstream, so propagate as a crash.
                        throw new RuntimeException("LiveData does not handle errors. Errors from "
                                + "publishers should be handled upstream and propagated as "
                                + "state", ex);
                    }
                });
            }

            @Override
            public void onComplete() {
                mSubscriber.compareAndSet(this, null);
            }

            public void cancelSubscription() {
                Subscription s = get();
                if (s != null) {
                    s.cancel();
                }
            }
        }
    }

    private static class TaskExecutor {

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
            //noinspection ConstantConditions
            mMainHandler.post(runnable);
        }
    }
}
