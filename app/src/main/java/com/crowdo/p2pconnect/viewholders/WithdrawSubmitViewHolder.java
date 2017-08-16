package com.crowdo.p2pconnect.viewholders;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.others.BankInfo;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.mateware.snacky.Snacky;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitViewHolder {
    @BindView(R.id.withdraw_submit_transfer_amount_edittext) public EditText mSubmitTransferAmountEditText;
    @BindView(R.id.withdraw_submit_account_holder_icon) ImageView mSubmitAccountHolderIcon;
    @BindView(R.id.withdraw_submit_account_holder_label) TextView mSubmitAccountHolderLabel;
    @BindView(R.id.withdraw_submit_account_number_value) TextView mSubmitAccountNumberValue;
    @BindView(R.id.withdraw_submit_account_bank_name_value) TextView mSubmitAccountBankNameValue;
    @BindView(R.id.withdraw_submit_account_type_value) TextView mSubmitAccountTypeValue;
    @BindView(R.id.withdraw_submit_account_bank_address_value) TextView mSubmitAccountBankAddressValue;
    @BindView(R.id.withdraw_submit_account_location_value) TextView mSubmitAccountLocationValue;
    @BindView(R.id.withdraw_submit_account_request_change_button) public LinearLayout mSubmitAccountRequestChangeButton;
    @BindView(R.id.withdraw_submit_account_request_change_icon) ImageView mSubmitAccountRequestChangeIcon;
    @BindView(R.id.withdraw_submit_notes_fees_header) LinearLayout mSubmitNotesFeesHeaderButton;
    @BindView(R.id.withdraw_submit_notes_fees_expandable_body) ExpandableLayout mSubmitNotesFeesExpandableLayout;
    @BindView(R.id.withdraw_submit_notes_fees_icon) ImageView mSubmitNotesFeesHeaderIcon;
    @BindView(R.id.withdraw_submit_notes_fees_tap_icon) ImageView mSubmitNotesFeesHeaderTapIcon;
    @BindView(R.id.withdraw_submit_fees_title) TextView mSubmitFeesTitle;
    @BindView(R.id.withdraw_submit_notes_title) TextView mSubmitNotesTitle;
    @BindView(R.id.withdraw_submit_transfer_submit_button) public Button mSubmitTransferSubmitButton;

    @BindString(R.string.withdraw_submit_account_holder_account_holder) String mSubmitAccountHolderStartText;
    @BindString(R.string.withdraw_submit_fees_title) String mSubmitFeesTitleText;
    @BindString(R.string.withdraw_submit_notes_title) String mSubmitNotesTitleText;
    @BindString(R.string.intent_email_client_chooser) String mEmailWithText;
    @BindString(R.string.withdraw_submit_no_email_app) String mNoEmailSupportLabel;

    private Context mContext;
    private View mRootView;

    public WithdrawSubmitViewHolder(View rootView, Context context){
        this.mContext = context;
        this.mRootView = rootView;
        ButterKnife.bind(this, rootView);
    }

    public void initView(){
        mSubmitAccountHolderIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_account_card_details)
            .sizeRes(R.dimen.wallet_info_header_icon_size)
            .colorRes(R.color.color_primary_text));

        mSubmitAccountRequestChangeIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_border_color)
            .sizeRes(R.dimen.wallet_card_icon_size)
            .colorRes(R.color.color_icons_text));

        mSubmitNotesFeesHeaderIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_information)
            .sizeRes(R.dimen.wallet_info_header_icon_size)
            .colorRes(R.color.color_primary_text));

        mSubmitNotesFeesHeaderTapIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_gesture_double_tap)
            .sizeRes(R.dimen.wallet_card_icon_size)
            .colorRes(R.color.color_primary_text));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSubmitFeesTitle.setText(Html.fromHtml(mSubmitFeesTitleText, Html.FROM_HTML_MODE_LEGACY));
        }else{
            mSubmitFeesTitle.setText(Html.fromHtml(mSubmitFeesTitleText));
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSubmitNotesTitle.setText(Html.fromHtml(mSubmitNotesTitleText, Html.FROM_HTML_MODE_LEGACY));
        }else{
            mSubmitNotesTitle.setText(Html.fromHtml(mSubmitNotesTitleText));
        }

        mSubmitNotesFeesHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSubmitNotesFeesExpandableLayout.isExpanded()){
                    mSubmitNotesFeesExpandableLayout.collapse();
                }else{
                    mSubmitNotesFeesExpandableLayout.expand();
                }
            }
        });

        mSubmitAccountRequestChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"enquiry.p2p@crowdo.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Request for change");

                ComponentName emailApp = intent.resolveActivity(mContext.getPackageManager());
                ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
                boolean hasEmailApp = emailApp != null && !emailApp.equals(unsupportedAction);

                if(hasEmailApp) {
                    mContext.startActivity(Intent.createChooser(intent, mEmailWithText));
                }else{
                    Spanned noEmailStatement;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        noEmailStatement = Html.fromHtml(mNoEmailSupportLabel, Html.FROM_HTML_MODE_LEGACY);
                    } else {
                        noEmailStatement = Html.fromHtml(mNoEmailSupportLabel);
                    }

                    Snacky.builder().setView(mRootView)
                            .setText(noEmailStatement)
                            .setDuration(Snackbar.LENGTH_LONG)
                            .info().show();
                }
            }
        });


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
