package com.example.rxmvp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.rxmvp.RxMvpApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalStorageModule {

    private final RxMvpApplication context;

    public LocalStorageModule(@NonNull RxMvpApplication context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @NonNull
    public SharedPreferences provideSharedPrefferences() {
        return context.getSharedPreferences("LOCAL_STORAGE", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    @NonNull
    public LocalStorage provideLocalStorage(@NonNull SharedPreferences sharedPreffences) {
        return new SharedPreffsLocalStorage(sharedPreffences);
    }
}
