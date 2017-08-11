package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.BankInfo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitViewHolder {
    @BindView(R.id.withdraw_submit_amount_edittext) public EditText mSubmitAmountEditText;
    @BindView(R.id.withdraw_submit_account_holder_icon) ImageView mSubmitAccountHolderIcon;
    @BindView(R.id.withdraw_submit_account_holder_label) TextView mSubmitAccountHolderLabel;
    @BindView(R.id.withdraw_submit_account_number_value) TextView mSubmitAccountNumberValue;
    @BindView(R.id.withdraw_submit_account_bank_name_value) TextView mSubmitAccountBankNameValue;
    @BindView(R.id.withdraw_submit_account_type_value) TextView mSubmitAccountTypeValue;
    @BindView(R.id.withdraw_submit_account_bank_address_value) TextView mSubmitAccountBankAddressValue;
    @BindView(R.id.withdraw_submit_account_location_value) TextView mSubmitAccountLocationValue;


    @BindString(R.string.withdraw_submit_account_holder_account_holder) String mSubmitAccountHolderStartText;

    private Context mContext;

    public WithdrawSubmitViewHolder(View view, Context context){
        this.mContext = context;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        mSubmitAccountHolderIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_account_card_details)
            .sizeRes(R.dimen.wallet_info_header_icon_size)
            .colorRes(R.color.color_primary_text));
    }

    public void populateView(final BankInfo bankInfo){

        if(bankInfo.accountHolder != null) {
            mSubmitAccountHolderLabel.setText(mSubmitAccountHolderStartText + bankInfo.accountHolder);
        }

        if(bankInfo.accountNumber != null){
            mSubmitAccountNumberValue.setText(bankInfo.accountNumber);
        }

        if(bankInfo.bankName != null){
            mSubmitAccountBankNameValue.setText(bankInfo.bankName);
        }

        if(bankInfo.accountType != null){
            mSubmitAccountTypeValue.setText(bankInfo.accountType);
        }

        if(bankInfo.bankAddress != null){
            mSubmitAccountBankAddressValue.setText(bankInfo.bankAddress);
        }

        if(bankInfo.bankCountry != null){
            mSubmitAccountLocationValue.setText(bankInfo.bankCountry);
        }
    }
}
