package com.example.rxmvp.models;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;

import com.example.rxmvp.data.api.RxMvpApi;
import com.example.rxmvp.data.api.requests.RegisterRequest;
import com.example.rxmvp.data.api.requests.UserInfoRequest;
import com.example.rxmvp.data.entity.RegisteredInfo;
import com.example.rxmvp.data.entity.UserInfo;
import com.example.rxmvp.data.info.AppIdGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.subjects.BehaviorSubject;

@Singleton
public class UserModel {

    @NonNull
    private final AppIdGenerator appIdGenerator;

    @NonNull
    private final AuthModel authModel;

    @NonNull
    private final RxMvpApi rxMvpApi;

    @NonNull
    private final BehaviorSubject<RegisteredInfo> currentUser = BehaviorSubject.create();

    @NonNull
    private final LruCache<String, UserInfo> usersCache = new LruCache<>(5);

    @Inject
    public UserModel(@NonNull AppIdGenerator appIdGenerator, @NonNull AuthModel authModel, @NonNull RxMvpApi rxMvpApi) {
        this.appIdGenerator = appIdGenerator;
        this.authModel = authModel;
        this.rxMvpApi = rxMvpApi;
    }

    @CheckResult
    @NonNull
    public Single<UserInfo> getUserInfo(@NonNull String name) {
        return Observable.merge(
                Single.just(usersCache.get(name)).toObservable(),
                authModel.getToken()
                        .flatMap(token -> appIdGenerator
                                .appDeviceId()
                                .map(appId -> Pair.create(token, appId))
                        )
                        .flatMap(tokenAndAppId -> rxMvpApi
                                .loadUserInfo(
                                        UserInfoRequest.builder()
                                                .token(tokenAndAppId.first)
                                                .appId(tokenAndAppId.second)
                                                .name(name)
                                                .build()
                                )
                        )
                        .doOnSuccess(user -> usersCache.put(name, user))
                        .toObservable())
                .filter(user -> user != null)
                .first()
                .toSingle();
    }

    @CheckResult
    @NonNull
    public Completable registerUser(
            @NonNull String name,
            @NonNull String password
    ) {
        return appIdGenerator.appDeviceId()
                .flatMap(appId -> rxMvpApi.registerUser(
                        RegisterRequest.builder()
                                .appId(appId)
                                .name(name)
                                .password(password)
                                .build()
                        )
                )
                .flatMap(registerInfo -> authModel.setToken(registerInfo.token()).toSingle(() -> registerInfo))
                .doOnSuccess(currentUser::onNext)
                .toCompletable();
    }

    @CheckResult
    @NonNull
    public Observable<RegisteredInfo> observeCurrentUser() {
        return currentUser;
    }
}
