package com.crowdo.p2pconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 21/3/17.
 */

public class Member implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("legacy_p2p_id")
    @Expose
    private Object legacyP2pId;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("reset_password_token")
    @Expose
    private Object resetPasswordToken;
    @SerializedName("reset_password_token_expires_at")
    @Expose
    private Object resetPasswordTokenExpiresAt;
    @SerializedName("reset_password_email_sent_at")
    @Expose
    private Object resetPasswordEmailSentAt;
    @SerializedName("last_login_at")
    @Expose
    private String lastLoginAt;
    @SerializedName("last_logout_at")
    @Expose
    private String lastLogoutAt;
    @SerializedName("last_activity_at")
    @Expose
    private String lastActivityAt;
    @SerializedName("last_login_from_ip_address")
    @Expose
    private String lastLoginFromIpAddress;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_number")
    @Expose
    private Object phoneNumber;
    @SerializedName("id_number")
    @Expose
    private Object idNumber;
    @SerializedName("nationality")
    @Expose
    private Object nationality;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("address1")
    @Expose
    private Object address1;
    @SerializedName("address2")
    @Expose
    private Object address2;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("postal_code")
    @Expose
    private Object postalCode;
    @SerializedName("country_of_residence")
    @Expose
    private Object countryOfResidence;
    @SerializedName("id_document")
    @Expose
    private Object idDocument;
    @SerializedName("id_document_additional")
    @Expose
    private Object idDocumentAdditional;
    @SerializedName("proof_of_residence")
    @Expose
    private Object proofOfResidence;
    @SerializedName("locale_preference")
    @Expose
    private String localePreference;
    @SerializedName("session_country")
    @Expose
    private String sessionCountry;
    @SerializedName("is_admin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("is_banned")
    @Expose
    private Boolean isBanned;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("legacy_ecf_id")
    @Expose
    private Object legacyEcfId;
    @SerializedName("registered_p2p")
    @Expose
    private Boolean registeredP2p;
    @SerializedName("registered_ecf")
    @Expose
    private Boolean registeredEcf;
    @SerializedName("agree_singapore_tnc")
    @Expose
    private Boolean agreeSingaporeTnc;
    @SerializedName("agree_indonesia_tnc")
    @Expose
    private Boolean agreeIndonesiaTnc;
    @SerializedName("agree_malaysia_tnc")
    @Expose
    private Boolean agreeMalaysiaTnc;
    @SerializedName("referral_code")
    @Expose
    private Object referralCode;

    public Member() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRegisteredIndonesia() {
        return registeredIndonesia;
    }

    public void setRegisteredIndonesia(boolean registeredIndonesia) {
        this.registeredIndonesia = registeredIndonesia;
    }

    public boolean isRegisteredMalaysia() {
        return registeredMalaysia;
    }

    public void setRegisteredMalaysia(boolean registeredMalaysia) {
        this.registeredMalaysia = registeredMalaysia;
    }

    public boolean isRegisteredSingapore() {
        return registeredSingapore;
    }

    public void setRegisteredSingapore(boolean registeredSingapore) {
        this.registeredSingapore = registeredSingapore;
    }

    public String getLocalePreference() {
        return localePreference;
    }

    public void setLocalePreference(String localePreference) {
        this.localePreference = localePreference;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(String lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }


}
