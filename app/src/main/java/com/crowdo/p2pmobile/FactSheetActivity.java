package com.crowdo.p2pmobile;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 14/12/16.
 */

public class FactSheetActivity extends Activity {

    @InjectExtra public int id;
    @BindView(R.id.loan_factsheet_test) TextView testTextView;

    private static final String LOG_TAG = FactSheetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factsheet);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);
        ButterKnife.bind(this);

        Log.d(LOG_TAG, "TEST: in factsheetactivity id:" + id);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(id >= 0) {
            testTextView.setText(id);
        }
    }

}
