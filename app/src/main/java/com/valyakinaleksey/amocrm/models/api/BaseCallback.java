package com.valyakinaleksey.amocrm.models.api;

import com.valyakinaleksey.amocrm.models.events.ApiErrorEvent;
import com.valyakinaleksey.amocrm.util.ErrorUtils;
import com.valyakinaleksey.amocrm.util.Logger;

import org.greenrobot.eventbus.EventBus;

import retrofit2.*;

/**
 * Created by avalyakin on 07.04.2016.
 */
public abstract class BaseCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        if (!response.isSuccessful()) {
            APIError apiError = ErrorUtils.parseError(response);
            EventBus.getDefault().post(new ApiErrorEvent(apiError));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Logger.d(t.toString());
        APIError apiError = new APIError();
        apiError.error_code = ErrorUtils.ERROR_INTERNET_CONNECTION;
        apiError.error = t.toString();
        EventBus.getDefault().post(new ApiErrorEvent(apiError));
    }
}
