package com.example.rxmvp.data.api.requests;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_RegisterRequest.Builder.class)
public abstract class RegisterRequest {

    @JsonProperty("app_id")
    @NonNull
    public abstract String appId();

    @JsonProperty("name")
    @NonNull
    public abstract String name();

    @JsonProperty("password")
    @NonNull
    public abstract String password();

    @NonNull
    public static Builder builder() {
        return new AutoValue_RegisterRequest.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @JsonProperty("app_id")
        @NonNull
        public abstract Builder appId(@NonNull String appId);

        @JsonProperty("name")
        @NonNull
        public abstract Builder name(@NonNull String name);

        @JsonProperty("family_name")
        @NonNull
        public abstract Builder password(@NonNull String password);

        @NonNull
        public abstract RegisterRequest build();
    }
}
