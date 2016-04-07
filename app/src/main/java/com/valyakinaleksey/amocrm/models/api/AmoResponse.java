package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

public class AmoResponse<T> {
    @SerializedName("response")
    public T response;
}
