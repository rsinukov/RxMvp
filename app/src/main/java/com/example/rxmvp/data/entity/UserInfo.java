package com.example.rxmvp.data.entity;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_UserInfo.Builder.class)
public abstract class UserInfo {

    @JsonProperty("avatarUrl")
    @NonNull
    public abstract String avatarUrl();

    @JsonProperty("name")
    @NonNull
    public abstract String name();

    @NonNull
    public static Builder builder() {
        return new AutoValue_UserInfo.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder avatarUrl(@NonNull String avatarUrl);

        @NonNull
        public abstract Builder name(@NonNull String name);

        @NonNull
        public abstract UserInfo build();
    }
}
