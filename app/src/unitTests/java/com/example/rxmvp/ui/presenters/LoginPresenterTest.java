package com.example.rxmvp.ui.presenters;

import android.support.annotation.NonNull;

import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.test.IntegrationTestRunner;
import com.example.rxmvp.ui.views.LceView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Completable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(IntegrationTestRunner.class)
public class LoginPresenterTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private LoginPresenter presenter;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private LceView<String> view;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private UserModel userModel;

    @Before
    public void beforeEachTest() {
        view = mock(LceView.class);
        userModel = mock(UserModel.class);
        presenter = new LoginPresenter(
                userModel,
                LcePresenter.Config.create(Schedulers.immediate(), Schedulers.immediate())
        );
    }

    @Test
    public void registerUser_shouldNotifyViewAboutError() {
        final String name = "name";
        final String password = "password";
        Throwable error = new AssertionError();
        when(userModel.registerUser(name, password)).thenReturn(Completable.error(error));

        presenter.bindView(view, null);
        presenter.registerUser(name, password);

        verify(view).onError(error);
    }

    @Test
    public void registerUser_shouldNotifyViewAboutSuccess() {
        final String name = "name";
        final String password = "password";
        when(userModel.registerUser(name, password)).thenReturn(Completable.complete());

        presenter.bindView(view, null);
        presenter.registerUser(name, password);

        verify(view).onSuccess(name);
    }
}