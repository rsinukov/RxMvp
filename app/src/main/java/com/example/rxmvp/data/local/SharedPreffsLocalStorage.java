package com.example.rxmvp.data.local;

import android.content.SharedPreferences;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.Completable;
import rx.Single;

public class SharedPreffsLocalStorage implements LocalStorage {

    public static final String APP_ID_KEY = "APP_ID";

    public static final String TOKEN_KEY = "TOKEN";

    @NonNull
    private final SharedPreferences preferences;

    public SharedPreffsLocalStorage(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @CheckResult
    @NonNull
    @Override
    public Single<String> getAppId() {
        return Single.fromCallable(() -> preferences.getString(APP_ID_KEY, null));
    }

    @CheckResult
    @NonNull
    @Override
    public Completable saveAppId(@NonNull String id) {
        return Completable.fromAction(() -> preferences.edit().putString(APP_ID_KEY, id).apply());
    }

    @CheckResult
    @NonNull
    @Override
    public Single<String> getToken() {
        return Single.fromCallable(() -> preferences.getString(TOKEN_KEY, null));
    }

    @CheckResult
    @NonNull
    @Override
    public Completable saveToken(@NonNull String token) {
        return Completable.fromAction(() -> preferences.edit().putString(TOKEN_KEY, token).apply());
    }
}
