package com.crowdo.p2pconnect.view.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.TypefaceUtils;
import com.crowdo.p2pconnect.view.fragments.LearningCenterFragment;
import com.crowdo.p2pconnect.view.fragments.LoanListFragment;
import com.crowdo.p2pconnect.view.fragments.UserSettingsFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

    private static final int DRAWER_SELECT_LOAN_LIST_FRAGMENT = 100;
    private static final int DRAWER_SELECT_USER_SETTINGS_FRAGMENT = 101;
    private static final int DRAWER_SELECT_LEARNING_CENTER_FRAGMENT = 102;
    private static final int DRAWER_SELECT_LANGUAGE_CHANGE = 103;
    private static final int DRAWER_SELECT_LANGUAGE_EN = 500;
    private static final int DRAWER_SELECT_LANGUAGE_IN = 501;
    private static final int DRAWER_SELECT_TOP_UP_WALLET = 104;
    private static final int DRAWER_SELECT_APPLY_AS_INVESTOR = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PermissionsUtils.checkDrawOverlayPermission(this, mOverlayPermissionRequest);

        mToolbar.setTitle(getString(R.string.toolbar_title_loan_list));
        setSupportActionBar(mToolbar);

        //finally load fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, new LoanListFragment())
                .commit();

        navDrawer = buildNavigationDrawer()
                .withSavedInstance(savedInstanceState)
                .build();

        navDrawer.setSelection(100);

        TextView mNavDrawerAppLogo = (TextView) navDrawer.getHeader().findViewById(R.id.nav_header_app_title);
        //set typeface
        mNavDrawerAppLogo.setTypeface(TypefaceUtils.getNothingYouCouldDoTypeFace(this));
    }

    private DrawerBuilder buildNavigationDrawer(){
        return new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withToolbar(mToolbar)
                .withHeader(R.layout.nav_header)
                .withDrawerWidthDp(280)
                .withFullscreen(true)
                .withCloseOnClick(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_LOAN_LIST_FRAGMENT).withName(R.string.toolbar_title_loan_list).withIcon(CommunityMaterial.Icon.cmd_gavel)
                                .withSetSelected(true).withSelectedTextColorRes(R.color.color_primary_700).withSelectedIconColorRes(R.color.color_primary_700),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_USER_SETTINGS_FRAGMENT).withName(R.string.toolbar_title_user_settings).withIcon(CommunityMaterial.Icon.cmd_settings)
                                .withSelectedTextColorRes(R.color.color_primary_700).withSelectedIconColorRes(R.color.color_primary_700),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_LEARNING_CENTER_FRAGMENT).withName(R.string.toolbar_title_learning_center).withIcon(CommunityMaterial.Icon.cmd_book_open_page_variant)
                                .withSelectedTextColorRes(R.color.color_primary_700).withSelectedIconColorRes(R.color.color_primary_700),
                        new SectionDrawerItem().withName(R.string.navmenu_label_preferences),
                        new ExpandableDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_CHANGE).withName(R.string.navmenu_label_language).withIcon(CommunityMaterial.Icon.cmd_translate)
                                .withSelectable(false).withSubItems(
                                    new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_EN).withName(R.string.language_english_label).withLevel(2).withSelectable(false),
                                    new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_IN).withName(R.string.language_bahasa_label).withLevel(2).withSelectable(false)
                                ),
                        new SectionDrawerItem().withName(R.string.navmenu_label_extras),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_TOP_UP_WALLET).withName(R.string.toolbar_title_top_up_wallet)
                                .withSelectable(false).withIcon(CommunityMaterial.Icon.cmd_wallet),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_APPLY_AS_INVESTOR).withName(R.string.toolbar_title_apply_investor)
                                .withSelectable(false).withIcon(CommunityMaterial.Icon.cmd_account_star_variant)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem != null) {
                            Fragment fragment = null;
                            Class fragmentClass = null;
                            String action = null;
                            boolean webCall = false;

                            switch ((int) drawerItem.getIdentifier()) {
                                case DRAWER_SELECT_LOAN_LIST_FRAGMENT:
                                    fragmentClass = LoanListFragment.class;
                                    mToolbar.setTitle(R.string.toolbar_title_loan_list);
                                    break;
                                case DRAWER_SELECT_USER_SETTINGS_FRAGMENT:
                                    fragmentClass = UserSettingsFragment.class;
                                    mToolbar.setTitle(R.string.toolbar_title_user_settings);
                                    break;
                                case DRAWER_SELECT_LEARNING_CENTER_FRAGMENT:
                                    fragmentClass = LearningCenterFragment.class;
                                    mToolbar.setTitle(R.string.toolbar_title_learning_center);
                                    break;
                                case DRAWER_SELECT_LANGUAGE_EN:
                                    LocaleHelper.setLocale(MainActivity.this, ConstantVariables.APP_LANG_EN);
                                    MainActivity.this.recreate();
                                    return true;

                                case DRAWER_SELECT_LANGUAGE_IN:
                                    LocaleHelper.setLocale(MainActivity.this, ConstantVariables.APP_LANG_ID);
                                    MainActivity.this.recreate();
                                    return true;

                                case DRAWER_SELECT_TOP_UP_WALLET:
                                    action = "top_up";
                                    webCall = true;
                                    break;

                                case DRAWER_SELECT_APPLY_AS_INVESTOR:
                                    action = "register_as_investor";
                                    webCall = true;
                                    break;
                                default:
                                    return false; //default close
                            }

                            if(webCall == true && action != null) {
                                final String locale = LocaleHelper.getLanguage(MainActivity.this);
                                String webViewUrl = APIServices.API_BASE_URL +
                                        "mobile/" + action +
                                        "?lang=" + locale;

                                Intent intent = Henson.with(MainActivity.this)
                                        .gotoWebViewActivity()
                                        .mUrl(webViewUrl)
                                        .build();
                                startActivity(intent);
                                Log.d(LOG_TAG, "APP: webview launched to " + webViewUrl);
                                return true;
                            }

                            if (fragmentClass != null) {
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                    e.printStackTrace();
                                }

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_content, fragment)
                                        .commit();
                                return false;
                            }
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

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
            Log.d(LOG_TAG, "APP: TaskStackBuilder.create(this) has been called");
        } else {
            //If no backstack then navigate to logical main list view
            NavUtils.navigateUpTo(this, upIntent);
            Log.d(LOG_TAG, "APP: NavUtils.navigateUpTo(this, upIntent) has been called");
        }
        return true;
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
