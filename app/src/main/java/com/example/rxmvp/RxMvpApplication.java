package com.example.rxmvp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.rxmvp.data.api.ApiModule;
import com.example.rxmvp.data.info.InfoModule;
import com.example.rxmvp.data.local.LocalStorageModule;

import timber.log.Timber;

public class RxMvpApplication extends Application {

    @NonNull
    public static RxMvpApplication app(@NonNull Context context) {
        return (RxMvpApplication) context.getApplicationContext();
    }

    @NonNull
    public static ApplicationComponent appComponent(@NonNull Context context) {
        return ((RxMvpApplication) context.getApplicationContext()).applicationComponent;
    }

    @SuppressWarnings("NullableProblems") // onCreate
    @NonNull
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .apiModule(new ApiModule())
                .localStorageModule(new LocalStorageModule(this))
                .infoModule(new InfoModule())
                .build();
    }
}
