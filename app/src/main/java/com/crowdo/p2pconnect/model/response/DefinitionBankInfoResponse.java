package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 25/7/17.
 */

public class DefinitionBankInfoResponse {

    @Json(name = "server")
    public Server server;
    @Json(name = "bank_info")
    public BankInfo bankInfo;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

    public class Server {
        @Json(name = "message")
        public String message;
        @Json(name = "status")
        public long status;
    }


}
