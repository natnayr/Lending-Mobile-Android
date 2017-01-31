package com.crowdo.p2pmobile.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by cwdsg05 on 31/1/17.
 */

public class SnackBarUtil {

    public static Snackbar snackBarCreate(View view, String msg, int colorText) {

        Snackbar snackbar = Snackbar.make(view,
                msg, Snackbar.LENGTH_SHORT);
        TextView snackTextView = (TextView) snackbar.getView()
                .findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(colorText);
        return snackbar;
    }
}
