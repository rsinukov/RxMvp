package com.example.rxmvp.ui.presenters;


import android.support.annotation.NonNull;

import com.example.rxmvp.data.entity.RegisteredInfo;
import com.example.rxmvp.data.entity.UserInfo;
import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.test.IntegrationTestRunner;
import com.example.rxmvp.ui.views.LceView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(IntegrationTestRunner.class)
public class UserInfoPresenterTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private UserInfoPresenter presenter;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private LceView<UserInfo> view;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private UserModel userModel;

    @Before
    public void beforeEachTest() {
        view = mock(LceView.class);
        userModel = mock(UserModel.class);
        presenter = new UserInfoPresenter(
                userModel,
                LcePresenter.Config.create(Schedulers.immediate(), Schedulers.immediate())
        );
    }

    @Test
    public void bindView_shouldLoadCurrentUser() {
        RegisteredInfo regInfo = regInfo(1);
        when(userModel.observeCurrentUser()).thenReturn(Observable.just(regInfo));
        UserInfo userInfo = userInfo(1);
        when(userModel.getUserInfo(regInfo.name())).thenReturn(Single.just(userInfo));

        presenter.bindView(view, null);

        verify(view).onSuccess(userInfo);
    }

    @Test
    public void bindView_shouldSubscribeToUserChanges() {
        PublishSubject<RegisteredInfo> regInfoSubject = PublishSubject.create();
        when(userModel.observeCurrentUser()).thenReturn(regInfoSubject);

        RegisteredInfo regInfo1 = regInfo(1);
        RegisteredInfo regInfo2 = regInfo(2);
        UserInfo userInfo1 = userInfo(1);
        UserInfo userInfo2 = userInfo(2);
        when(userModel.getUserInfo(regInfo1.name())).thenReturn(Single.just(userInfo1));
        when(userModel.getUserInfo(regInfo2.name())).thenReturn(Single.just(userInfo2));

        presenter.bindView(view, null);

        InOrder inOrderView = inOrder(view);

        regInfoSubject.onNext(regInfo1);
        inOrderView.verify(view).onSuccess(userInfo1);

        regInfoSubject.onNext(regInfo2);
        inOrderView.verify(view).onSuccess(userInfo2);
    }

    // TODO: tests for filter and distinctUntilChanged

    @NonNull
    private UserInfo userInfo(int index) {
        return UserInfo.builder().avatarUrl("avatar" + index).name("name" + index).build();
    }

    @NonNull
    private RegisteredInfo regInfo(int index) {
        return RegisteredInfo.builder().name("name" + index).token("token" + index).build();
    }
}