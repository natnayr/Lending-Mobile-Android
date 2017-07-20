package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
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

    @BindView(R.id.toolbar_custom_title) LinearLayout mSummaryTitleBtn;
    @BindView(R.id.toolbar_custom_title_icon) ImageView mSummaryTitleExpandIcon;
    @BindView(R.id.toolbar_custom_title_label) TextView mSummaryTitleLabel;

    @BindView(R.id.toolbar_custom_left_btn) public LinearLayout mSummaryCloseBtn;
    @BindView(R.id.toolbar_custom_left_icon) ImageView mSummaryCloseIcon;

    @BindView(R.id.toolbar_custom_right_btn) public LinearLayout mSummaryRefreshBtn;
    @BindView(R.id.toolbar_custom_right_icon) ImageView mSummarySyncIcon;

    @BindView(R.id.checkout_summary_update_notify_label) public TextView mSummaryUpdateNotifyLabel;
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

    @BindView(R.id.checkout_summary_action_button) public LinearLayout mSummaryActionButton;
    @BindView(R.id.checkout_summary_action_button_label) TextView mSummaryActionButtonLabel;
    @BindView(R.id.checkout_summary_action_button_icon) ImageView mSummaryActionButtonIcon;

    @BindView(R.id.checkout_summary_update_button) public LinearLayout mSummaryUpdateButton;
    @BindView(R.id.checkout_summary_update_button_label) TextView mSummaryUpdateButtonLabel;
    @BindView(R.id.checkout_summary_update_button_icon) ImageView mSummaryUpdateButtonIcon;

    @BindString(R.string.checkout_summary_title_label) String mSummaryTitleText;
    @BindString(R.string.checkout_summary_bidding_overall_idr_currency_symbol_label) String mSummaryCurrencySymbolText;
    @BindString(R.string.checkout_summary_container_top_up_label) String mSummaryTopUpText;
    @BindString(R.string.checkout_summary_container_update_label) String mSummaryUpdateText;
    @BindString(R.string.checkout_summary_container_confirm_label) String mSummaryConfirmText;

    @BindDrawable(R.drawable.ic_top_up_icon) Drawable mSummaryTopUpIcon;
    @BindColor(R.color.color_primary) int mColorPrimary;
    @BindColor(R.color.color_primary_text) int mColorPrimaryText;
    @BindColor(R.color.color_green_500) int mColorGreen500;
    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_amber_600) int mColorAmber600;

    private static final String LOG_TAG = CheckoutSummaryViewHolder.class.getSimpleName();
    private Context mContext;
    private CallBackUtil<Boolean> callBackFragmentPopulateSummary;
    private CallBackUtil<Object> callBackTopUpWebView;
    private CallBackUtil<Object> callBackConfirmBtnPress;
    private IconicsDrawable menuUpIcon;
    private IconicsDrawable menuDownIcon;

    public static final int CONFIRM_ACTION = 1;
    public static final int TOPUP_ACTION = 2;

    public CheckoutSummaryViewHolder(View view, Context context,
                                     CallBackUtil<Boolean> callBackSummaryRefresh,
                                     CallBackUtil<Object> callBackTopUpWebView,
                                     CallBackUtil<Object> callBackConfirmBtnPress) {
        this.mContext = context;
        this.callBackFragmentPopulateSummary = callBackSummaryRefresh;
        this.callBackTopUpWebView = callBackTopUpWebView;
        this.callBackConfirmBtnPress = callBackConfirmBtnPress;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        this.menuUpIcon = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_menu_up)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.toolbar_custom_title_icon_size);

        this.menuDownIcon = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_menu_down)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.toolbar_custom_title_icon_size);

        final IconicsDrawable syncIconEnabled = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_sync)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.toolbar_custom_right_icon_size);

        final IconicsDrawable syncIconPressed = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_sync)
                .colorRes(R.color.color_secondary_text_300)
                .sizeRes(R.dimen.toolbar_custom_right_icon_size);

        mSummaryTitleLabel.setText(mSummaryTitleText);

        mSummaryTitleExpandIcon.setImageDrawable(menuUpIcon);
        mSummarySyncIcon.setImageDrawable(syncIconEnabled);
        
        mSummaryRefreshBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mSummarySyncIcon.setImageDrawable(syncIconPressed);
                        callBackFragmentPopulateSummary.eventCallBack(true);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mSummarySyncIcon.setImageDrawable(syncIconEnabled);
                        return true;
                }
                return false;
            }
        });

        mSummaryCloseIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_close)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.toolbar_custom_left_icon_size));

        mSummaryPendingBidsIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_format_list_bulleted_type)
                        .colorRes(R.color.color_primary_text_800)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAvalibleBalanceIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_cash)
                        .colorRes(R.color.color_primary_text_800)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAmtTopUpIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_wallet)
                        .colorRes(R.color.color_primary_text_800)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_main_size));

        mSummaryAmtTopUpIconFloat.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_plus_circle)
                        .backgroundColorRes(R.color.color_icons_text)
                        .roundedCornersDp(5)
                        .colorRes(R.color.color_primary_text_800)
                        .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_float_size));

        mSummaryTitleBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mSummaryExpandableLayout.isExpanded()){
                            mSummaryExpandableLayout.collapse();
                            mSummaryTitleExpandIcon.setImageDrawable(menuDownIcon);
                        }else{
                            mSummaryExpandableLayout.expand();
                            mSummaryTitleExpandIcon.setImageDrawable(menuUpIcon);
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
                        mSummaryTitleExpandIcon.setImageDrawable(menuDownIcon);
                    }
                }
            }
        });



        mSummaryRecycleView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if(velocityY > 0){
                    if(mSummaryExpandableLayout.isExpanded()){
                        mSummaryExpandableLayout.collapse();
                        mSummaryTitleExpandIcon.setImageDrawable(menuDownIcon);
                        return true;
                    }
                }
                return false;
            }
        });

        //set up update button but hide first
        mSummaryUpdateButton.setVisibility(View.GONE);
        mSummaryUpdateButton.setBackgroundColor(mColorAmber600);
        mSummaryUpdateButtonIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_clipboard_flow)
            .colorRes(R.color.color_icons_text)
            .sizeRes(R.dimen.checkout_summary_action_bottom_icon_size));
        mSummaryUpdateButtonLabel.setText(mSummaryUpdateText);

    }

    public void populateSummaryDetails(long totalPendingBids, long availableBalance){
        mSummaryPendingBidsValue.setText(mSummaryCurrencySymbolText + Long.toString(totalPendingBids));
        mSummaryAvailableBalanceValue.setText(mSummaryCurrencySymbolText + Long.toString(availableBalance));

        if(totalPendingBids > availableBalance){
            //top up needed
            long requiredAmtTopUp = totalPendingBids - availableBalance;
            mSummaryAmtTopUpValue.setText(mSummaryCurrencySymbolText + Long.toString(requiredAmtTopUp));
            mSummaryAmtTopUpValue.setTextColor(mColorPrimary);
            mSummaryAvalibleBalanceIconFloat.setVisibility(View.VISIBLE);
            mSummaryAvalibleBalanceIconFloat.setImageDrawable(
                    new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_alert_circle)
                            .backgroundColorRes(R.color.color_icons_text)
                            .roundedCornersDp(6)
                            .colorRes(R.color.color_primary)
                            .sizeRes(R.dimen.checkout_summary_bidding_overall_icon_float_size));

            followUpActionTrigger(TOPUP_ACTION);

        }else{
            //top up not needed
            mSummaryAmtTopUpValue.setText("-");
            mSummaryAmtTopUpValue.setTextColor(mColorPrimaryText);
            mSummaryAvalibleBalanceIconFloat.setVisibility(View.INVISIBLE);

            followUpActionTrigger(CONFIRM_ACTION);
        }
    }

    public void followUpActionTrigger(int actionCall){
        switch (actionCall){
            case CONFIRM_ACTION:
                mSummaryActionButton.setBackgroundColor(mColorGreen500);
                mSummaryActionButtonIcon.setImageDrawable(new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_checkbox_marked_circle)
                        .sizeRes(R.dimen.checkout_summary_action_bottom_icon_size)
                        .colorRes(R.color.color_icons_text));
                mSummaryActionButtonLabel.setText(mSummaryConfirmText);
                mSummaryActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBackConfirmBtnPress.eventCallBack(null);
                    }
                });

                return;

            case TOPUP_ACTION:

                mSummaryActionButton.setBackgroundColor(mColorAccent);
                mSummaryActionButtonIcon.setImageDrawable(mSummaryTopUpIcon);
                mSummaryActionButtonLabel.setText(mSummaryTopUpText);
                mSummaryActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBackTopUpWebView.eventCallBack(null);
                    }
                });

                return;
        }
    }


}
