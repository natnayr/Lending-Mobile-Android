package com.crowdo.p2pconnect.data.request_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 27/3/17.
 */
public class LoginRequest {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("device_id")
    @Expose
    public String deviceId;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginRequest() {
    }

    /**
     *
     * @param email
     * @param password
     * @param deviceId
     */
    public LoginRequest(String email, String password, String deviceId) {
        super();
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

}