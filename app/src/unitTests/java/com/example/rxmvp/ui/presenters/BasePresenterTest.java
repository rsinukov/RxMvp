package com.example.rxmvp.ui.presenters;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import rx.Subscription;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class BasePresenterTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private BasePresenter<Object> presenter;

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private Object view;

    @Before
    public void beforeEachTest() {
        view = new Object();
        presenter = new BasePresenter<>();
    }

    @Test
    public void bindView_shouldAttachViewToThePresenter() {
        presenter.bindView(view, null);
        assertThat(presenter.view()).isSameAs(view);
    }

    @Test
    public void bindView_shouldThrowIfPreviousViewIsNotUnbounded() {
        presenter.bindView(view, null);

        try {
            presenter.bindView(new Object(), null);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Previous view is not unbounded! previousView = " + view);
        }
    }

    @Test
    public void view_shouldReturnNullByDefault() {
        assertThat(presenter.view()).isNull();
    }

    @Test
    public void executeIfViewBound_shouldExecuteActionIfBound() {
        presenter.bindView(view, null);

        //noinspection unchecked
        Action1<Object> action = mock(Action1.class);
        presenter.executeIfViewBound(action);

        verify(action).call(view);
        verifyNoMoreInteractions(action);
    }

    @Test
    public void executeIfViewBound_shouldNotExecuteActionIfUnbound() {
        //noinspection unchecked
        Action1<Object> action = mock(Action1.class);

        presenter.executeIfViewBound(action);
        verifyZeroInteractions(action);
    }


    @Test
    public void unsubscribeOnUnbindView_shouldWorkAccordingItsContract() {
        presenter.bindView(view, null);

        Subscription subscription1 = mock(Subscription.class);
        Subscription subscription2 = mock(Subscription.class);
        Subscription subscription3 = mock(Subscription.class);

        presenter.unsubscribeOnUnbindView(subscription1, subscription2, subscription3);
        verify(subscription1, never()).unsubscribe();
        verify(subscription2, never()).unsubscribe();
        verify(subscription3, never()).unsubscribe();

        presenter.unbindView(view);
        verify(subscription1).unsubscribe();
        verify(subscription2).unsubscribe();
        verify(subscription3).unsubscribe();
    }

    @Test
    public void unbindView_shouldNullTheViewReference() {
        presenter.bindView(view, null);
        assertThat(presenter.view()).isSameAs(view);

        presenter.unbindView(view);
        assertThat(presenter.view()).isNull();
    }

    @Test
    public void unbindView_shouldThrowIfPreviousViewIsNotSameAsExpected() {
        presenter.bindView(view, null);
        Object unexpectedView = new Object();

        try {
            presenter.unbindView(unexpectedView);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Unexpected view! previousView = " + view + ", view to unbind = " + unexpectedView);
        }
    }
}