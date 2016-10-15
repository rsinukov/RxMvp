package com.example.rxmvp.ui.presenters;

import android.support.annotation.NonNull;

import com.example.rxmvp.test.IntegrationTestRunner;
import com.example.rxmvp.ui.views.LceView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Single;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(IntegrationTestRunner.class)
public class LcePresenterTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private LcePresenter<Object, LceView<Object>> presenter;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private LceView<Object> view;

    @Before
    public void beforeEachTest() {
        view = mock(LceView.class);
        presenter = new LcePresenter<Object, LceView<Object>>(
                LcePresenter.Config.create(Schedulers.immediate(), Schedulers.immediate())
        ) { };
    }

    @Test
    public void load_shouldNotifyViewAboutLoadingIfViewBound() {
        final Object value = new Object();
        Single<Object> single = Single.just(value);

        presenter.bindView(view, null);
        presenter.load(single);
        verify(view).showLoading();
    }

    @Test
    public void load_shouldNotifyViewAboutResultIfViewBound() {
        final Object value = new Object();
        Single<Object> single = Single.just(value);

        presenter.bindView(view, null);
        presenter.load(single);
        verify(view).onSuccess(value);
    }

    @Test
    public void load_shouldNotifyViewAboutErrorIfViewBound() {
        final Throwable value = new AssertionError();
        Single<Object> single = Single.error(value);

        presenter.bindView(view, null);
        presenter.load(single);
        verify(view).onError(value);
    }

    @Test
    public void load_shouldNotifyViewAboutLoadingIfViewBoundAfter() {
        Single<Object> single = PublishSubject.create().toSingle();

        presenter.load(single);
        presenter.bindView(view, null);
        verify(view).showLoading();
    }

    @Test
    public void load_shouldNotifyViewAboutResultIfViewBoundAfter() {
        final Object value = new Object();
        Single<Object> single = Single.just(value);

        presenter.load(single);
        presenter.bindView(view, null);
        verify(view).onSuccess(value);
    }

    @Test
    public void load_shouldNotifyViewAboutErrorIfViewBoundAfter() {
        final Throwable value = new AssertionError();
        Single<Object> single = Single.error(value);

        presenter.load(single);
        presenter.bindView(view, null);
        verify(view).onError(value);
    }
}