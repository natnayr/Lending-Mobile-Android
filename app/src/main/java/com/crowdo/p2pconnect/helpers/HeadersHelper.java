package com.crowdo.p2pconnect.helpers;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cwdsg05 on 10/4/17.
 */

public class HeadersHelper {

    public static Map<String, String> setUpRESTfulHeader(@Nullable String authToken){
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");

        if(authToken != null){
            map.put("Authorization", authToken);
        }
        return map;
    }
}
