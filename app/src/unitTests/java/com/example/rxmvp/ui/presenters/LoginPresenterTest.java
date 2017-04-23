package com.example.rxmvp.ui.presenters;

import android.support.annotation.NonNull;

import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.test.IntegrationTestRunner;
import com.example.rxmvp.ui.views.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

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
    private LoginView view;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private UserModel userModel;

    @Before
    public void beforeEachTest() {
        view = mock(LoginView.class);
        userModel = mock(UserModel.class);
        when(view.password()).thenReturn(Observable.empty());
        when(view.userName()).thenReturn(Observable.empty());
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

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValid() {
        when(view.password()).thenReturn(Observable.just("ABC"));
        when(view.userName()).thenReturn(Observable.just("DEF"));

        presenter.bindView(view, null);

        verify(view).onFieldsValidChanged(true);
    }

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValidOnShortName() {
        when(view.password()).thenReturn(Observable.just("AB"));
        when(view.userName()).thenReturn(Observable.just("DEF"));

        presenter.bindView(view, null);

        verify(view).onFieldsValidChanged(false);
    }

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValidOnShortPassword() {
        when(view.password()).thenReturn(Observable.just("ABC"));
        when(view.userName()).thenReturn(Observable.just("DE"));

        presenter.bindView(view, null);

        verify(view).onFieldsValidChanged(false);
    }

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValidOnShortNameAndPassword() {
        when(view.password()).thenReturn(Observable.just("AB"));
        when(view.userName()).thenReturn(Observable.just("DE"));

        presenter.bindView(view, null);

        verify(view).onFieldsValidChanged(false);
    }

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValidWhenNameChanged() {
        final BehaviorSubject<CharSequence> nameSubject = BehaviorSubject.create();
        when(view.password()).thenReturn(nameSubject);
        when(view.userName()).thenReturn(Observable.just("DEF"));

        presenter.bindView(view, null);

        nameSubject.onNext("ABC");
        verify(view).onFieldsValidChanged(true);
        nameSubject.onNext("AB");
        verify(view).onFieldsValidChanged(false);
    }

    @Test
    public void presenter_shouldNotifyAboutViewFieldsValidWhenPasswordChanged() {
        final BehaviorSubject<CharSequence> passwordSubject = BehaviorSubject.create();
        when(view.password()).thenReturn(Observable.just("ABC"));
        when(view.userName()).thenReturn(passwordSubject);

        presenter.bindView(view, null);

        passwordSubject.onNext("DEF");
        verify(view).onFieldsValidChanged(true);
        passwordSubject.onNext("DE");
        verify(view).onFieldsValidChanged(false);
    }
}