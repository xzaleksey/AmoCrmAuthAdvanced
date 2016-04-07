package com.valyakinaleksey.amocrm.models.events;

import com.valyakinaleksey.amocrm.models.api.APIError;

/**
 * Created by avalyakin on 07.04.2016.
 */
public class ApiErrorEvent {
    public final APIError apiError;

    public ApiErrorEvent(APIError apiError) {
        this.apiError = apiError;
    }
}
