package com.valyakinaleksey.amocrm.models.events;

import com.valyakinaleksey.amocrm.models.api.Account;

/**
 * Created by avalyakin on 07.04.2016.
 */
public class AccountChosenEvent {
    public final Account account;

    public AccountChosenEvent(Account account) {
        this.account = account;
    }
}
