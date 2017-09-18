package com.crowdo.p2pconnect.support;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by cwdsg05 on 18/8/17.
 */

public class UpdateChecking{

    private static final String LOG_TAG = UpdateChecking.class.getSimpleName();
    private String currentVersion, latestVersion;
    private MaterialDialog dialog;
    private Activity mActivity;

    public void getCurrentVersion(Activity activity){
        PackageManager pm = activity.getPackageManager();
        PackageInfo packageInfo = null;
        this.mActivity = activity;
        try{
            packageInfo = pm.getPackageInfo(activity.getPackageName(), 0);
        }catch (PackageManager.NameNotFoundException e){
            Log.d(LOG_TAG, "ERROR " + e.getMessage(), e);
            e.printStackTrace();
        }

        currentVersion = packageInfo.versionName;
        Log.d(LOG_TAG, "APP appPackageName showUpdateDialog: " + activity.getPackageName());

        new GetLatestVersion().execute();
    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(ConstantVariables.PLAYSTORE_APP_URL).get();
                latestVersion = document.getElementsByAttributeValue("itemprop","softwareVersion")
                        .first().text();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion != null){
                if(!currentVersion.equalsIgnoreCase(latestVersion)){
                    Log.d(LOG_TAG, "APP: currentVersion=" + currentVersion +
                            " latestVersion:" + latestVersion );
                    if(!mActivity.isFinishing()){
                        showUpdateDialog();
                    }
                }
            }

            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog(){
        final String appPackageName = mActivity.getPackageName();

        dialog = new MaterialDialog.Builder(mActivity)
                .title(R.string.prompt_download_latest_version_title)
                .content(R.string.prompt_download_latest_version_message)
                .positiveText(R.string.download_label)
                .negativeText(R.string.cancel_label)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();

        dialog.show();

    }
}
