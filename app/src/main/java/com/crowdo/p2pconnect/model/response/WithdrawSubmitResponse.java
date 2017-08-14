package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.others.WalletEntry;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 14/8/17.
 */

public class WithdrawSubmitResponse {
    @Json(name = "server")
    private Server server;
    @Json(name = "withdraw")
    private WalletEntry walletEntry;

    public Server getServer() {
        return server;
    }

    public WalletEntry getWalletEntry() {
        return walletEntry;
    }
}
