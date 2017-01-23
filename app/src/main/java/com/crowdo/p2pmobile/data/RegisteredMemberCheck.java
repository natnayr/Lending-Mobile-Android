package com.crowdo.p2pmobile.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredMemberCheck {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("approval_status")
    @Expose
    public String approvalStatus;

}