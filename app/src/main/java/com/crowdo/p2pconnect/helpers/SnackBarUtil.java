package com.crowdo.p2pconnect.helpers;

import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by cwdsg05 on 31/1/17.
 */

public class SnackBarUtil {

    public static Snackbar snackBarCreate(View view, String msg, int colorText) {
        return snackBarCreate(view, msg, colorText, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar snackBarCreate(View view, String msg, int colorText, int snackBarDuration) {
        Snackbar snackbar = Snackbar.make(view, msg, snackBarDuration);

        TextView tv = (TextView) snackbar.getView()
                .findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(colorText);
        return snackbar;
    }

    public static Snackbar snackBarForAuthCreate(View view, String msg, int colorText,
                                                 int snackBarDuration, int colorBackground){

        Snackbar snackbar = snackBarCreate(view, msg, colorText, snackBarDuration);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(colorBackground);
        TextView tv = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setGravity(Gravity.CENTER);

        return snackbar;
    }
}
