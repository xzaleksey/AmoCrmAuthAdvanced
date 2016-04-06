package com.valyakinaleksey.amocrm.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.valyakinaleksey.amocrm.R;
import com.valyakinaleksey.amocrm.domain.ServiceGenerator;
import com.valyakinaleksey.amocrm.models.api.APIError;
import com.valyakinaleksey.amocrm.models.api.AuthResponse;
import com.valyakinaleksey.amocrm.models.api.Response;
import com.valyakinaleksey.amocrm.util.ErrorUtils;
import com.valyakinaleksey.amocrm.util.Logger;
import com.valyakinaleksey.amocrm.util.Session;
import com.valyakinaleksey.amocrm.util.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private EditText etLogin;
    private EditText etPassword;

    public LoginFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        etLogin = (EditText) view.findViewById(R.id.et_login);

        etPassword = (EditText) view.findViewById(R.id.et_password);
        initLogin(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (etLogin.getText().toString().isEmpty()) {
            etLogin.setText("xzaleksey@gmail.com");
            etPassword.setText("nighthunger00");
        }
    }

    private void initLogin(final View view) {
        final View btnLogin = view.findViewById(R.id.btn_auth);
        final View progressBar = view.findViewById(R.id.progress_bar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoading(true);
                final View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                if (login.isEmpty() && password.isEmpty()) {
                    ToastUtils.showShortMessage("fill login and password", getContext());
                }
//                ServiceGenerator.AmoCrmApiInterface client = ServiceGenerator.getClient(GsonConverterFactory.create());
                ServiceGenerator.AmoCrmApiInterface client = ServiceGenerator.createService(ServiceGenerator.AmoCrmApiInterface.class);
                Call<Response<AuthResponse>> itemCall = client.apiLogin(login, password);
                itemCall.enqueue(new Callback<Response<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<Response<AuthResponse>> call, retrofit2.Response<Response<AuthResponse>> response) {
                        Logger.d(response.toString());
                        if (response.isSuccessful()) {
                            Session.saveSession(response, getContext());
                            ToastUtils.showShortMessage("Login successful", getContext());
                            ((Navigator) getActivity()).navigateToFragment(new LeadsFragment());
                        } else {
                            APIError apiError = ErrorUtils.parseError(response);
                            displayLoading(false);
                            if (!TextUtils.isEmpty(apiError.error)) {
                                ToastUtils.showShortMessage(apiError.error, getContext());
                                switch (apiError.error_code) {
                                    //и т д. здесь можно обрабатывать наши ошибки
                                    case ErrorUtils.ERROR_AUTH_LOGIN_PASSWORD:
                                        break;
                                    case ErrorUtils.ERROR_AUTH_CAPTCHA:
                                        break;
                                }
                                Logger.d(apiError.error);
                                Logger.d(String.valueOf(apiError.error_code));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<AuthResponse>> call, Throwable t) {
                        Logger.d(t.toString());
                        ToastUtils.showShortMessage("Check your internet connection", getContext());
                        displayLoading(false);
                    }
                });
            }

            private void displayLoading(boolean b) {
                btnLogin.setEnabled(!b);
                progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}
