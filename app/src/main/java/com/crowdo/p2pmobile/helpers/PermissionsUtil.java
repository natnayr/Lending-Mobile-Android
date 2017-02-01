package com.crowdo.p2pmobile.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Created by cwdsg05 on 1/2/17.
 */

public class PermissionsUtil {

    public static boolean checkPermission(Context context, String permissionToCheck){
        final String permission = permissionToCheck;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if(permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 0);
            return false;
        }
        return true;
    }
}
