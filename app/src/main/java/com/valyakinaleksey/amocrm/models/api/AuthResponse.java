package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {
    @SerializedName("auth")
    boolean auth;
    @SerializedName("accounts")
    List<Account> accounts;
}
