package com.example.rxmvp.ui.views;

import android.support.annotation.NonNull;

import rx.Observable;

public interface LoginView extends LceView<String> {

    @NonNull
    Observable<CharSequence> userName();

    @NonNull
    Observable<CharSequence> password();

    void onFieldsValidChanged(boolean isValid);
}
