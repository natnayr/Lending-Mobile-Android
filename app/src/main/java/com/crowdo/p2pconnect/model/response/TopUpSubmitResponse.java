package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.others.TopUp;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 31/7/17.
 */

public class TopUpSubmitResponse {
    @Json(name = "server")
    private Server server;
    @Json(name = "top_ups")
    private TopUp topUp;

    public Server getServer() {
        return server;
    }

    public TopUp getTopUp() {
        return topUp;
    }
}
