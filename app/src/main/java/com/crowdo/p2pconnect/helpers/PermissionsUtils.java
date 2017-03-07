package com.crowdo.p2pconnect.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;

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

    public static boolean checkDrawOverlayPermission(final Context context, String msg) {
        //Check Overlays for Marshmellow version and after
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (!Settings.canDrawOverlays(context)) {
            new AlertDialog.Builder(context)
                    .setMessage(msg)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + context.getPackageName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ((Activity) context).startActivityForResult(intent, ConstantVariables.REQUEST_CODE_PERMISSIONS_OVERLAY);
                        }
                    }).create().show();
            return false;
        } else {
            return true;
        }
    }
}
