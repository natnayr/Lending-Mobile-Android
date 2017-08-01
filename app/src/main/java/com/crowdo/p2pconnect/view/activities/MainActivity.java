package com.crowdo.p2pconnect.view.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.support.NetworkConnectionChecks;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.TypefaceUtils;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.oauth.AuthHelper;
import com.crowdo.p2pconnect.view.fragments.LearningCenterFragment;
import com.crowdo.p2pconnect.view.fragments.LoanListFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
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
    @BindString(R.string.language_english_label) String mLanguageEnglish;
    @BindString(R.string.language_bahasa_label) String mLanguageBahasa;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Drawer navDrawer;

    private static final int DRAWER_SELECT_LOAN_LIST_FRAGMENT = 100;
    private static final int DRAWER_SELECT_LEARNING_CENTER_FRAGMENT = 101;
    private static final int DRAWER_SELECT_ACCOUNT_TOP_UP = 102;
    private static final int DRAWER_SELECT_ACCOUNT_WITHDRAW = 103;
    private static final int DRAWER_SELECT_LANGUAGE_CHANGE = 104;
    private static final int DRAWER_SELECT_LANGUAGE_EN = 500;
    private static final int DRAWER_SELECT_LANGUAGE_IN = 501;
    private static final int DRAWER_SELECT_TOP_UP_WALLET = 105;
    private static final int DRAWER_SELECT_APPLY_AS_INVESTOR = 106;
    private static final int DRAWER_SELECT_LOGOUT = 107;
    private static final int DRAWER_SELECT_SHOPPING_CART = 999;


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.toolbar_title_loan_list));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mToolbar.setElevation(0); //only 5.0 onwards
        }
        setSupportActionBar(mToolbar);

        //finally load fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, new LoanListFragment())
                .commit();

        navDrawer = buildNavigationDrawer()
                .withSavedInstance(savedInstanceState)
                .build();

        navDrawer.setSelection(DRAWER_SELECT_LOAN_LIST_FRAGMENT);

        TextView mNavDrawerAppLogo = (TextView) navDrawer.getHeader().findViewById(R.id.nav_header_app_title);
        //set typeface
        mNavDrawerAppLogo.setTypeface(TypefaceUtils.getNothingYouCouldDoTypeFace(this));

        //HockeyApp user metrics
//        MetricsManager.register(getApplication());
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
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_LOAN_LIST_FRAGMENT)
                                .withName(R.string.toolbar_title_loan_list).withIcon(CommunityMaterial.Icon.cmd_gavel)
                                .withSetSelected(true).withSelectedTextColorRes(R.color.color_primary_700)
                                .withSelectedIconColorRes(R.color.color_primary_700),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_LEARNING_CENTER_FRAGMENT)
                                .withName(R.string.toolbar_title_learning_center).withIcon(CommunityMaterial.Icon.cmd_book_open_page_variant)
                                .withSelectedTextColorRes(R.color.color_primary_700)
                                .withSelectedIconColorRes(R.color.color_primary_700),
                        new SectionDrawerItem().withName("Account"),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_ACCOUNT_TOP_UP)
                                .withName("Top Up")
                                .withIcon(CommunityMaterial.Icon.cmd_wallet)
                                .withSelectable(false),
//                        new PrimaryDrawerItem().withIdentifier(DRAWER_SELECT_ACCOUNT_WITHDRAW)
//                                .withName("Withdraw")
//                                .withIcon(CommunityMaterial.Icon.cmd_square_inc_cash)
//                                .withSelectable(false),
                        new SectionDrawerItem().withName(R.string.navmenu_label_preferences),
                        new ExpandableDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_CHANGE)
                                .withName(R.string.navmenu_label_language)
                                .withIcon(CommunityMaterial.Icon.cmd_translate)
                                .withSelectable(false)
                                .withTextColorRes(R.color.color_secondary_text)
                                .withSubItems(
                                    new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_EN)
                                            .withName(R.string.language_english_label).withLevel(2)
                                            .withSelectable(false),
                                    new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_LANGUAGE_IN)
                                            .withName(R.string.language_bahasa_label).withLevel(2)
                                            .withSelectable(false)
                                ),
                        new SectionDrawerItem().withName(R.string.navmenu_label_actions),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_TOP_UP_WALLET)
                                .withName(R.string.toolbar_title_top_up_wallet)
                                .withIcon(CommunityMaterial.Icon.cmd_wallet)
                                .withSelectable(false),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_APPLY_AS_INVESTOR)
                                .withName(R.string.toolbar_title_apply_investor)
                                .withIcon(CommunityMaterial.Icon.cmd_account_star)
                                .withSelectable(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SELECT_LOGOUT)
                                .withName(R.string.toolbar_title_logout)
                                .withIcon(CommunityMaterial.Icon.cmd_logout_variant)
                                .withSelectable(false)
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

                                case DRAWER_SELECT_LEARNING_CENTER_FRAGMENT:
                                    fragmentClass = LearningCenterFragment.class;
                                    mToolbar.setTitle(R.string.toolbar_title_learning_center);
                                    break;

                                case DRAWER_SELECT_ACCOUNT_TOP_UP:
                                    Intent intent = new Intent(MainActivity.this, TopUpActivity.class);
                                    startActivity(intent);
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

                                case DRAWER_SELECT_LOGOUT:
                                    AuthHelper.invalidateTokenAndMakeCall(MainActivity.this,
                                            new CallBackUtil<MemberInfoResponse>() {
                                        @Override
                                        public void eventCallBack(MemberInfoResponse t1) {
                                            //do nothing, login should pop up
                                        }
                                    });
                                    return true;
                                default:
                                    return false; //default close
                            }

                            if(webCall == true && action != null) {
                                final String locale = LocaleHelper.getLanguage(MainActivity.this);
                                String webViewUrl = APIServices.P2P_BASE_URL +
                                        "mobile2/" + action +
                                        "?lang=" + locale +
                                        "&device_id=" +
                                        ConstantVariables.getUniqueAndroidID(MainActivity.this);

                                Intent intent = Henson.with(MainActivity.this)
                                        .gotoWebViewActivity()
                                        .mUrl(webViewUrl)
                                        .build();
                                startActivity(intent);
                                Log.d(LOG_TAG, "APP activity_webview launched to " + webViewUrl);
                                return true;
                            }

                            if (fragmentClass != null) {
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
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

        super.onBackPressed();
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

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();

        //check network and invalidateTokenAndMakeCall if needed
        NetworkConnectionChecks.isOnline(this);
    }

    private void checkForCrashes() {
//        CrashManager.register(this, new CrashManagerListener() {
//            @Override
//            public boolean shouldAutoUploadCrashes() {
//                return true;
//            }
//        });
    }


}
