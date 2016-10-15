package com.example.rxmvp.ui.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rxmvp.data.entity.UserInfo;
import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.ui.views.LceView;

import rx.Subscription;

public class UserInfoPresenter extends LcePresenter<UserInfo, LceView<UserInfo>> {

    @NonNull
    private final UserModel userModel;

    public UserInfoPresenter(@NonNull UserModel userModel, @NonNull Config config) {
        super(config);
        this.userModel = userModel;
    }

    @Override
    public void bindView(@NonNull LceView<UserInfo> userInfoView, @Nullable Bundle savedState) {
        super.bindView(userInfoView, savedState);

        final Subscription subscription = userModel.observeCurrentUser()
                .distinctUntilChanged()
                .filter(user -> user != null)
                .observeOn(config.uiScheduler())
                .doOnNext(user -> load(userModel.getUserInfo(user.name())))
                .subscribe();
        unsubscribeOnUnbindView(subscription);
    }
}
