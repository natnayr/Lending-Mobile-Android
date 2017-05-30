package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.ServerReply;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutSummaryResponse {

    @SerializedName("server")
    @Expose
    private ServerReply serverReply;

    @SerializedName("investments")
    @Expose
    private List<InvestmentResponse> investments;

    @SerializedName("status")
    @Expose
    private int status;

    public ServerReply getServerReply(){
        return serverReply;
    }

    public void setServerReply(ServerReply serverReply){
        this.serverReply = serverReply;
    }

    public List<InvestmentResponse> getInvestments() {
        return investments;
    }

    public void setInvestments(List<InvestmentResponse> investments) {
        this.investments = investments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
