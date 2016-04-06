package com.valyakinaleksey.amocrm.domain;

import android.text.TextUtils;

import com.valyakinaleksey.amocrm.models.api.AuthResponse;
import com.valyakinaleksey.amocrm.models.api.LeadsResponse;
import com.valyakinaleksey.amocrm.models.api.LeadsStatusesResponse;
import com.valyakinaleksey.amocrm.models.api.Response;
import com.valyakinaleksey.amocrm.util.Logger;
import com.valyakinaleksey.amocrm.util.Session;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public class ServiceGenerator {
    public static final String USER_LOGIN_P = "USER_LOGIN";
    public static final String USER_PASSWORD_P = "USER_PASSWORD";
    private static final String BASE_URL = "https://andxzalekseygmailcomibqb.amocrm.ru";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient okClient = new OkHttpClient.Builder().
                addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        if (!TextUtils.isEmpty(Session.SESSION_ID)) {
                            Logger.d(Session.SESSION_ID);
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Cookie", Session.SESSION_ID)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                        return chain.proceed(chain.request());
                    }
                }).build();
        Retrofit retrofit = builder.client(okClient).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return builder.build();
    }

    public interface AmoCrmApiInterface {

        @FormUrlEncoded
        @POST("/private/api/auth.php?type=json")
        Call<Response<AuthResponse>> apiLogin(@Field(USER_LOGIN_P) String login, @Field(USER_PASSWORD_P) String password);

        @GET("/private/api/v2/json/leads/list")
        Call<Response<LeadsResponse>> getLeads();

        @GET("/private/api/v2/json/accounts/current")
        Call<Response<LeadsStatusesResponse>> getLeadStatuses();
//        @PUT("/user/{id}/update")
//        Call<Response> updateUser(@Path("id") String id, @Body Response user);
    }

}