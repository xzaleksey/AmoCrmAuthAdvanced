package com.valyakinaleksey.amocrm.domain;

import com.valyakinaleksey.amocrm.models.api.Account;
import com.valyakinaleksey.amocrm.models.api.AmoResponse;
import com.valyakinaleksey.amocrm.models.api.AuthResponse;
import com.valyakinaleksey.amocrm.models.api.BaseCallback;
import com.valyakinaleksey.amocrm.models.events.AccountAuthEvent;
import com.valyakinaleksey.amocrm.models.events.BaseAuthEvent;
import com.valyakinaleksey.amocrm.util.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

/**
 * Created by avalyakin on 07.04.2016.
 */
public class Authentificator {
    public static ServiceGenerator.AmoCrmApiInterface client;
    public static ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public static void baseAuth(final String login, final String password) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                client = getClient();
                Call<AmoResponse<AuthResponse>> itemCall = client.apiLoginBase(login, password);
                itemCall.enqueue(new BaseCallback<AmoResponse<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<AmoResponse<AuthResponse>> call, retrofit2.Response<AmoResponse<AuthResponse>> response) {
                        Logger.d(response.toString());
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            EventBus.getDefault().post(new BaseAuthEvent(response.body().response.accounts));
                        }
                    }
                });
            }
        });
    }

    public static void accountAuth(final Account account, final String login, final String password) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                client = getClient();
                Call<Account[]> itemCall = client.getAccountDomains(account.subdomain);
                itemCall.enqueue(new BaseCallback<Account[]>() {
                    @Override
                    public void onResponse(Call<Account[]> call, retrofit2.Response<Account[]> response) {
                        Logger.d(response.toString());
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            ServiceGenerator.setBaseUrl(response.body()[0].account_domain);
                            client = getClient();
                            Call<AmoResponse<AuthResponse>> itemCall = client.apiLogin(login, password);
                            itemCall.enqueue(new BaseCallback<AmoResponse<AuthResponse>>() {
                                @Override
                                public void onResponse(Call<AmoResponse<AuthResponse>> call, retrofit2.Response<AmoResponse<AuthResponse>> response) {
                                    super.onResponse(call, response);
                                    if (response.isSuccessful()) {
                                        AuthResponse authResponse = response.body().response;
                                        if (authResponse.auth) {
                                            EventBus.getDefault().post(new AccountAuthEvent(response));
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

    }

    public static void resetClient() {
        client = null;
    }

    private static ServiceGenerator.AmoCrmApiInterface getClient() {
        return client == null ? ServiceGenerator.createService(ServiceGenerator.AmoCrmApiInterface.class) : client;
    }
}
