package com.crowdo.p2pconnect.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

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

    public static Snackbar snackBarForErrorCreate(View view, String msg, int snackBarDuration) throws IllegalStateException{

        return Snacky.builder()
                .setView(view)
                .setText(msg)
                .setDuration(snackBarDuration)
                .error();
    }
}