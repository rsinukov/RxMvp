package com.example.rxmvp.models;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rxmvp.data.local.LocalStorage;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public class AuthModel {

    @NonNull
    private final LocalStorage localStorage;

    @Inject
    public AuthModel(@NonNull LocalStorage localStorage) {
        this.localStorage = localStorage;
    }

    @CheckResult
    @NonNull
    public Single<String> getToken() {
        return localStorage.getToken();
    }

    @CheckResult
    @NonNull
    public Completable setToken(@Nullable String token) {
        return localStorage.saveToken(token);
    }
}
