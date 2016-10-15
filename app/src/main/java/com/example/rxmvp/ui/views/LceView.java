package com.example.rxmvp.ui.views;

import android.support.annotation.NonNull;

public interface LceView<T> {

    void showLoading();

    void onSuccess(@NonNull T result);

    void onError(@NonNull Throwable error);
}
