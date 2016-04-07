package com.valyakinaleksey.amocrm.models.events;

import com.valyakinaleksey.amocrm.models.api.Account;

import java.util.List;

/**
 * Created by avalyakin on 07.04.2016.
 */
public class BaseAuthEvent {
    public final List<Account> accountList;

    public BaseAuthEvent(List<Account> response) {
        this.accountList = response;
    }
}
