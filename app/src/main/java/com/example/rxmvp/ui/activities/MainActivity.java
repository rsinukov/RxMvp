package com.example.rxmvp.ui.activities;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rxmvp.R;
import com.example.rxmvp.ui.fragments.LoginFragment;
import com.example.rxmvp.ui.fragments.UserInfoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            LoginFragment loginFragment = new LoginFragment();
            UserInfoFragment userInfoFragment = new UserInfoFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_activity_register, loginFragment)
                    .add(R.id.main_activity_user_info, userInfoFragment)
                    .commit();
        }
    }
}
