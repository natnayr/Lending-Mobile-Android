package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 27/3/17.
 */

public class RegisterRequest {

    @Json(name = "member")
    public RegisterMember member;
    @Json(name = "device_id")
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

    private static class RegisterMember {

        @Json(name = "name")
        public String name;
        @Json(name = "email")
        public String email;
        @Json(name = "password")
        public String password;
        @Json(name = "password_confirmation")
        public String passwordConfirmation;
        @Json(name = "locale_preference")
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
