package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 23/8/17.
 */

public class SocialRequest {

    @Json(name = "provider")
    public String provider;

    @Json(name = "uid")
    public String uid;

    @Json(name = "access_token")
    public String accessToken;

    @Json(name = "locale_preference")
    public String localePreference;

    @Json(name = "device_id")
    public String deviceId;

    public SocialRequest() {
    }

    public SocialRequest(String provider, String uid, String accessToken,
                         String localePreference, String deviceId) {
        super();
        this.provider = provider;
        this.uid = uid;
        this.accessToken = accessToken;
        this.localePreference = localePreference;
        this.deviceId = deviceId;
    }
}
