package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 23/5/17.
 */

public class CheckoutSummaryViewHolder {

    @BindView(R.id.checkout_summary_expand_btn) LinearLayout mSummaryExpandBtn;
    @BindView(R.id.checkout_summary_expand_icon) ImageView mSummaryExpandIcon;
    @BindView(R.id.checkout_summary_refresh_btn) LinearLayout mSummaryRefreshBtn;
    @BindView(R.id.checkout_summary_refresh_icon) ImageView mSummaryRefreshIcon;
    @BindView(R.id.checkout_summary_close_btn) LinearLayout mSummaryCloseBtn;
    @BindView(R.id.checkout_summary_close_icon) ImageView mSummaryCloseIcon;
    @BindView(R.id.checkout_summary_description_pending_bids_icon) ImageView mSummaryPendingBidsIcon;
    @BindView(R.id.checkout_summary_description_avalible_balance_icon) ImageView mSummaryAvalibleBalanceIcon;
    @BindView(R.id.checkout_summary_description_amt_top_up_icon) ImageView mSummaryAmtToTopUpIcon;


    private static final String LOG_TAG = CheckoutSummaryViewHolder.class.getSimpleName();
    private Context mContext;

    public CheckoutSummaryViewHolder(View view, Context mContext) {
        this.mContext = mContext;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        mSummaryExpandIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_chevron_down)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_expand_icon_size));

        mSummaryRefreshIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_refresh)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_icon_size));

        mSummaryCloseIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_window_close)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.checkout_summary_action_icon_size));

        mSummaryPendingBidsIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_format_list_bulleted_type)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_size));

        mSummaryAvalibleBalanceIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_cash)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_size));

        mSummaryAmtToTopUpIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_wallet)
                        .colorRes(R.color.color_accent)
                        .sizeRes(R.dimen.checkout_summary_description_icon_size));
    }
}
