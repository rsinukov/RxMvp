package com.example.rxmvp.data.local;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Completable;
import rx.Single;

public interface LocalStorage {

    @CheckResult
    @NonNull
    Single<String> getAppId();

    @CheckResult
    @NonNull
    Completable saveAppId(@NonNull String id);

    @CheckResult
    @NonNull
    Single<String> getToken();

    @CheckResult
    @NonNull
    Completable saveToken(@NonNull String token);
}
