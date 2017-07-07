package com.crowdo.p2pconnect.helpers;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.crowdo.p2pconnect.R;
import com.mikepenz.iconics.IconicsDrawable;

import de.mateware.snacky.Snacky;

/**
 * Created by cwdsg05 on 31/1/17.
 */

public class SnackBarUtil {

    public static Snackbar snackBarForSuccessCreate(View view, String msg, int snackBarDuration) {

        return Snacky.builder()
                .setView(view)
                .setText(msg)
                .setDuration(snackBarDuration)
                .success();
    }

    public static Snackbar snackBarForInfoCreate(View view, String msg, int snackBarDuration) {

        return Snacky.builder()
                .setView(view)
                .setText(msg)
                .setDuration(snackBarDuration)
                .info();
    }

    public static Snackbar snackBarForWarningCreate(View view, String msg, int snackBarDuration) {

        return Snacky.builder()
                .setView(view)
                .setText(msg)
                .setDuration(snackBarDuration)
                .warning();
    }

    public static Snackbar snackBarForErrorCreate(View view, String msg, int snackBarDuration) {

        return Snacky.builder()
                .setView(view)
                .setText(msg)
                .setDuration(snackBarDuration)
                .error();
    }
}