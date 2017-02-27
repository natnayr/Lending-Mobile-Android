package com.crowdo.p2pconnect.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by cwdsg05 on 31/1/17.
 */

public class SnackBarUtil {

    public static Snackbar snackBarCreate(View view, String msg, int colorText) {
        return snackBarCreate(view, msg, colorText, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar snackBarCreate(View view, String msg, int colorText, int snackBarLength) {
        Snackbar snackbar = Snackbar.make(view, msg, snackBarLength);

        TextView snackTextView = (TextView) snackbar.getView()
                .findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(colorText);
        return snackbar;
    }
}
