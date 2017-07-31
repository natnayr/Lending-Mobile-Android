package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.others.TopUp;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 31/7/17.
 */

public class TopUpHistoryResponse {

    @Json(name = "server")
    private Server server;
    @Json(name = "top_ups")
    private List<TopUp> topUps = null;

    public Server getServer() {
        return server;
    }

    public List<TopUp> getTopUps() {
        return topUps;
    }

}
