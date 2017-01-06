package com.crowdo.p2pmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ryan on 19/10/16.
 */

public class SplashAndInitActivity extends AppCompatActivity {

    private static final String LOG_TAG = SplashAndInitActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, LoanListActivity.class);
        startActivity(intent);
        finish();
    }
}
