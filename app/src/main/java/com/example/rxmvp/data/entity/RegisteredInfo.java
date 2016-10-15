package com.example.rxmvp.data.entity;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_RegisteredInfo.Builder.class)
public abstract class RegisteredInfo {

    @JsonProperty("token")
    @NonNull
    public abstract String token();

    @JsonProperty("name")
    @NonNull
    public abstract String name();

    @NonNull
    public static Builder builder() {
        return new AutoValue_RegisteredInfo.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {

        @NonNull
        public abstract Builder token(@NonNull String token);

        @NonNull
        public abstract Builder name(@NonNull String name);

        @NonNull
        public abstract RegisteredInfo build();
    }
}
