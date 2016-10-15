package com.example.rxmvp.models;

import android.support.annotation.NonNull;

import com.example.rxmvp.RxMvpApplication;
import com.example.rxmvp.test.IntegrationTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(IntegrationTestRunner.class)
public class AuthModelTest {

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    AuthModel authModel;

    @Before
    public void beforeEachTest() {
        authModel = RxMvpApplication.appComponent(IntegrationTestRunner.app()).authModel();
    }

    @Test
    public void authModel_savesToken() {
        String token1 = "token1";
        String token2 = "token2";

        authModel.setToken(token1).subscribe();
        assertThat(authModel.getToken().toBlocking().value()).isEqualTo(token1);

        authModel.setToken(token2).subscribe();
        assertThat(authModel.getToken().toBlocking().value()).isEqualTo(token2);
    }
}