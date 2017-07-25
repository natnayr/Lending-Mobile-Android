package com.crowdo.p2pconnect.model.others;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 25/7/17.
 */

public class ServerMoshi {

    @Json(name = "message")
    public String message;

    @Json(name = "status")
    public int status;
}
