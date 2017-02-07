package com.crowdo.p2pmobile.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.fragments.LoanListFragment;
import com.crowdo.p2pmobile.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 3/2/17.
 */

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.main_nav_view) NavigationView mNavDrawer;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActionBarDrawerToggle drawerToggle;
    private View navHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        //load first fragment
        mToolbar.setTitle(getString(R.string.activity_loan_list_action_bar_label));
        MainActivity.this.setTitle(getString(R.string.activity_loan_list_action_bar_label));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, new LoanListFragment())
                .commit();

        setupDrawerContent(mNavDrawer);
        drawerToggle = setUpDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        navHeader = mNavDrawer.getHeaderView(0); //reference point
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectDrawerItem(item);
                    return true;
                }
            }
        );
    }

    private void selectDrawerItem(MenuItem item){
        Fragment fragment = null;
        Class fragmentClass;

        switch (item.getItemId()){
            case R.id.nav_drawer_loan_list:
                fragmentClass = LoanListFragment.class;
                break;
            case R.id.nav_drawer_settings:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = LoanListFragment.class;
        }

        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();

        // Highlight the selected item
        item.setChecked(true);
        // Set Action Bar
        setTitle(item.getTitle());
        // Close the Navi Drawer
        mDrawer.closeDrawers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // For handling of events
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.END);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setUpDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
    }
}
