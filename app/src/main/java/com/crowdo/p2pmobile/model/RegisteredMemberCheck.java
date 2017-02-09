package com.crowdo.p2pmobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredMemberCheck {

    @SerializedName("member_id")
    @Expose
    public int memberId;

    @SerializedName("is_member")
    @Expose
    public boolean isMember;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("can_invest_idr")
    @Expose
    public boolean canInvestIdr;

    @SerializedName("can_invest_sgd")
    @Expose
    public boolean canInvestSgd;
}