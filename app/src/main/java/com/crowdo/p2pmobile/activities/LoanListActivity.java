package com.crowdo.p2pmobile.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.fragments.LoanListFragment;
import com.f2prateek.dart.Dart;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanListActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoanListActivity.class.getSimpleName();
    public static final String TAG_LOAN_LIST_FRAGMENT = "LoanListFragment";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //mToolbar view
        setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setTitle(getString(R.string.activity_loan_list_action_bar_label));
        LoanListActivity.this.setTitle(getString(R.string.activity_loan_list_action_bar_label));

        //inject intent settings
        Dart.inject(this);

        Fragment loanListFragment = getFragmentManager().findFragmentByTag(TAG_LOAN_LIST_FRAGMENT);
        if(loanListFragment == null){
            getFragmentManager().beginTransaction()
                    .add(new LoanListFragment(), TAG_LOAN_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
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
