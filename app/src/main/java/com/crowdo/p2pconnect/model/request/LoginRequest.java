package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 27/3/17.
 */
public class LoginRequest {

    @Json(name = "email")
    public String email;
    @Json(name = "password")
    public String password;
    @Json(name = "device_id")
    public String deviceId;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String deviceId) {
        super();
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }
}