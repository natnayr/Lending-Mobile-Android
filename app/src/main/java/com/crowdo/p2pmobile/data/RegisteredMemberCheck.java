package com.crowdo.p2pmobile.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredMemberCheck {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("registered_singapore")
    @Expose
    public boolean registeredSingapore;

    @SerializedName("registered_indonesia")
    @Expose
    public boolean registeredIndonesia;

}