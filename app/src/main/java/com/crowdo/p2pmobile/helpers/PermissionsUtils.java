package com.crowdo.p2pmobile.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;

/**
 * Created by cwdsg05 on 24/2/17.
 */

public class PermissionsUtils {

    public static boolean checkPermission(Context context, String permissionToCheck){
        final String permission = permissionToCheck;
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if(permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 0);
            return false;
        }
        return true;
    }

    public static void checkPermisssionAndRequest(final Activity activity, String msg, String okay, String cancel){
        if(ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(activity)
                        .setMessage(msg)
                        .setPositiveButton(okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE},
                                        ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                                Toast toast = Toast.makeText(activity, activity.getString(
                                        R.string.permissions_write_request_continue),
                                        Toast.LENGTH_SHORT);

                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();
                            }
                        })
                        .setNegativeButton(cancel, null)
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
}
