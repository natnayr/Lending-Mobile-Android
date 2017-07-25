package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 25/7/17.
 */

public class DefinitionBankInfoResponse {

    @Json(name = "server")
    private Server server;
    @Json(name = "bank_info")
    private BankInfo bankInfo;

    public Server getServer() {
        return server;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }
}
