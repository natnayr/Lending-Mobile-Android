package com.crowdo.p2pconnect.view.activities;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.TypefaceUtils;
import com.crowdo.p2pconnect.view.fragments.LearningCenterFragment;
import com.crowdo.p2pconnect.view.fragments.LoanListFragment;
import com.crowdo.p2pconnect.view.fragments.SettingsFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 3/2/17.
 */

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindString(R.string.pre_exit_question) String mPreExitQuestion;
    @BindString(R.string.permission_overlay_permission_request) String mOverlayPermissionRequest;
    @BindString(R.string.language_english_label) String mLanguageEnglish;
    @BindString(R.string.language_bahasa_label) String mLanguageBahasa;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Drawer navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkDrawOverlayPermission();

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.toolbar_title_loan_list));
        MainActivity.this.setTitle(getString(R.string.toolbar_title_loan_list));

        //finally load fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, new LoanListFragment())
                .commit();

        navDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHeader(R.layout.nav_header)
                .withDrawerWidthDp(280)
                .build();

        TextView mNavDrawerAppLogo = (TextView) navDrawer.getHeader().findViewById(R.id.nav_header_app_title);
        mNavDrawerAppLogo.setTypeface(TypefaceUtils.getQuickSandTypeFace(this));
    }


//    private void selectDrawerItem(MenuItem item){
//        Fragment fragment = null;
//        Class fragmentClass;
//
//        switch (item.getItemId()){
//            case R.id.nav_drawer_loan_list:
//                fragmentClass = LoanListFragment.class;
//                break;
//            case R.id.nav_drawer_settings:
//                fragmentClass = SettingsFragment.class;
//                break;
//            case R.id.nav_drawer_learning_center:
//                fragmentClass = LearningCenterFragment.class;
//                break;
//            default:
//                fragmentClass = LoanListFragment.class;
//        }
//
//        try{
//            fragment = (Fragment) fragmentClass.newInstance();
//        }catch (Exception e){
//            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
//            e.printStackTrace();
//        }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(mPreExitQuestion)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public void onSupportActionModeStarted(@NonNull android.support.v7.view.ActionMode mode) {
        //cause style.xml windowActionBarOverlay doesnt work
        mToolbar.setVisibility(View.GONE);
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(@NonNull android.support.v7.view.ActionMode mode) {
        //cause style.xml windowActionBarOverlay doesnt work
        super.onSupportActionModeFinished(mode);
        mToolbar.animate()
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToolbar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    public boolean checkDrawOverlayPermission() {
        //Check Overlays for Marshmellow version and after
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (!Settings.canDrawOverlays(this)) {
            new AlertDialog.Builder(this)
                    .setMessage(mOverlayPermissionRequest)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivityForResult(intent, ConstantVariables.REQUEST_CODE_PERMISSIONS_OVERLAY);
                        }
                    }).create().show();
            return false;
        } else {
            return true;
        }
    }
}
