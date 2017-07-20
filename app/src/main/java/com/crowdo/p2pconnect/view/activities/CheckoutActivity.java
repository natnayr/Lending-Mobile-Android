package com.crowdo.p2pconnect.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.commons.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.view.fragments.CheckoutSummaryFragment;

/**
 * Created by cwdsg05 on 22/5/17.
 */

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        CheckoutSummaryFragment checkoutSummaryFragment = new CheckoutSummaryFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.checkout_summary_content, checkoutSummaryFragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this);
    }
}
