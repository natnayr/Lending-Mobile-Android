package com.crowdo.p2pconnect.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.support.NetworkConnectionChecks;
import com.crowdo.p2pconnect.view.fragments.WithdrawHistoryFragment;
import com.crowdo.p2pconnect.view.fragments.WithdrawSubmitFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 10/8/17.
 */

public class WithdrawActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_custom_header) RelativeLayout mWithdrawHeader;
    @BindView(R.id.toolbar_custom_title) LinearLayout mWithdrawTitleBtn;
    @BindView(R.id.toolbar_custom_title_label) TextView mWithdrawTitleLabel;
    @BindView(R.id.toolbar_custom_title_icon) ImageView mWithdrawTitleExpandIcon;
    @BindView(R.id.toolbar_custom_left_btn) LinearLayout mWithdrawCloseBtn;
    @BindView(R.id.toolbar_custom_left_icon) ImageView mWithdrawCloseIcon;
    @BindView(R.id.toolbar_custom_right_btn) LinearLayout mWithdrawRefreshBtn;
    @BindView(R.id.toolbar_custom_right_icon) ImageView mWithdrawRefreshIcon;
    @BindView(R.id.withdraw_balance_expandable) public ExpandableLayout mWithdrawBalanceExpandableLayout;
    @BindView(R.id.withdraw_balance_amount) TextView mWithdrawBalanceAmountLabel;
    @BindView(R.id.withdraw_balance_description) TextView mWithdrawBalanceDescriptionLabel;

    @BindView(R.id.withdraw_viewpager) ViewPager mWithdrawViewPager;
    @BindView(R.id.withdraw_tablayout) TabLayout mWithdrawTabLayout;

    @BindString(R.string.withdraw_title_label) String mWithdrawTitleText;
    @BindString(R.string.withdraw_balance_description_start) String mWithdrawBalanceDescriptionStartText;
    @BindString(R.string.withdraw_tab_title_one) String mWithdrawTabOneTitleText;
    @BindString(R.string.withdraw_tab_title_two) String mWithdrawTabTwoTitleText;

    @BindColor(R.color.color_grey_blue_800) int mColorGreyBlue800;
    @BindColor(R.color.color_icons_text) int mColorWhite;

    private WithdrawPagerAdapter pagerAdapter;
    private String[] PAGE_TITLES;
    private Fragment[] PAGES;
    private WithdrawSubmitFragment submitFragment;
    private WithdrawHistoryFragment historyFragment;

    public WithdrawActivity() {
        submitFragment = new WithdrawSubmitFragment();
        historyFragment = new WithdrawHistoryFragment();
        PAGES = new Fragment[]{submitFragment, historyFragment};
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        ButterKnife.bind(this);

        PAGE_TITLES = new String[]{mWithdrawTabOneTitleText, mWithdrawTabTwoTitleText};

        initView();

        getMemberDetails();

        mWithdrawCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pagerAdapter = new WithdrawPagerAdapter(getSupportFragmentManager());
        mWithdrawViewPager.setAdapter(pagerAdapter);
        mWithdrawTabLayout.setupWithViewPager(mWithdrawViewPager, true);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getMemberDetails(){
        MemberInfoRetrieval memberRetrieval = new MemberInfoRetrieval();
        memberRetrieval.retrieveInfo(this, new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                String amount = NumericUtils.formatCurrency(NumericUtils.IDR,
                        ((double) memberInfoResponse.getAvailableCashBalance()), false).trim();
                mWithdrawBalanceAmountLabel.setText(amount);
                mWithdrawBalanceDescriptionLabel.setText(mWithdrawBalanceDescriptionStartText +
                    "(" + NumericUtils.IDR + ")");

                if(submitFragment != null){
                    if(memberInfoResponse.getBankInfo() != null) {
                        submitFragment.setMemberInfo(memberInfoResponse.getBankInfo(),
                                memberInfoResponse.getAvailableCashBalance());
                    }
                }
            }
        });
    }

    private void initView(){
        mWithdrawHeader.setBackgroundColor(mColorGreyBlue800);
        mWithdrawTitleLabel.setTextColor(mColorWhite);

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

        mWithdrawTitleLabel.setText(mWithdrawTitleText);
        mWithdrawTitleExpandIcon.setImageDrawable(menuUpIcon);
        mWithdrawCloseIcon.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_close)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.toolbar_custom_left_icon_size));

        mWithdrawTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWithdrawBalanceExpandableLayout.isExpanded()){
                    mWithdrawBalanceExpandableLayout.collapse();
                    mWithdrawTitleExpandIcon.setImageDrawable(menuDownIcon);
                }else{
                    mWithdrawBalanceExpandableLayout.expand();
                    mWithdrawTitleExpandIcon.setImageDrawable(menuUpIcon);
                }
            }
        });

        mWithdrawRefreshIcon.setImageDrawable(syncIconEnabled);
        mWithdrawRefreshBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mWithdrawRefreshIcon.setImageDrawable(syncIconPressed);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mWithdrawRefreshIcon.setImageDrawable(syncIconEnabled);
                        return true;
                }
                return false;
            }
        });

    }

    private class WithdrawPagerAdapter extends FragmentStatePagerAdapter{

        public WithdrawPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this);
    }
}
