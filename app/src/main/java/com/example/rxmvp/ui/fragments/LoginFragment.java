package com.example.rxmvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rxmvp.R;
import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.ui.presenters.LoginPresenter;
import com.example.rxmvp.ui.views.LoginView;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import rx.Observable;

import static com.example.rxmvp.RxMvpApplication.appComponent;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class LoginFragment extends Fragment implements LoginView {

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_user_name_field)
    @NonNull
    EditText userNameField;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_password_field)
    @NonNull
    EditText passwordField;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_register_button)
    @NonNull
    Button loginButton;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_register_container)
    @NonNull
    View registerContainer;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_loading)
    @NonNull
    View progressContainer;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_login_success)
    @NonNull
    View successContainer;

    @SuppressWarnings("NullableProblems") // onCreate
    @NonNull
    @Inject
    LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        appComponent(getActivity()).plus(new LoginFragmentModule()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter.bindView(this, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        presenter.unbindView(this);
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @OnClick(R.id.fragment_login_register_button)
    void onRegisterClick() {
        // TODO: check fields
        presenter.registerUser(userNameField.getText().toString(), passwordField.getText().toString());
    }

    @Override
    public void onFieldsValidChanged(boolean isValid) {
        loginButton.setEnabled(isValid);
    }

    @Override
    @NonNull
    public Observable<CharSequence> userName() {
        return RxTextView.textChanges(userNameField);
    }

    @Override
    @NonNull
    public Observable<CharSequence> password() {
        return RxTextView.textChanges(passwordField);
    }

    @Override
    public void onSuccess(@NonNull String result) {
        progressContainer.setVisibility(View.GONE);
        registerContainer.setVisibility(View.GONE);
        successContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(@NonNull Throwable error) {
        // TODO: show error layout
        Toast.makeText(getActivity(), "Can't register. Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        registerContainer.setVisibility(View.GONE);
        successContainer.setVisibility(View.GONE);
        progressContainer.setVisibility(View.VISIBLE);
    }

    @Module
    public static class LoginFragmentModule {

        @Provides
        @NonNull
        public LoginPresenter provideLoginPresenter(@NonNull UserModel userModel) {
            return new LoginPresenter(userModel, LoginPresenter.Config.create(io(), mainThread()));
        }
    }

    @Subcomponent(modules = LoginFragmentModule.class)
    public interface LoginFragmentComponent {

        void inject(@NonNull LoginFragment loginFragment);
    }
}
