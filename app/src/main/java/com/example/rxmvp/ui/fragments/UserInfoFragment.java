package com.example.rxmvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rxmvp.R;
import com.example.rxmvp.data.entity.UserInfo;
import com.example.rxmvp.models.UserModel;
import com.example.rxmvp.ui.presenters.UserInfoPresenter;
import com.example.rxmvp.ui.views.LceView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

import static com.example.rxmvp.RxMvpApplication.appComponent;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class UserInfoFragment extends Fragment implements LceView<UserInfo> {

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_user_info_avatar)
    @NonNull
    ImageView avatarImage;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_user_info_name_label)
    @NonNull
    TextView userNameLabel;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_user_info_container)
    @NonNull
    View infoContainer;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_user_info_loading)
    @NonNull
    View progressContainer;

    @SuppressWarnings("NullableProblems") // onViewCreated
    @Bind(R.id.fragment_user_info_waiting)
    @NonNull
    View waitingContainer;

    @SuppressWarnings("NullableProblems") // onCreate
    @Inject
    @NonNull
    UserInfoPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note that tis fragment does not retain it's instance
        // Saving state is not required, cause all data taken from model
        appComponent(getActivity()).plus(new UserInfoFragmentModule()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
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
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        waitingContainer.setVisibility(View.GONE);
        infoContainer.setVisibility(View.GONE);
        progressContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(@NonNull UserInfo result) {
        userNameLabel.setText(result.name());
        Picasso.with(getActivity())
                .load(result.avatarUrl())
                .into(avatarImage);

        progressContainer.setVisibility(View.GONE);
        waitingContainer.setVisibility(View.GONE);
        infoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(@NonNull Throwable error) {
        // TODO: show error layout
        Toast.makeText(getActivity(), "Can't register. Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Module
    public static class UserInfoFragmentModule {

        @Provides
        @NonNull
        public UserInfoPresenter provideLoginPresenter(@NonNull UserModel userModel) {
            return new UserInfoPresenter(userModel, UserInfoPresenter.Config.create(io(), mainThread()));
        }
    }

    @Subcomponent(modules = UserInfoFragmentModule.class)
    public interface UserInfoFragmentComponent {

        void inject(@NonNull UserInfoFragment loginFragment);
    }
}
