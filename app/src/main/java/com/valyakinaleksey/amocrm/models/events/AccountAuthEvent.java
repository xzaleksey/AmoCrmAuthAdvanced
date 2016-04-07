package com.valyakinaleksey.amocrm.models.events;

import com.valyakinaleksey.amocrm.models.api.AmoResponse;
import com.valyakinaleksey.amocrm.models.api.AuthResponse;

/**
 * Created by avalyakin on 07.04.2016.
 */
public class AccountAuthEvent {
    public final retrofit2.Response<AmoResponse<AuthResponse>> response;

    public AccountAuthEvent(retrofit2.Response<AmoResponse<AuthResponse>> response) {
        this.response = response;
    }
}
