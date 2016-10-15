package com.example.rxmvp.ui.presenters;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<View> {

    @Nullable
    private CompositeSubscription subscriptionsToUnsubscribeOnUnbindView;

    @Nullable
    private View view;

    @CallSuper
    public void bindView(@NonNull View view, @Nullable Bundle savedState) {
        final View previousView = this.view;

        if (previousView != null) {
            throw new IllegalStateException("Previous view is not unbounded! previousView = " + previousView);
        }

        subscriptionsToUnsubscribeOnUnbindView = new CompositeSubscription();

        this.view = view;
    }

    @CallSuper
    public void unbindView(@NonNull View view) {
        final View previousView = this.view;

        if (previousView == view) {
            this.view = null;
        } else {
            throw new IllegalStateException("Unexpected view! previousView = " + previousView + ", view to unbind = " + view);
        }

        assert subscriptionsToUnsubscribeOnUnbindView != null;
        subscriptionsToUnsubscribeOnUnbindView.clear();
    }

    protected void executeIfViewBound(@NonNull Action1<View> action) {
        if (view != null) {
            action.call(view);
        }
    }

    protected final void unsubscribeOnUnbindView(@NonNull Subscription subscription, @NonNull Subscription... subscriptions) {
        if (subscriptionsToUnsubscribeOnUnbindView == null) {
            throw new IllegalStateException("View has not been bounded yet!");
        }

        subscriptionsToUnsubscribeOnUnbindView.add(subscription);

        for (Subscription s : subscriptions) {
            subscriptionsToUnsubscribeOnUnbindView.add(s);
        }
    }

    @Nullable
    public View view() {
        return view;
    }
}
