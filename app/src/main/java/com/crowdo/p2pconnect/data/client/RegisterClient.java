package com.crowdo.p2pconnect.data.client;

/**
 * Created by cwdsg05 on 22/3/17.
 */

public class RegisterClient {

    public class Input {
        String email;
        String password;
        String deviceId;

        public Input(String email, String password, String deviceId) {
            this.email = email;
            this.password = password;
            this.deviceId = deviceId;
        }
    }
}
