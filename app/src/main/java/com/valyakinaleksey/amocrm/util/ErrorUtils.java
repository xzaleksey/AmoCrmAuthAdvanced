package com.valyakinaleksey.amocrm.util;

import com.google.gson.reflect.TypeToken;
import com.valyakinaleksey.amocrm.domain.ServiceGenerator;
import com.valyakinaleksey.amocrm.models.api.APIError;
import com.valyakinaleksey.amocrm.models.api.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class ErrorUtils {

    public static final int ERROR_AUTH_LOGIN_PASSWORD = 110;
    public static final int ERROR_AUTH_CAPTCHA = 111;
    public static final int ERROR_AUTH__USER_NOT_IN_ACCOUNT = 112;
    public static final int ERROR_AUTH__USER_NOT_IN_WHITE_LIST = 113;
    public static final int ERROR_AUTH__ACCOUNT_NOT_FOUND = 101;
    public static final int ERROR_AUTH__NO_ACCOUNT_DATA = 401;
    public static final int ERROR_LEAD_ADD_LEAD_EMPTY_ARRAY = 213;
    public static final int ERROR_LEAD_ADD_UPDATE_EMPTY_REQUEST = 214;
    public static final int ERROR_LEAD_ADD_UPDATE_WRONG_METHOD = 215;
    public static final int ERROR_LEAD_UPDATE_EMPTY_ARRAY = 216;
    public static final int ERROR_LEAD_UPDATE_PARAMETERS_MISSED = 217;
    public static final int ERROR_LEAD_ADD_LEAD_EMPTY = 240;

    public static final Type ERROR_TYPE = new TypeToken<com.valyakinaleksey.amocrm.models.api.Response<APIError>>() {
    }.getType();

    public static APIError parseError(retrofit2.Response response) {
        Converter<ResponseBody, Response<APIError>> converter =
                ServiceGenerator.retrofit()
                        .responseBodyConverter(ERROR_TYPE, new Annotation[0]);

        APIError error;

        try {
            com.valyakinaleksey.amocrm.models.api.Response<APIError> apiErrorResponse = converter.convert(response.errorBody());
            error = apiErrorResponse.response;
        } catch (IOException e) {
            Logger.d(e.toString());
            return new APIError();
        }

        return error;
    }
}
