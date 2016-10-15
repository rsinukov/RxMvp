package com.example.rxmvp.models;

import android.support.annotation.NonNull;

import com.example.rxmvp.RxMvpApplication;
import com.example.rxmvp.data.api.RxMvpApi;
import com.example.rxmvp.data.api.requests.RegisterRequest;
import com.example.rxmvp.data.api.requests.UserInfoRequest;
import com.example.rxmvp.data.entity.RegisteredInfo;
import com.example.rxmvp.data.entity.UserInfo;
import com.example.rxmvp.data.info.AppIdGenerator;
import com.example.rxmvp.test.IntegrationTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(IntegrationTestRunner.class)
public class UserModelTest {

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    UserModel userModel;

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    AppIdGenerator appIdGenerator;

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    AuthModel authModel;

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    RxMvpApi rxMvpApi;

    @Before
    public void beforeEachTest() {
        appIdGenerator = RxMvpApplication.appComponent(IntegrationTestRunner.app()).appIdGenerator();
        authModel = spy(RxMvpApplication.appComponent(IntegrationTestRunner.app()).authModel());
        rxMvpApi = mock(RxMvpApi.class);
        userModel = new UserModel(appIdGenerator, authModel, rxMvpApi);
    }

    @Test
    public void registerUser_savesTokenAndCurrentUser() {
        String appId = appIdGenerator.appDeviceId().toBlocking().value();
        String name = "name";
        String password = "password";
        RegisteredInfo regInfo = RegisteredInfo.builder().token("token").name(name).build();

        when(rxMvpApi.registerUser(RegisterRequest.builder().name(name).password(password).appId(appId).build()))
                .thenReturn(Single.just(regInfo));

        TestSubscriber subscriber = TestSubscriber.create();
        userModel.registerUser(name, password).subscribe(subscriber);
        subscriber.assertNoErrors();
        subscriber.awaitTerminalEvent();

        assertThat(authModel.getToken().toBlocking().value()).isEqualTo("token");
        assertThat(userModel.observeCurrentUser().toBlocking().first()).isEqualTo(regInfo);
    }

    @Test
    public void getUserInfo_shouldReturnInfo() {
        String appId = appIdGenerator.appDeviceId().toBlocking().value();
        String name = "name";
        String token = "token";

        when(authModel.getToken()).thenReturn(Single.just(token));
        UserInfo userInfo = UserInfo.builder().name(name).avatarUrl("avatar").build();
        when(rxMvpApi.loadUserInfo(UserInfoRequest.builder().name(name).token(token).appId(appId).build()))
                .thenReturn(Single.just(userInfo));

        TestSubscriber<UserInfo> subscriber = TestSubscriber.create();
        userModel.getUserInfo(name).subscribe(subscriber);
        subscriber.assertNoErrors();
        subscriber.awaitTerminalEvent();
        subscriber.assertValue(userInfo);
    }

    @Test
    public void getUserInfo_shouldTakeValueFromCache() {
        String appId = appIdGenerator.appDeviceId().toBlocking().value();
        String name = "name";
        String token = "token";

        when(authModel.getToken()).thenReturn(Single.just(token));
        UserInfo userInfo = UserInfo.builder().name(name).avatarUrl("avatar").build();
        when(rxMvpApi.loadUserInfo(UserInfoRequest.builder().name(name).token(token).appId(appId).build()))
                .thenReturn(Single.just(userInfo));

        TestSubscriber<UserInfo> subscriber1 = TestSubscriber.create();
        userModel.getUserInfo(name).subscribe(subscriber1);
        subscriber1.awaitTerminalEvent();
        subscriber1.assertValue(userInfo);

        TestSubscriber<UserInfo> subscriber2 = TestSubscriber.create();
        userModel.getUserInfo(name).subscribe(subscriber2);
        subscriber2.awaitTerminalEvent();
        subscriber2.assertValue(userInfo);

        //noinspection CheckResult
        verify(rxMvpApi, times(1)).loadUserInfo(any());
    }
}