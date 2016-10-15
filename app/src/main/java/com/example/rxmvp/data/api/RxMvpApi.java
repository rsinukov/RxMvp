package com.example.rxmvp.data.api;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.example.rxmvp.data.api.requests.RegisterRequest;
import com.example.rxmvp.data.api.requests.UserInfoRequest;
import com.example.rxmvp.data.entity.RegisteredInfo;
import com.example.rxmvp.data.entity.UserInfo;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Single;

public interface RxMvpApi {

    @POST("/api/v1/user/info")
    @CheckResult
    @NonNull
    Single<UserInfo> loadUserInfo(@NonNull @Body UserInfoRequest request);

    @POST("/api/v1/user/register")
    @CheckResult
    @NonNull
    Single<RegisteredInfo> registerUser(@NonNull @Body RegisterRequest request);
}
