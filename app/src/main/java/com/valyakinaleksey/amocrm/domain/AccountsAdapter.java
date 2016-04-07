package com.valyakinaleksey.amocrm.domain;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valyakinaleksey.amocrm.R;
import com.valyakinaleksey.amocrm.models.api.Account;
import com.valyakinaleksey.amocrm.models.events.AccountChosenEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by avalyakin on 06.04.2016.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private List<Account> accountList;

    public AccountsAdapter(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(
                inflater.inflate(R.layout.account, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(accountList.get(position));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Account account;
        private final TextView tvAccountName;
        private final TextView tvAccountSubDomain;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAccountName = (TextView) itemView.findViewById(R.id.tv_account_name);
            tvAccountSubDomain = (TextView) itemView.findViewById(R.id.tv_account_subdomain);
            itemView.setOnClickListener(this);
        }

        void bind(Account account) {
            this.account = account;
            tvAccountName.setText("name: " + account.name);
            tvAccountSubDomain.setText("subdomain: \n" + account.subdomain);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new AccountChosenEvent(account));
        }
    }
}
