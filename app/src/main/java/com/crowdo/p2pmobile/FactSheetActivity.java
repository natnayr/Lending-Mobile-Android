package com.crowdo.p2pmobile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by cwdsg05 on 14/12/16.
 */

public class FactSheetActivity extends Activity {

    @InjectExtra public int id;
    private static final String LOG_TAG = FactSheetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factsheet);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Dart.inject(this);

        View rootView = getLayoutInflater().inflate(R.layout.activity_factsheet, null);

        Log.d(LOG_TAG, "TEST: in factsheetactivity id:" + id);
        final FactSheetViewHolder viewHolder = new FactSheetViewHolder(rootView);
        viewHolder.initView(this, this.id);
    }

    static class FactSheetViewHolder {

        @BindView(R.id.loan_factsheet_test) TextView mTestTextView;

        public FactSheetViewHolder(View view){ ButterKnife.bind(this, view); }

        public void initView(final Context context, final int holderId) {
            Log.d(LOG_TAG, "TEST: factsheetviewholder = " + holderId);
            mTestTextView.setText(holderId);
        }
    }

}
