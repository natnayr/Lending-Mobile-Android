package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

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

    @BindView(R.id.checkout_summary_description_pending_bids_icon_main) ImageView mSummaryPendingBidsIconMain;
    @BindView(R.id.checkout_summary_description_avalible_balance_icon_main) ImageView mSummaryAvalibleBalanceIconMain;
    @BindView(R.id.checkout_summary_description_avalible_balance_icon_float) ImageView getmSummaryAvalibleBalanceIconFloat;
    @BindView(R.id.checkout_summary_description_amt_top_up_icon_main) ImageView mSummaryAmtTopUpIconMain;
    @BindView(R.id.checkout_summary_description_amt_top_up_icon_float) ImageView mSummaryAmtTopUpIconFloat;

    @BindView(R.id.checkout_summary_no_of_loans_icon) ImageView mSummaryCartTotalsIcon;

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

        mSummaryExpandIcon.setImageDrawable(chevronDownIcon);

        mSummarySyncIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_refresh)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_icon_size));

        mSummaryCloseIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_window_close)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_close_icon_size));

        mSummaryPendingBidsIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_format_list_bulleted_type)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_main_size));

        mSummaryAvalibleBalanceIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_cash)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_main_size));

        getmSummaryAvalibleBalanceIconFloat.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_alert_circle)
                        .backgroundColorRes(R.color.color_icons_text)
                        .roundedCornersDp(6)
                        .colorRes(R.color.color_primary)
                        .sizeRes(R.dimen.checkout_summary_description_icon_float_size));

        mSummaryAmtTopUpIconMain.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_wallet)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_main_size));

        mSummaryAmtTopUpIconFloat.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_plus_circle)
                        .backgroundColorRes(R.color.color_icons_text)
                        .roundedCornersDp(6)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_float_size));

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

        mSummaryCartTotalsIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_cash_multiple)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_cart_icon_size)
        );

    }
}
