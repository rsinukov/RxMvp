package com.example.rxmvp;

import android.support.annotation.NonNull;

import com.example.rxmvp.data.api.ApiModule;
import com.example.rxmvp.data.info.AppIdGenerator;
import com.example.rxmvp.data.info.InfoModule;
import com.example.rxmvp.data.local.LocalStorageModule;
import com.example.rxmvp.models.AuthModel;
import com.example.rxmvp.ui.fragments.LoginFragment.LoginFragmentComponent;
import com.example.rxmvp.ui.fragments.LoginFragment.LoginFragmentModule;
import com.example.rxmvp.ui.fragments.UserInfoFragment.UserInfoFragmentComponent;
import com.example.rxmvp.ui.fragments.UserInfoFragment.UserInfoFragmentModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LocalStorageModule.class, InfoModule.class, ApiModule.class})
public interface ApplicationComponent {

    @NonNull
    AppIdGenerator appIdGenerator();

    @NonNull
    AuthModel authModel();

    @NonNull
    LoginFragmentComponent plus(@NonNull LoginFragmentModule loginFragmentModule);

    @NonNull
    UserInfoFragmentComponent plus(@NonNull UserInfoFragmentModule userInfoFragmentModule);
}
