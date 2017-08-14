package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.others.WalletEntry;
import com.squareup.moshi.Json;


/**
 * Created by cwdsg05 on 31/7/17.
 */

public class TopUpSubmitResponse {
    @Json(name = "server")
    private Server server;
    @Json(name = "top_up")
    private WalletEntry walletEntry;

    public Server getServer() {
        return server;
    }

    public WalletEntry getWalletEntry() {
        return walletEntry;
    }
}
