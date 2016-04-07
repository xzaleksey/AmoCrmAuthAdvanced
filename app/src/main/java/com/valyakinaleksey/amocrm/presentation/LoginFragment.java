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
import com.valyakinaleksey.amocrm.domain.Authentificator;
import com.valyakinaleksey.amocrm.models.api.APIError;
import com.valyakinaleksey.amocrm.models.events.AccountAuthEvent;
import com.valyakinaleksey.amocrm.models.events.AccountChosenEvent;
import com.valyakinaleksey.amocrm.models.events.ApiErrorEvent;
import com.valyakinaleksey.amocrm.models.events.BaseAuthEvent;
import com.valyakinaleksey.amocrm.util.ErrorUtils;
import com.valyakinaleksey.amocrm.util.Logger;
import com.valyakinaleksey.amocrm.util.Session;
import com.valyakinaleksey.amocrm.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {
    private EditText etLogin;
    private EditText etPassword;
    private AccountsFragment accountsFragment;
    private View btnLogin;
    private View progressBar;

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
        btnLogin = view.findViewById(R.id.btn_auth);
        progressBar = view.findViewById(R.id.progress_bar);
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
                    return;
                }
                Authentificator.baseAuth(login, password);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiErrorEvent(ApiErrorEvent apiErrorEvent) {
        displayLoading(false);
        APIError apiError = apiErrorEvent.apiError;
        if (apiError != null && !TextUtils.isEmpty(apiError.error)) {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseAuthEvent(BaseAuthEvent baseAuthEvent) {
        displayLoading(false);
        accountsFragment = new AccountsFragment();
        accountsFragment.setAccountList(baseAuthEvent.accountList);
        accountsFragment.show(getFragmentManager(), AccountsFragment.TAG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountAuthEvent(AccountAuthEvent accountAuthEvent) {
        displayLoading(false);
        Session.saveSession(accountAuthEvent.response, getContext());
        ToastUtils.showShortMessage("Login successful", getContext());
        ((Navigator) getActivity()).navigateToFragment(new LeadsFragment());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountChosenEvent(AccountChosenEvent accountChosenEvent) {
        if (accountsFragment != null && accountsFragment.isVisible()) {
            accountsFragment.dismiss();
        }
        displayLoading(true);
        Authentificator.accountAuth(accountChosenEvent.account, etLogin.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void displayLoading(boolean b) {
        btnLogin.setEnabled(!b);
        progressBar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }
}
