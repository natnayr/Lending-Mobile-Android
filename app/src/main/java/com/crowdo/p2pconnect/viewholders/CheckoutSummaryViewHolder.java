package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 23/5/17.
 */

public class CheckoutSummaryViewHolder {

    @BindView(R.id.checkout_summary_expand_btn) LinearLayout mSummaryExpandBtn;
    @BindView(R.id.checkout_summary_expand_icon) ImageView mSummaryExpandIcon;
    @BindView(R.id.checkout_summary_refresh_btn) LinearLayout mSummaryRefreshBtn;
    @BindView(R.id.checkout_summary_refresh_icon) ImageView mSummarySyncIcon;
    @BindView(R.id.checkout_summary_close_btn) LinearLayout mSummaryCloseBtn;
    @BindView(R.id.checkout_summary_close_icon) ImageView mSummaryCloseIcon;
    @BindView(R.id.checkout_summary_expandable) ExpandableLayout mSummaryExpandableLayout;
    @BindView(R.id.checkout_summary_recycler_view) RecyclerView mSummaryRecycleView;

    @BindView(R.id.checkout_summary_bidding_overall_pending_bids_icon_main) ImageView mSummaryPendingBidsIconMain;
    @BindView(R.id.checkout_summary_bidding_overall_available_balance_icon_main) ImageView mSummaryAvalibleBalanceIconMain;
    @BindView(R.id.checkout_summary_bidding_overall_avalible_balance_icon_float) ImageView mSummaryAvalibleBalanceIconFloat;
    @BindView(R.id.checkout_summary_bidding_overall_amt_top_up_icon_main) ImageView mSummaryAmtTopUpIconMain;
    @BindView(R.id.checkout_summary_bidding_overall_amt_top_up_icon_float) ImageView mSummaryAmtTopUpIconFloat;


    @BindView(R.id.checkout_summary_bidding_overall_pending_bids_value) TextView mSummaryPendingBidsValue;
    @BindView(R.id.checkout_summary_bidding_overall_available_balance_value) TextView mSummaryAvailableBalanceValue;
    @BindView(R.id.checkout_summary_bidding_overall_amt_top_up_value) TextView mSummaryAmtTopUpValue;

    @BindView(R.id.checkout_summary_action_button) LinearLayout mSummaryActionButton;
    @BindView(R.id.checkout_summary_action_button_label) TextView mSummaryActionButtonLabel;
    @BindView(R.id.checkout_summary_action_button_icon) ImageView mSummaryActionButtonIcon;

    @BindString(R.string.checkout_summary_bidding_overall_idr_currency_symbol_label) String mSummaryCurrencySymbol;
    @BindString(R.string.checkout_summary_container_top_up_label) String mSummaryTopUpLabel;
    @BindString(R.string.checkout_summary_container_update_label) String mSummaryUpdateLabel;
    @BindString(R.string.checkout_summary_container_confirm_label) String mSummaryConfirmLabel;

    @BindDrawable(R.drawable.ic_top_up_icon) Drawable mSummaryTopUpIcon;
    @BindColor(R.color.color_primary) int mColorPrimary;
    @BindColor(R.color.color_primary_text) int mColorPrimaryText;
    @BindColor(R.color.color_green_500) int mColorGreen500;
    @BindColor(R.color.color_accent) int mColorAccent;

    private static final String LOG_TAG = CheckoutSummaryViewHolder.class.getSimpleName();
    private Context mContext;

    public CheckoutSummaryViewHolder(View view, Context mContext) {
        this.mContext = mContext;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        final IconicsDrawable chevronUpIcon = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_chevron_up)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.checkout_summary_expand_icon_size);

        final IconicsDrawable chevronDownIcon = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_chevron_down)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.checkout_summary_expand_icon_size);

        mSummaryExpandIcon.setImageDrawable(chevronUpIcon);

        mSummarySyncIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_refresh)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_icon_size));

        mSummaryCloseIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_close)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_close_icon_size));

        mSummaryPendingBidsIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_format_list_bulleted_type)
                        .colorRes(R.color.color_primary_text)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAvalibleBalanceIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_cash)
                        .colorRes(R.color.color_primary_text)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAmtTopUpIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_wallet)
                        .colorRes(R.color.color_primary_text)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAmtTopUpIconFloat.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_plus_circle)
                        .backgroundColorRes(R.color.color_icons_text)
                        .roundedCornersDp(5)
                        .colorRes(R.color.color_primary_text)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_float_size));

        mSummaryExpandBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mSummaryExpandableLayout.isExpanded()){
                            mSummaryExpandableLayout.collapse();
                            mSummaryExpandIcon.setImageDrawable(chevronDownIcon);
                        }else{
                            mSummaryExpandableLayout.expand();
                            mSummaryExpandIcon.setImageDrawable(chevronUpIcon);
                        }
                    }
                }
        );

        mSummaryAmtTopUpValue.setText("-");
        mSummaryAmtTopUpValue.setTextColor(mColorPrimaryText);
        mSummaryAvalibleBalanceIconFloat.setVisibility(View.INVISIBLE);

        //scroll up and close expandable layout
        mSummaryRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    if(mSummaryExpandableLayout.isExpanded()){
                        mSummaryExpandableLayout.collapse();
                        mSummaryExpandIcon.setImageDrawable(chevronDownIcon);
                    }
                }
            }
        });
    }

    public void populateSummaryDetails(long totalPendingBids, long availableBalance){
        availableBalance = Long.valueOf("25000000000"); //TODO remove this
        mSummaryPendingBidsValue.setText(mSummaryCurrencySymbol + Long.toString(totalPendingBids));
        mSummaryAvailableBalanceValue.setText(mSummaryCurrencySymbol + Long.toString(availableBalance));

        if(totalPendingBids > availableBalance){
            //top up needed
            long requiredAmtTopUp = totalPendingBids - availableBalance;
            mSummaryAmtTopUpValue.setText(mSummaryCurrencySymbol + Long.toString(requiredAmtTopUp));
            mSummaryAmtTopUpValue.setTextColor(mColorPrimary);
            mSummaryAvalibleBalanceIconFloat.setVisibility(View.VISIBLE);
            mSummaryAvalibleBalanceIconFloat.setImageDrawable(
                    new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_alert_circle)
                            .backgroundColorRes(R.color.color_icons_text)
                            .roundedCornersDp(6)
                            .colorRes(R.color.color_primary)
                            .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_float_size));

            mSummaryActionButton.setBackgroundColor(mColorAccent);
            mSummaryActionButtonIcon.setImageDrawable(mSummaryTopUpIcon);
            mSummaryActionButtonLabel.setText(mSummaryTopUpLabel);

        }else{
            //top up not needed
            mSummaryAmtTopUpValue.setText("-");
            mSummaryAmtTopUpValue.setTextColor(mColorPrimaryText);
            mSummaryAvalibleBalanceIconFloat.setVisibility(View.INVISIBLE);

            mSummaryActionButton.setBackgroundColor(mColorGreen500);
            mSummaryActionButtonIcon.setImageDrawable(new IconicsDrawable(mContext)
                    .icon(CommunityMaterial.Icon.cmd_checkbox_marked_circle)
                    .sizeRes(R.dimen.checkout_summary_action_bottom_icon_size)
                    .colorRes(R.color.color_icons_text));
            mSummaryActionButtonLabel.setText(mSummaryConfirmLabel);
        }


    }
}
