package com.crowdo.p2pconnect.data.request_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 27/3/17.
 */

public class RegisterRequest {

    @SerializedName("member")
    @Expose
    public RegisterMember member;

    @SerializedName("device_id")
    @Expose
    public String deviceId;


    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterRequest(){
    }

    public RegisterRequest(String name, String email, String password, String passwordConfirmation, String localePreference, String deviceId){
        super();
        this.member = new RegisterMember(name, email, password, passwordConfirmation, localePreference);
        this.deviceId = deviceId;
    }

    private class RegisterMember {

        public String name;

        public String email;

        @SerializedName("password")
        @Expose
        public String password;

        @SerializedName("password_confirmation")
        @Expose
        public String passwordConfirmation;

        @SerializedName("locale_preference")
        @Expose
        public String localePreference;

        /**
         * No args constructor for use in serialization
         *
         */
        public RegisterMember() {
        }

        public RegisterMember(String name, String email, String password, String passwordConfirmation, String localePreference) {
            super();
            this.name = name;
            this.email = email;
            this.password = password;
            this.passwordConfirmation = passwordConfirmation;
            this.localePreference = localePreference;
        }
    }
}
