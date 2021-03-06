package com.crowdo.p2pconnect.view.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.support.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.view.fragments.TopUpHistoryFragment;
import com.crowdo.p2pconnect.view.fragments.TopUpSubmitFragment;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/7/17.
 */

public class TopUpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_custom_header) RelativeLayout mTopUpHeader;
    @BindView(R.id.toolbar_custom_title) LinearLayout mTopUpTitleBtn;
    @BindView(R.id.toolbar_custom_title_label) TextView mTopUpTitleLabel;
    @BindView(R.id.toolbar_custom_title_icon) ImageView mTopUpTitleExpandIcon;
    @BindView(R.id.toolbar_custom_left_btn) LinearLayout mTopUpCloseBtn;
    @BindView(R.id.toolbar_custom_left_icon) ImageView mTopUpCloseIcon;
    @BindView(R.id.toolbar_custom_right_btn) LinearLayout mTopUpRefreshBtn;
    @BindView(R.id.toolbar_custom_right_icon) ImageView mTopUpRefreshIcon;
    @BindView(R.id.top_up_balance_expandable) public ExpandableLayout mTopUpBalanceExpandableLayout;
    @BindView(R.id.top_up_balance_amount) TextView mTopUpBalanceAmountLabel;
    @BindView(R.id.top_up_balance_description) TextView mTopUpBalanceDescriptionLabel;

    @BindView(R.id.top_up_viewpager) ViewPager mTopUpViewPager;
    @BindView(R.id.top_up_tablayout) TabLayout mTopUpTabLayout;

    @BindString(R.string.top_up_title_label) String mTopUpTitleText;
    @BindString(R.string.top_up_balance_description_start) String mTopUpBalanceDescriptionStartText;
    @BindString(R.string.top_up_tab_title_one) String mTopUpTabOneTitleText;
    @BindString(R.string.top_up_tab_title_two) String mTopUpTabTwoTitleText;


    @BindColor(R.color.color_grey_blue_800) int mColorGreyBlue800;
    @BindColor(R.color.color_icons_text) int mColorWhite;

    private TopUpPagerAdapter pagerAdapter;

    private final static String LOG_TAG = TopUpActivity.class.getSimpleName();
    private TopUpSubmitFragment submitFragment;
    private TopUpHistoryFragment historyFragment;
    private Fragment[] PAGES;
    private String[] PAGE_TITLES;


    public TopUpActivity(){
        submitFragment = new TopUpSubmitFragment();
        historyFragment = new TopUpHistoryFragment();
        PAGES = new Fragment[]{
                submitFragment, historyFragment
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        ButterKnife.bind(this);

        PAGE_TITLES = new String[]{mTopUpTabOneTitleText, mTopUpTabTwoTitleText};

        initView();

        getMemberDetails();

        mTopUpCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pagerAdapter = new TopUpPagerAdapter(getSupportFragmentManager());
        mTopUpViewPager.setAdapter(pagerAdapter);

        mTopUpTabLayout.setupWithViewPager(mTopUpViewPager, true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getMemberDetails(){
        MemberInfoRetrieval memberRetrieval = new MemberInfoRetrieval();
        memberRetrieval.retrieveInfo(this, new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                String amount = NumericUtils.formatCurrency(NumericUtils.IDR,
                        ((double) memberInfoResponse.getAvailableCashBalance()), false).trim();
                mTopUpBalanceAmountLabel.setText(amount);
                mTopUpBalanceDescriptionLabel.setText(mTopUpBalanceDescriptionStartText +
                    "(" + NumericUtils.IDR + ")");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(submitFragment.filePickerDialog != null){
                        submitFragment.filePickerDialog.show();
                    }
                }else{
                    Toast.makeText(TopUpActivity.this,
                            getString(R.string.top_up_filepickerdialog_no_permission),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView(){
        mTopUpHeader.setBackgroundColor(mColorGreyBlue800);
        mTopUpTitleLabel.setTextColor(mColorWhite);

        final IconicsDrawable menuUpIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_menu_up)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.toolbar_custom_title_icon_size);

        final IconicsDrawable menuDownIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_menu_down)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.toolbar_custom_title_icon_size);

        final IconicsDrawable syncIconEnabled = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_sync)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.toolbar_custom_right_icon_size);

        final IconicsDrawable syncIconPressed = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_sync)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.toolbar_custom_right_icon_size);

        mTopUpTitleLabel.setText(mTopUpTitleText);
        mTopUpTitleExpandIcon.setImageDrawable(menuUpIcon);
        mTopUpCloseIcon.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_close)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.toolbar_custom_left_icon_size));

        mTopUpRefreshIcon.setImageDrawable(syncIconEnabled);

        mTopUpTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTopUpBalanceExpandableLayout.isExpanded()){
                    mTopUpBalanceExpandableLayout.collapse();
                    mTopUpTitleExpandIcon.setImageDrawable(menuDownIcon);
                }else{
                    mTopUpBalanceExpandableLayout.expand();
                    mTopUpTitleExpandIcon.setImageDrawable(menuUpIcon);
                }
            }
        });

        mTopUpRefreshBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mTopUpRefreshIcon.setImageDrawable(syncIconPressed);
                        getMemberDetails();
                        refreshAllFragments();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mTopUpRefreshIcon.setImageDrawable(syncIconEnabled);
                        return true;
                }
                return false;
            }
        });
    }

    public void refreshAllFragments(){
        if(submitFragment != null && historyFragment != null) {
            submitFragment.refreshFragment();
            historyFragment.refreshFragment();
        }
    }

    private class TopUpPagerAdapter extends FragmentStatePagerAdapter {

        public TopUpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }


        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this);
    }
}
