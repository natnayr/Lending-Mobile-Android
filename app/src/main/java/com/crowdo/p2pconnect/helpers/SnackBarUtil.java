package com.crowdo.p2pconnect.helpers;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;

/**
 * Created by cwdsg05 on 31/1/17.
 */

public class SnackBarUtil {

    public static Snackbar snackBarCreate(View view, String msg, int colorText) {
        return snackBarCreate(view, msg, colorText, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar snackBarCreate(View view, String msg, int colorText, int snackBarDuration) {
        Snackbar snackbar = Snackbar
                .make(view, msg, snackBarDuration)
                .setActionTextColor(getIconTextColor(view));

        TextView tv = (TextView) snackbar.getView()
                .findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(colorText);
        return snackbar;
    }

    public static Snackbar snackBarForInfoCreate(View view, String msg, int snackBarDuration){

        Snackbar snackbar = snackBarCreate(view, msg, getIconTextColor(view), snackBarDuration);

        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.color_accent);
        return snackbar;
    }

    public static Snackbar snackBarForWarningCreate(View view, String msg, int snackBarDuration){

        Snackbar snackbar = snackBarCreate(view, msg, getIconTextColor(view), snackBarDuration);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.color_amber_500);

        return snackbar;
    }

    public static Snackbar snackBarForErrorCreate(View view, String msg, int snackBarDuration){

        Snackbar snackbar = snackBarCreate(view, msg, getIconTextColor(view), snackBarDuration);
        View sbView = snackbar.getView();
        sbView.setBackgroundResource(R.color.color_primary_700);

        return snackbar;
    }

    private static int getIconTextColor(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return view.getResources().getColor(R.color.color_icons_text, null);
        }else{
            return view.getResources().getColor(R.color.color_icons_text);
        }
    }
}
