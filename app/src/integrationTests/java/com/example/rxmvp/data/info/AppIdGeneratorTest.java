package com.example.rxmvp.data.info;

import android.support.annotation.NonNull;

import com.example.rxmvp.RxMvpApplication;
import com.example.rxmvp.test.IntegrationTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(IntegrationTestRunner.class)
public class AppIdGeneratorTest {

    @SuppressWarnings("NullableProblems") // @Before
    @NonNull
    AppIdGenerator appIdGenerator;

    @Before
    public void beforeEachTest() {
        appIdGenerator = RxMvpApplication.appComponent(IntegrationTestRunner.app()).appIdGenerator();
    }

    @Test
    public void generateNewId_generateRandomIds() {
        String appId1 = appIdGenerator.generateNewId().toBlocking().value();
        String appId2 = appIdGenerator.generateNewId().toBlocking().value();

        assertThat(appId1).isNotEqualTo(appId2);
    }

    @Test
    public void appDeviceId_returnsSameID() {
        String appId1 = appIdGenerator.appDeviceId().toBlocking().value();
        String appId2 = appIdGenerator.appDeviceId().toBlocking().value();

        assertThat(appId1).isEqualTo(appId2);
    }
}