package com.crowdo.p2pconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 21/3/17.
 */

public class Member {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("status")
    @Expose
    private long status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    class User {

        @SerializedName("id")
        @Expose
        private long id;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("registered_indonesia")
        @Expose
        private boolean registeredIndonesia;

        @SerializedName("registered_malaysia")
        @Expose
        private boolean registeredMalaysia;

        @SerializedName("registered_singapore")
        @Expose
        private boolean registeredSingapore;

        @SerializedName("locale_preference")
        @Expose
        private String localePreference;

        @SerializedName("created_at")
        @Expose
        private String createdAt;

        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("last_activity_at")
        @Expose
        private String lastActivityAt;

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
}
