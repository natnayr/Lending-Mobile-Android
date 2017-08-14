package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.DateUtils;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.others.WalletEntry;

import org.joda.time.DateTime;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 14/8/17.
 */

public class ItemWithdrawHistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_withdraw_history_date) TextView mHistoryItemDate;
    @BindView(R.id.item_withdraw_history_amount) TextView mHistoryItemAmount;
    @BindView(R.id.item_withdraw_history_status) TextView mHistoryItemStatus;
    @BindView(R.id.item_withdraw_history_ref_value) TextView mHistoryItemRefValue;
    @BindView(R.id.item_withdraw_history_fee_value) TextView mHistoryItemFeeValue;

    @BindString(R.string.withdraw_history_item_unconfirmed) String mUnconfirmedLabel;
    @BindString(R.string.withdraw_history_item_status_success) String mStatusSuccessLabel;
    @BindString(R.string.withdraw_history_item_status_pending) String mStatusPendingLabel;
    @BindString(R.string.withdraw_history_item_status_cancelled) String mStatusCancelledLabel;

    @BindColor(R.color.color_green_500) int mColorGreen500;
    @BindColor(R.color.color_amber_600) int mColorAmber600;
    @BindColor(R.color.color_primary_700) int mColorPrimary700;
    @BindColor(R.color.color_primary_text) int mColorPrimaryText;
    @BindColor(R.color.color_primary_text_700) int mColorPrimaryText700;

    private final static String LOG_TAG = ItemWithdrawHistoryViewHolder.class.getSimpleName();
    private Context mContext;

    public ItemWithdrawHistoryViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void populateItemDetails(final WalletEntry walletEntryItem){

        Log.d(LOG_TAG, "APP created_at: "  + walletEntryItem.getCreatedAt());

        if(walletEntryItem.getCreatedAt() != null){
            String dateTime = DateUtils.dateTimeFormatter("dd MMM yyyy", walletEntryItem.getCreatedAt());
            mHistoryItemAmount.setText(dateTime);
        }else{
            String dateText = DateUtils.dateTimeFormatter("dd MMM yyyy",
                    DateTime.now().toString(DateUtils.SQL_DATE_TIME_PATTERN));
            mHistoryItemDate.setText(dateText);
        }

        if(walletEntryItem.getAmount() < 0){
            mHistoryItemAmount.setText(mUnconfirmedLabel);
            mHistoryItemAmount.setTextColor(mColorPrimaryText700);
        }else{
            String amountText = NumericUtils.IDR + " " + NumericUtils.formatCurrency(NumericUtils.IDR,
                    (double) walletEntryItem.getAmount(), false);
            mHistoryItemAmount.setText(amountText);
            mHistoryItemAmount.setTextColor(mColorPrimaryText);
        }

        if(!"".equals(walletEntryItem.getTransactionReference())){
            mHistoryItemRefValue.setText(walletEntryItem.getTransactionReference());
        }

        if(walletEntryItem.getStatus() != null){
            switch (walletEntryItem.getStatus()){
                case ConstantVariables.WALLET_CONFIRM:
                    mHistoryItemStatus.setText(mStatusSuccessLabel);
                    mHistoryItemStatus.setTextColor(mColorGreen500);
                    break;
                case ConstantVariables.WALLET_PENDING:
                    mHistoryItemStatus.setText(mStatusPendingLabel);
                    mHistoryItemStatus.setTextColor(mColorAmber600);
                    break;
                case ConstantVariables.WALLET_CANCELLED:
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

        if(walletEntryItem.getFeeAmount() < 0){
            mHistoryItemFeeValue.setText(mUnconfirmedLabel);
            mHistoryItemFeeValue.setTextColor(mColorPrimaryText700);
        }else{
            String amountText = NumericUtils.IDR + " " + NumericUtils.formatCurrency(NumericUtils.IDR,
                    (double) walletEntryItem.getFeeAmount(), false);
            mHistoryItemFeeValue.setText(amountText);
            mHistoryItemFeeValue.setTextColor(mColorPrimaryText);
        }

    }
}
