package com.crowdo.p2pconnect.helpers;

import android.os.Bundle;

/**
 * Created by cwdsg05 on 5/4/17.
 */

public class BundleHelper {

    public static String bundleToString(Bundle bundle){
        StringBuilder toAppend = new StringBuilder();
        for (String key : bundle.keySet()) {
            toAppend.append(key + " : " + bundle.get(key) + "\n");
        }
        return toAppend.toString();
    }
}
