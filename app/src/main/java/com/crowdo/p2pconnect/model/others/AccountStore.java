package com.crowdo.p2pconnect.model.others;

import io.realm.RealmObject;

/**
 * Created by cwdsg05 on 6/7/17.
 */

public class AccountStore extends RealmObject {

    private String accountUserName;

    private String accountUserEmail;

    private String accountAuthToken;

    public String getAccountUserName() {
        return accountUserName;
    }

    public void setAccountUserName(String accountUserName) {
        this.accountUserName = accountUserName;
    }

    public String getAccountUserEmail() {
        return accountUserEmail;
    }

    public void setAccountUserEmail(String accountUserEmail) {
        this.accountUserEmail = accountUserEmail;
    }

    public String getAccountAuthToken() {
        return accountAuthToken;
    }

    public void setAccountAuthToken(String accountAuthToken) {
        this.accountAuthToken = accountAuthToken;
    }
}
