package com.crowdo.p2pconnect.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.view.fragments.WelcomeFragment;

/**
 * Created by ryan on 19/10/16.
 */
public class LaunchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.launch_content, new WelcomeFragment(),
                        WelcomeFragment.TAG_WELCOME_FRAGMENT)
                .commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
