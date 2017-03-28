package com.crowdo.p2pconnect.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 28/3/17.
 */

public class ShortResponse {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("status")
    @Expose
    public int status;

}
