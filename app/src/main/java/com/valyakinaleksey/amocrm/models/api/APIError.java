package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

public class APIError {
    @SerializedName("error")
    public String error;
    @SerializedName("error_code")
    public int error_code;
}
