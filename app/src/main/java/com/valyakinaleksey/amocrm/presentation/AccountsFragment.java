package com.valyakinaleksey.amocrm.presentation;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.amocrm.R;
import com.valyakinaleksey.amocrm.domain.AccountsAdapter;
import com.valyakinaleksey.amocrm.models.api.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avalyakin on 06.04.2016.
 */
public class AccountsFragment extends DialogFragment {
    public static final String TAG = AccountsFragment.class.getSimpleName();
    private List<Account> accountList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Choose account");
        View v = inflater.inflate(R.layout.accounts_fragment, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.rv_accounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AccountsAdapter(accountList));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return v;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}
