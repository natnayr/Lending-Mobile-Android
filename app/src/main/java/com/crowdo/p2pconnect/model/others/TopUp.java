package com.crowdo.p2pconnect.model.others;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 31/7/17.
 */

public class TopUp {

    @Json(name = "id")
    private long id;

    @Json(name = "wallet_id")
    private long walletId;

    @Json(name = "amount")
    @FallbackOnNull(fallbackLong = 0)
    private long amount;

    @Json(name = "origin")
    private String origin;

    @Json(name = "status")
    private String status;

    @Json(name = "payment_mode")
    private String paymentMode;

    @Json(name = "transaction_reference")
    private String transactionReference;

    @Json(name = "transaction_proof")
    private TransactionProof transactionProof;

    @Json(name = "created_at")
    private String createdAt;

    @Json(name = "updated_at")
    private String updatedAt;

    public long getId() {
        return id;
    }

    public long getWalletId() {
        return walletId;
    }

    public long getAmount() {
        return amount;
    }

    public String getOrigin() {
        return origin;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getTransactionProofUrl() {
        return transactionProof.getUrl();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    private static class TransactionProof {

        @Json(name = "url")
        private String url;

        public String getUrl() {
            return url;
        }

    }
}
