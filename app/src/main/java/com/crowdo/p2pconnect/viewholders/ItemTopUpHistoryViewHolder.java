package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.DateUtils;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.others.TopUp;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

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
    @BindView(R.id.item_top_up_history_proof_button) ImageButton mHistoryItemProofImageButton;

    @BindString(R.string.top_up_history_item_amount_unconfirmed) String mAmountUnconfirmedLabel;
    @BindString(R.string.top_up_history_item_status_success) String mStatusSuccessLabel;
    @BindString(R.string.top_up_history_item_status_pending) String mStatusPendingLabel;
    @BindString(R.string.top_up_history_item_status_cancelled) String mStatusCancelledLabel;


    @BindColor(R.color.color_primary_text) int mColorPrimaryText;
    @BindColor(R.color.color_primary_text_700) int mColorPrimaryText700;

    private static final String SUCCESS_VAL = "success";
    private Context mContext;

    public ItemTopUpHistoryViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void initView(){
        mHistoryItemProofImageButton.setImageDrawable(new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_download)
                            .colorRes(R.color.color_primary_text)
                            .sizeRes(R.dimen.top_up_card_item_icon_size));

    }

    public void populateItemDetails(final TopUp topUpItem){

        if(topUpItem.getCreatedAt() != null){
            String dateText = DateUtils.dateTimeFormatter("dd MMM yyyy", topUpItem.getCreatedAt());
            mHistoryItemDate.setText(dateText);
        }

        if(topUpItem.getAmount() < 0){
            mHistoryItemAmount.setText(mAmountUnconfirmedLabel);
            mHistoryItemAmount.setTextColor(mColorPrimaryText700);
        }else{
            String amountText = NumericUtils.IDR + " " + NumericUtils.formatCurrency(NumericUtils.IDR,
                    (double) topUpItem.getAmount(), false);
            mHistoryItemAmount.setText(amountText);
            mHistoryItemAmount.setTextColor(mColorPrimaryText);
        }

        if(topUpItem.getTransactionReference() != null){
            mHistoryItemRefValue.setText(topUpItem.getTransactionReference());
        }

    }
}
