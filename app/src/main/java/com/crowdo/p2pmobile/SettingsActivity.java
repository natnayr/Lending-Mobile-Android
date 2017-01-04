package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 2/1/17.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG_SETTINGS_FRAGMENT = "SettingsFragment";
    public static final String LOG_TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)  Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Fragment settingsFragment = getFragmentManager().findFragmentByTag(TAG_SETTINGS_FRAGMENT);
        if(settingsFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new SettingsFragment(), TAG_SETTINGS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        Log.d(LOG_TAG, "TEST: onBackPressed count = " + count);
        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
    }



    private boolean toBackStackOrParent(){
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            Log.d(LOG_TAG, "TEST: TaskBuilder Option");
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //If no backstack then navigate to logical main list view
            Log.d(LOG_TAG, "TEST: Navigate Up option");
            NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
    }
}
