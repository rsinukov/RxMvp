package com.example.rxmvp.test;

import android.support.annotation.NonNull;

import com.example.rxmvp.RxMvpApplication;

import org.robolectric.TestLifecycleApplication;
import org.robolectric.shadows.ShadowLog;

import java.lang.reflect.Method;

public class IntegrationRxMvpApp extends RxMvpApplication implements TestLifecycleApplication {

    @Override
    public void beforeTest(@NonNull Method method) {
        ShadowLog.stream = System.err;
    }

    @Override
    public void prepareTest(@NonNull Object test) {
    }

    @Override
    public void afterTest(@NonNull Method method) {
    }
}
