package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.response.DefinitionBankInfoResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.File;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 19/7/17.
 */

public class TopUpSubmitViewHolder {

    @BindView(R.id.top_up_submit_bank_details_icon) ImageView mSubmitInfoIcon;
    @BindView(R.id.top_up_submit_bank_details_instructions) TextView mSubmitInfoStatementTextView;
    @BindView(R.id.top_up_submit_bank_details_indicate) TextView mSubmitInfoIndicateAccountInfo;
    @BindView(R.id.top_up_submit_bank_details_account_name_value) TextView mSubmitInfoAccountName;
    @BindView(R.id.top_up_submit_bank_details_account_number_value) TextView mSubmitInfoAccountNumber;
    @BindView(R.id.top_up_submit_bank_details_bank_name_value) TextView mSubmitInfoBankName;
    @BindView(R.id.top_up_submit_bank_details_branch_name_value) TextView mSubmitInfoBranchName;
    @BindView(R.id.top_up_submit_bank_details_swift_code_value) TextView mSubmitInfoSwiftCode;
    @BindView(R.id.top_up_submit_bank_details_bank_address_value) TextView mSubmitInfoBankAddress;

    @BindView(R.id.top_up_submit_upload_header_icon) ImageView mSubmitUploadIcon;
    @BindView(R.id.top_up_submit_upload_open_dialog_icon) ImageView mSubmitUploadOpenIcon;
    @BindView(R.id.top_up_submit_upload_reference_edittext) public EditText mSubmiUploadReferenceEditText;
    @BindView(R.id.top_up_submit_upload_open_dialog_button) public RelativeLayout mSubmitUploadOpenButton;
    @BindView(R.id.top_up_submit_upload_open_dialog_instruction_main) public TextView mSubmitUploadOpenInstructionsMainTextView;
    @BindView(R.id.top_up_submit_upload_open_dialog_instruction_sub) public TextView mSubmitUploadOpenInstructionsSubTextView;
    @BindView(R.id.top_up_submit_upload_submit_button) public Button mSubmitUploadSubloadButton;

    @BindString(R.string.top_up_submit_bank_details_instructions_with_pending) String mSubmitInfoWithPendingLabel;
    @BindString(R.string.top_up_submit_bank_details_instructions_without_pending) String mSubmitInfoWithoutPendingLabel;
    @BindString(R.string.top_up_submit_upload_details) String mSubmitInfoButtonInstructionsLabel;
    @BindString(R.string.top_up_submit_upload_added_info) String mSubmitInfoButtonAddedInstructionsLabel;
    @BindString(R.string.top_up_submit_bank_details_indicate) String mSubmitInfoIndicateLabel;
    @BindString(R.string.cancel_label) String mSubmitCancelLabel;
    @BindString(R.string.top_up_submit_upload_dialog_title) String mSubmitTitleLabel;
    @BindString(R.string.top_up_submit_upload_dialog_positive) String mSubmitPositiveLabel;

    @BindColor(R.color.color_secondary_text) int mColorSecondary;
    @BindColor(R.color.color_primary) public int mColorPrimary;

    public FilePickerDialog pickerDialog;
    private Context mContext;
    private static final String LOG_TAG = TopUpSubmitViewHolder.class.getSimpleName();
    public String[] allowedExtensions =  new String[]{"pdf","doc","docx","jpeg",
            "jpg","png","tif","bmp"};
    private long totalPendingAmount = 0;

    public TopUpSubmitViewHolder(View view, Context context) {
        this.mContext = context;
        ButterKnife.bind(this, view);

        DialogProperties dialogProperties = new DialogProperties();
        dialogProperties.selection_mode = DialogConfigs.SINGLE_MODE;
        dialogProperties.selection_type = DialogConfigs.FILE_SELECT;
        dialogProperties.root = new File(DialogConfigs.DEFAULT_DIR);
        dialogProperties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        dialogProperties.offset = new File(DialogConfigs.DEFAULT_DIR);
        dialogProperties.extensions = allowedExtensions;

        pickerDialog = new FilePickerDialog(context, dialogProperties);
        pickerDialog.setTitle(mSubmitTitleLabel);
        pickerDialog.setPositiveBtnName(mSubmitPositiveLabel);
        pickerDialog.setNegativeBtnName(mSubmitCancelLabel);
    }

    public void initView(){

        mSubmitUploadIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_credit_card)
                .sizeRes(R.dimen.wallet_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

        mSubmitUploadOpenIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_upload)
                .sizeRes(R.dimen.wallet_card_icon_size)
                .colorRes(R.color.color_icons_text));

        mSubmitInfoIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_bank)
                .sizeRes(R.dimen.wallet_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

        mSubmitUploadOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.show();
            }
        });

        initSubmitButtonState();

    }

    public void initSubmitButtonState(){
        mSubmitUploadOpenInstructionsMainTextView.setText(mSubmitInfoButtonInstructionsLabel);
        mSubmitUploadOpenInstructionsMainTextView.setTypeface(null,
                Typeface.NORMAL);

        mSubmitUploadOpenInstructionsSubTextView.setText(mSubmitInfoButtonAddedInstructionsLabel);
        mSubmitUploadOpenInstructionsSubTextView.setTypeface(null,
                Typeface.NORMAL);
        mSubmitUploadOpenInstructionsSubTextView.setTextColor(mColorSecondary);

        mSubmiUploadReferenceEditText.setText("");
    }

    public void fillMemberInfo(final MemberInfoResponse memberInfoResponse) {
        long totalPendingAmount = (memberInfoResponse.getTotalPendingBidAmount() -
                memberInfoResponse.getAvailableCashBalance());

        if(totalPendingAmount > 0) {
            //to top up for bidding
            String statement = String.format(mSubmitInfoWithPendingLabel,
                    NumericUtils.formatCurrency(NumericUtils.IDR, totalPendingAmount, true));
            mSubmitInfoStatementTextView.setText(statement);
        }

        String indicateStatement = String.format(mSubmitInfoIndicateLabel,
                memberInfoResponse.getUserId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSubmitInfoIndicateAccountInfo.setText(Html.fromHtml(indicateStatement,
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            mSubmitInfoIndicateAccountInfo.setText(Html.fromHtml(indicateStatement));
        }
    }

    public void fillDefinitionsBankInfo(final DefinitionBankInfoResponse definitionBankInfoResponse) {
        BankInfo bankInfo = definitionBankInfoResponse.getBankInfo();

        if (totalPendingAmount <= 0) {
            //with 0 or enough money
            String statement = String.format(mSubmitInfoWithoutPendingLabel,
                    NumericUtils.formatCurrency(NumericUtils.IDR, bankInfo.minimumTopUp, true));
            mSubmitInfoStatementTextView.setText(statement);
        }

        if(bankInfo.accountName != null) {
            mSubmitInfoAccountName.setText(bankInfo.accountName.trim());
        }

        if(bankInfo.accountNumber != null){
            mSubmitInfoAccountNumber.setText(bankInfo.accountNumber.trim());
        }

        if(bankInfo.bankName != null){
            mSubmitInfoBankName.setText(bankInfo.bankName.trim());
        }

        if(bankInfo.branchName != null){
            mSubmitInfoBranchName.setText(bankInfo.branchName.trim());
        }


        if(bankInfo.swiftCode != null){
            mSubmitInfoSwiftCode.setText(bankInfo.swiftCode.trim());
        }

        if(bankInfo.bankAddress != null){
            mSubmitInfoBankAddress.setText(bankInfo.bankAddress.trim());
        }

    }


}
