package com.valyakinaleksey.amocrm.domain;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.valyakinaleksey.amocrm.models.api.Account;
import com.valyakinaleksey.amocrm.models.api.AmoResponse;
import com.valyakinaleksey.amocrm.models.api.AuthResponse;
import com.valyakinaleksey.amocrm.models.api.LeadsResponse;
import com.valyakinaleksey.amocrm.models.api.LeadsStatusesResponse;
import com.valyakinaleksey.amocrm.util.Logger;
import com.valyakinaleksey.amocrm.util.Session;
import com.valyakinaleksey.amocrm.util.SocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
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
    public static final String BASE_URL = "www.amocrm.com";
    private static final String BASE_AUTH = "/account/api_auth.php?type=json";
    public static final String ACCOUNT_AUTH = "/private/api/auth.php?type=json";
    public static final String HTTPS = "https://";

    public static String BASE_ACCOUNT_URL;
    private static Retrofit.Builder builder = getBuilder(HTTPS + BASE_URL);
    private static ConnectionSpec spec;
    private static X509TrustManager trustManager;

    static {
        spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException | KeyStoreException exception) {
            Logger.d(exception.toString());
        }
    }

    public static void setBaseUrl() {
        setBaseUrl(BASE_URL);
    }

    public static void setBaseUrl(String accountDomain) {
        builder = getBuilder(HTTPS + accountDomain);
        Authentificator.resetClient();
    }

    @NonNull
    private static Retrofit.Builder getBuilder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
    }


    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = null;
        try {
            okClient = new OkHttpClient.Builder().
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
                    })
                    .addInterceptor(interceptor)
                    .connectionSpecs(Collections.singletonList(spec))
                    .sslSocketFactory(new SocketFactory(trustManager))
                    .build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = builder.client(okClient).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return builder.build();
    }


    public interface AmoCrmApiInterface {

        @FormUrlEncoded
        @POST("/api/accounts/domains/")
        Call<Account[]> getAccountDomains(@Field("domains[]") String domain);

        @FormUrlEncoded
        @POST(BASE_AUTH)
        Call<AmoResponse<AuthResponse>> apiLoginBase(@Field(USER_LOGIN_P) String login, @Field(USER_PASSWORD_P) String password);

        @FormUrlEncoded
        @POST(ACCOUNT_AUTH)
        Call<AmoResponse<AuthResponse>> apiLogin(@Field(USER_LOGIN_P) String login, @Field(USER_PASSWORD_P) String password);

        @GET("/private/api/v2/json/leads/list")
        Call<AmoResponse<LeadsResponse>> getLeads();

        @GET("/private/api/v2/json/accounts/current")
        Call<AmoResponse<LeadsStatusesResponse>> getLeadStatuses();
//        @PUT("/user/{id}/update")
//        Call<AmoResponse> updateUser(@Path("id") String id, @Body AmoResponse user);
    }

}