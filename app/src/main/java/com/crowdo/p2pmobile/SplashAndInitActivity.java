package com.crowdo.p2pmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by ryan on 19/10/16.
 */

public class SplashAndInitActivity extends AppCompatActivity {

    private static final String LOG_TAG = SplashAndInitActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);

        Intent intent = new Intent(this, LoanListActivity.class);
        startActivity(intent);
        finish();
    }
}
