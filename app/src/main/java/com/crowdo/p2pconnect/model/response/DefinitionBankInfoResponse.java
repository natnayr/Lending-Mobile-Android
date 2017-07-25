package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.others.ServerMoshi;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 25/7/17.
 */

public class DefinitionBankInfoResponse {

    @Json(name = "server")
    public ServerMoshi server;
    @Json(name = "bank_info")
    public BankInfo bankInfo;
}
