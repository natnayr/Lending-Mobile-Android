package com.crowdo.p2pconnect.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
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
    @BindString(R.string.withdraw_balance_description_start) String mWithdrawBalanceDescriptionStartLabel;
    @BindString(R.string.withdraw_tab_title_one) String mWithdrawTabOneTitle;
    @BindString(R.string.withdraw_tab_title_two) String mWithdrawTabTwoTitle;

    @BindColor(R.color.color_grey_blue_800) int mColorGreyBlue800;
    @BindColor(R.color.color_icons_text) int mColorWhite;

    private String[] PAGE_TITLES;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        ButterKnife.bind(this);

        PAGE_TITLES = new String[]{mWithdrawTabOneTitle, mWithdrawTabTwoTitle};

        initView();

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
    }
}
