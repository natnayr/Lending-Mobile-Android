package com.crowdo.p2pconnect.commons;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cwdsg05 on 14/6/17.
 */

public class NetworkConnectionChecks {

    private final static String LOG_TAG = NetworkConnectionChecks.class.getSimpleName();

    public static void isOnline(final Activity activity, final boolean showLogOut) {

        final View thisView = activity.findViewById(android.R.id.content);

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                //checking device
                ConnectivityManager cm =
                        (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                boolean checkNetwork = netInfo != null && netInfo.isConnectedOrConnecting();

                //try connecting to server (google dns)
                boolean checkConnectToServer = true;
                try {
                    URL url = new URL(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_DOCS);
                    int timeoutMs = 10 * 1000;
                    HttpURLConnection httpUrlc = (HttpURLConnection) url.openConnection();
                    httpUrlc.setConnectTimeout(timeoutMs);
                    httpUrlc.connect();
                    if (httpUrlc.getResponseCode() == 200) {
                        checkConnectToServer = true;
                        Log.d(LOG_TAG, "APP Server Connection Success");
                    } else {
                        checkConnectToServer = false;
                        Log.d(LOG_TAG, "APP Server Connection Failed");
                    }
                } catch (IOException ioe) {
                    checkConnectToServer = false;
                }

                return checkNetwork && checkConnectToServer;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    String badConnectionMsg = activity.getString(R.string.server_connection_poor);
                    Snackbar snackbar = SnackBarUtil.snackBarForErrorCreate(thisView,
                            badConnectionMsg, Snackbar.LENGTH_INDEFINITE);
                    if(showLogOut) {
                        snackbar.setAction("Log Out", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                AuthAccountUtils.actionLogout(activity);
                            }
                        });
                    }
                    snackbar.show();
                }
            }
        }.execute();
    }
}
