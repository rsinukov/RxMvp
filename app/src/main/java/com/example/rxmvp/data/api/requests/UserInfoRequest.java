package com.example.rxmvp.data.api.requests;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_UserInfoRequest.Builder.class)
public abstract class UserInfoRequest {

    @JsonProperty("app_id")
    @NonNull
    public abstract String appId();

    @JsonProperty("token")
    @NonNull
    public abstract String token();

    @JsonProperty("name")
    @NonNull
    public abstract String name();

    @NonNull
    public static Builder builder() {
        return new AutoValue_UserInfoRequest.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @JsonProperty("app_id")
        @NonNull
        public abstract Builder appId(@NonNull String appId);

        @JsonProperty("token")
        @NonNull
        public abstract Builder token(@NonNull String token);

        @JsonProperty("name")
        @NonNull
        public abstract Builder name(@NonNull String name);

        @NonNull
        public abstract UserInfoRequest build();
    }
}
