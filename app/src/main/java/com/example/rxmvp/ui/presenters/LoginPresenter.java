package com.example.rxmvp.ui.presenters;

import android.support.annotation.NonNull;

import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.ui.views.LceView;

import rx.Single;

public class LoginPresenter extends LcePresenter<String, LceView<String>> {

    @NonNull
    private final UserModel userModel;


    public LoginPresenter(@NonNull UserModel userModel, @NonNull Config config) {
        super(config);
        this.userModel = userModel;
    }

    public void registerUser(@NonNull String name, @NonNull String password) {
        final Single<String> registerSingle = userModel.registerUser(name, password)
                .toSingle(() -> name);
        load(registerSingle);
    }
}
