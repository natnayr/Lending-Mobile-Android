package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.others.BankInfo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 19/7/17.
 */

public class TopUpSubmitViewHolder {

    @BindView(R.id.top_up_submit_upload_header_icon) ImageView mSubmitPaymentIcon;
    @BindView(R.id.top_up_submit_upload_open_dialog_icon) ImageView mSubmitPaymentUploadIcon;
    @BindView(R.id.top_up_submit_upload_reference_edittext) public EditText mSubmitPaymentReferenceEditText;
    @BindView(R.id.top_up_submit_upload_open_dialog_button) RelativeLayout mSubmitFileFinderButton;


    @BindView(R.id.top_up_submit_bank_details_icon) ImageView mSubmitBankDetailsIcon;
    @BindView(R.id.top_up_submit_bank_details_statement) TextView mSubmitBankDetailsStatement;



    @BindString(R.string.top_up_submit_bank_details_statement_with_pending) String mSubmitBankDetailsWithPending;
    @BindString(R.string.top_up_submit_bank_details_statement_without_pending) String mSubmitBankDetailsWithoutPending;
    private Context mContext;

    public TopUpSubmitViewHolder(View view, Context context) {
        this.mContext = context;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        mSubmitPaymentIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_credit_card)
                .sizeRes(R.dimen.top_up_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

        mSubmitPaymentUploadIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_upload)
                .sizeRes(R.dimen.top_up_card_icon_size)
                .colorRes(R.color.color_icons_text));

        mSubmitBankDetailsIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_bank)
                .sizeRes(R.dimen.top_up_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

    }

    public void fillAccountInfo(final double totalPendingAmount, final BankInfo bankInfo){
        if(totalPendingAmount <= 0) {
            mSubmitBankDetailsStatement.setText(mSubmitBankDetailsWithoutPending);
        }else{
            String statement = String.format(mSubmitBankDetailsWithPending,
                    NumericUtils.formatCurrency(NumericUtils.IDR, totalPendingAmount, true));
            mSubmitBankDetailsStatement.setText(statement);
        }

    }
}
