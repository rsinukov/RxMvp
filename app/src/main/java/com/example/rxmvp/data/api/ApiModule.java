package com.example.rxmvp.data.api;

import android.support.annotation.NonNull;

import com.example.rxmvp.data.api.requests.RegisterRequest;
import com.example.rxmvp.data.api.requests.UserInfoRequest;
import com.example.rxmvp.data.entity.RegisteredInfo;
import com.example.rxmvp.data.entity.UserInfo;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.http.Body;
import rx.Observable;
import rx.Single;
import timber.log.Timber;

@Module
public class ApiModule {

    @Provides
    @Singleton
    @NonNull
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.d(message));
        return new OkHttpClient.Builder().addInterceptor(logging).build();
    }

    @Provides
    @Singleton
    @NonNull
    public RxMvpApi provideApiModule(@NonNull OkHttpClient client) {
        // TODO: retrofit
        return new RxMvpApi() {
            @NonNull
            @Override
            public Single<UserInfo> loadUserInfo(@NonNull @Body UserInfoRequest request) {
                return Observable.timer(3, TimeUnit.SECONDS)
                        .toSingle()
                        .map(whatever -> UserInfo.builder()
                                .avatarUrl("https://upload.wikimedia.org/wikipedia/commons/0/0e/Trex_charge.jpg")
                                .name(request.name())
                                .build()
                        );
            }

            @NonNull
            @Override
            public Single<RegisteredInfo> registerUser(@NonNull @Body RegisterRequest request) {
                return Observable.timer(3, TimeUnit.SECONDS)
                        .toSingle()
                        .map(whatever -> RegisteredInfo.builder()
                                .token("blablatoken")
                                .name(request.name())
                                .build()
                        );
            }
        };
    }
}
