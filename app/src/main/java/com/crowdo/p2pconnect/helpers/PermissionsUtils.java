package com.crowdo.p2pconnect.helpers;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Created by cwdsg05 on 24/2/17.
 */

public class PermissionsUtils {

    public static boolean checkPermissionOnly(Context context, String permissionToCheck){
        return ContextCompat.checkSelfPermission(context, permissionToCheck)
                == PermissionChecker.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionAndRequestActivity(Context context, String permissionToCheck, int responseCode){
        final String permission = permissionToCheck;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if(permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, responseCode);
            return false;
        }
        return true;
    }

    public static void checkPermisssionAndRequestStorageFragment(final Context context, final Fragment fragment, String msg, String okay, String cancel){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            fragment.requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

        }
    }
}
