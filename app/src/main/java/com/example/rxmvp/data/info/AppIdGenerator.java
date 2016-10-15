package com.example.rxmvp.data.info;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.rxmvp.data.local.LocalStorage;

import java.util.UUID;

import rx.Single;

import static android.text.TextUtils.isEmpty;

public class AppIdGenerator {

    @NonNull
    private final LocalStorage localStorage;

    public AppIdGenerator(@NonNull LocalStorage localStorage) {
        this.localStorage = localStorage;
    }

    @CheckResult
    @NonNull
    public Single<String> appDeviceId() {
        return localStorage
                .getAppId()
                .flatMap(id -> isEmpty(id) ? generateNewId() : Single.just(id));
    }

    @VisibleForTesting
    @CheckResult
    @NonNull
    Single<String> generateNewId() {
        return Single.fromCallable(() -> UUID.randomUUID().toString())
                .flatMap(id -> localStorage.saveAppId(id).toSingle(() -> id));
    }
}
