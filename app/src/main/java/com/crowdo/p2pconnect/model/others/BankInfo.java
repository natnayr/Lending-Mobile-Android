package com.crowdo.p2pconnect.model.others;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 14/7/17.
 */

public class BankInfo {
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_number")
    @Expose
    private String accountNumber;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("account_holder")
    @Expose
    private String accountHolder;
    @SerializedName("swift_code")
    @Expose
    private String swiftCode;
    @SerializedName("bank_address")
    @Expose
    private String bankAddress;
    @SerializedName("bank_country")
    @Expose
    private String bankCountry;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }
}
