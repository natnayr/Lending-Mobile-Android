package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.DateUtils;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.others.TopUp;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.joda.time.DateTime;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 3/8/17.
 */

public class ItemTopUpHistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_top_up_history_date) TextView mHistoryItemDate;
    @BindView(R.id.item_top_up_history_amount) TextView mHistoryItemAmount;
    @BindView(R.id.item_top_up_history_status) TextView mHistoryItemStatus;
    @BindView(R.id.item_top_up_history_ref_value) TextView mHistoryItemRefValue;
    @BindView(R.id.item_top_up_history_proof_button) public ImageButton mHistoryItemProofImageButton;

    @BindString(R.string.top_up_history_item_unconfirmed) String mUnconfirmedLabel;
    @BindString(R.string.top_up_history_item_status_success) String mStatusSuccessLabel;
    @BindString(R.string.top_up_history_item_status_pending) String mStatusPendingLabel;
    @BindString(R.string.top_up_history_item_status_cancelled) String mStatusCancelledLabel;


    @BindColor(R.color.color_green_500) int mColorGreen500;
    @BindColor(R.color.color_amber_600) int mColorAmber600;
    @BindColor(R.color.color_primary_700) int mColorPrimary700;
    @BindColor(R.color.color_primary_text) int mColorPrimaryText;
    @BindColor(R.color.color_primary_text_700) int mColorPrimaryText700;

    private final static String LOG_TAG = ItemTopUpHistoryViewHolder.class.getSimpleName();
    private Context mContext;

    public ItemTopUpHistoryViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void initView(){
        mHistoryItemProofImageButton.setImageDrawable(new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_download)
                            .colorRes(R.color.color_secondary_text)
                            .sizeRes(R.dimen.top_up_card_item_history_icon_size));

    }

    public void populateItemDetails(final TopUp topUpItem,
                                    final CallBackUtil<String> callBackDownloadProof){

        if(topUpItem.getCreatedAt() != null){
            String dateText = DateUtils.dateTimeFormatter("dd MMM yyyy", topUpItem.getCreatedAt());
            mHistoryItemDate.setText(dateText);
        }else{
            String dateText = DateUtils.dateTimeFormatter("dd MMM yyyy",
                    DateTime.now().toString(DateUtils.SQL_DATE_TIME_PATTERN));
            mHistoryItemDate.setText(dateText);
        }

        if(topUpItem.getAmount() < 0){
            mHistoryItemAmount.setText(mUnconfirmedLabel);
            mHistoryItemAmount.setTextColor(mColorPrimaryText700);
        }else{
            String amountText = NumericUtils.IDR + " " + NumericUtils.formatCurrency(NumericUtils.IDR,
                    (double) topUpItem.getAmount(), false);
            mHistoryItemAmount.setText(amountText);
            mHistoryItemAmount.setTextColor(mColorPrimaryText);
        }

        if(!"".equals(topUpItem.getTransactionReference())){
            mHistoryItemRefValue.setText(topUpItem.getTransactionReference());
        }

        if(topUpItem.getStatus() != null) {
            switch (topUpItem.getStatus()) {
                case ConstantVariables.TOPUP_CONFIRM:
                    mHistoryItemStatus.setText(mStatusSuccessLabel);
                    mHistoryItemStatus.setTextColor(mColorGreen500);
                    break;
                case ConstantVariables.TOPUP_PENDING:
                    mHistoryItemStatus.setText(mStatusPendingLabel);
                    mHistoryItemStatus.setTextColor(mColorAmber600);
                    break;
                case ConstantVariables.TOPUP_CANCELLED:
                    mHistoryItemStatus.setText(mStatusCancelledLabel);
                    mHistoryItemStatus.setTextColor(mColorPrimary700);
                    break;
                default:
                    mHistoryItemStatus.setText(mUnconfirmedLabel);
                    mHistoryItemStatus.setTextColor(mColorPrimaryText700);
            }
        }else{
            mHistoryItemStatus.setText(mUnconfirmedLabel);
            mHistoryItemStatus.setTextColor(mColorPrimaryText700);
        }

        if(topUpItem.getTransactionProofUrl() != null) {
            mHistoryItemProofImageButton.setEnabled(true);
            mHistoryItemProofImageButton.setVisibility(View.VISIBLE);
            mHistoryItemProofImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackDownloadProof.eventCallBack(topUpItem.getTransactionProofUrl());
                }
            });
        }else{
            mHistoryItemProofImageButton.setEnabled(false);
            mHistoryItemProofImageButton.setVisibility(View.INVISIBLE);
        }

    }
}
