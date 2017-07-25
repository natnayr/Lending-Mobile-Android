package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.others.ServerMoshi;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberInfoResponse {

    @Json(name = "server")
    public ServerMoshi server;
    @Json(name = "name")
    public String name;
    @Json(name = "email")
    public String email;
    @Json(name = "user_id")
    public long userId;
    @Json(name = "number_of_pending_bids")
    public int numberOfPendingBids;
    @Json(name = "total_pending_bid_amount")
    public long totalPendingBidAmount;
    @Json(name = "available_cash_balance")
    public long availableCashBalance;
    @Json(name = "bank_info")
    public BankInfo bankInfo;


}

