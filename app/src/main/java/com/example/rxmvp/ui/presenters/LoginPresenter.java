package com.example.rxmvp.ui.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.ui.views.LoginView;

import rx.Observable;
import rx.Single;
import rx.Subscription;

public class LoginPresenter extends LcePresenter<String, LoginView> {

    private static final int FIELD_MIN_VALID_LENGTH = 3;

    @NonNull
    private final UserModel userModel;

    public LoginPresenter(@NonNull UserModel userModel, @NonNull Config config) {
        super(config);
        this.userModel = userModel;
    }

    @Override
    public void bindView(@NonNull LoginView view, @Nullable Bundle savedState) {
        super.bindView(view, savedState);

        final Subscription subscription = Observable.combineLatest(
                view.userName(),
                view.password(),
                (name, password) -> name.length() >= FIELD_MIN_VALID_LENGTH && password.length() >= FIELD_MIN_VALID_LENGTH
        ).subscribe(isValid -> executeIfViewBound(v -> v.onFieldsValidChanged(isValid)));
        unsubscribeOnUnbindView(subscription);
    }

    public void registerUser(@NonNull String name, @NonNull String password) {
        final Single<String> registerSingle = userModel.registerUser(name, password)
                .toSingle(() -> name);
        load(registerSingle);
    }
}
