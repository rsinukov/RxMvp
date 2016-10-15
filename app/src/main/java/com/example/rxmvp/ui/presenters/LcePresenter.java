package com.example.rxmvp.ui.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rxmvp.ui.views.LceView;
import com.google.auto.value.AutoValue;

import rx.Scheduler;
import rx.Single;
import rx.Subscriber;

public abstract class LcePresenter<T, View extends LceView<T>> extends BasePresenter<View> {

    @Nullable
    private CachedState<T> cachedState;

    @NonNull
    protected final Config config;

    protected LcePresenter(@NonNull Config config) {
        this.config = config;
    }

    @Override
    public void bindView(@NonNull View view, @Nullable Bundle savedState) {
        super.bindView(view, savedState);

        if (cachedState != null) {
            if (cachedState.isLoading()) {
                view.showLoading();
            } else if (cachedState.isError()) {
                assert cachedState.error != null;
                view.onError(cachedState.error);
            } else if (cachedState.isSuccess()) {
                assert cachedState.result != null;
                view.onSuccess(cachedState.result);
            }
        }
    }

    protected void load(@NonNull Single<T> single) {
        cachedState = CachedState.loading();
        executeIfViewBound(LceView::showLoading);
        single
                .subscribeOn(config.bgScheduler())
                .observeOn(config.uiScheduler())
                .subscribe(new LceSubscriber());
    }

    private class LceSubscriber extends Subscriber<T> {

        @Override
        public void onCompleted() {
            // no op
        }

        @Override
        public void onError(@NonNull Throwable e) {
            cachedState = CachedState.error(e);
            executeIfViewBound(view -> view.onError(e));
        }

        @Override
        public void onNext(@NonNull T t) {
            cachedState = CachedState.result(t);
            executeIfViewBound(view -> view.onSuccess(t));
        }
    }


    private static class CachedState<T> {

        static <T> CachedState<T> result(@Nullable T result) {
            return new CachedState<>(false, result, null);
        }

        static <T> CachedState<T> error(@NonNull Throwable error) {
            return new CachedState<>(false, null, error);
        }

        static <T> CachedState<T> loading() {
            return new CachedState<>(true, null, null);
        }

        private final boolean loading;

        @Nullable
        final Throwable error;

        @Nullable
        final T result;

        private CachedState(boolean loading, @Nullable T result, @Nullable Throwable error) {
            this.loading = loading;
            this.error = error;
            this.result = result;
        }

        boolean isLoading() {
            return loading;
        }

        boolean isError() {
            return error != null;
        }

        boolean isSuccess() {
            return result != null;
        }
    }

    @AutoValue
    public abstract static class Config {

        @NonNull
        public abstract Scheduler bgScheduler();

        @NonNull
        public abstract Scheduler uiScheduler();

        public static Config create(@NonNull Scheduler ioScheduler, @NonNull Scheduler uiScheduler) {
            return new AutoValue_LcePresenter_Config(ioScheduler, uiScheduler);
        }
    }
}
