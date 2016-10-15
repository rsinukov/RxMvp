package com.example.rxmvp.data.info;

import android.support.annotation.NonNull;

import com.example.rxmvp.data.local.LocalStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InfoModule {

    @Provides
    @Singleton
    @NonNull
    public AppIdGenerator provideAppIdGenerator(@NonNull LocalStorage localStorage) {
        return new AppIdGenerator(localStorage);
    }
}
