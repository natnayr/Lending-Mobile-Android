package com.crowdo.p2pmobile.activity;

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


import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.fragment.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 2/1/17.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG_SETTINGS_FRAGMENT = "SettingsFragment";
    public static final String LOG_TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)  Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.activity_settings_action_bar_label));
        SettingsActivity.this.setTitle(getString(R.string.activity_settings_action_bar_label));

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
        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
    }



    private boolean toBackStackOrParent(){
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //If no backstack then navigate to logical main list view
            NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
    }
}
