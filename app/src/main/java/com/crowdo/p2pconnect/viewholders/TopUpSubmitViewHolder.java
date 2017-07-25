package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
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
    @BindView(R.id.top_up_submit_upload_open_dialog_button) public RelativeLayout mSubmitFileFinderButton;

    @BindView(R.id.top_up_submit_bank_details_icon) ImageView mSubmitInfoIcon;
    @BindView(R.id.top_up_submit_bank_details_statement) TextView mSubmitInfoStatement;
    @BindView(R.id.top_up_submit_bank_details_indicate) TextView mSubmitInfoIndicateAccountInfo;
    @BindView(R.id.top_up_submit_bank_details_account_name_value) TextView mSubmitInfoAccountName;
    @BindView(R.id.top_up_submit_bank_details_account_number_value) TextView mSubmitInfoAccountNumber;
    @BindView(R.id.top_up_submit_bank_details_bank_name_value) TextView mSubmitInfoBankName;
    @BindView(R.id.top_up_submit_bank_details_branch_name_value) TextView mSubmitInfoBranchName;
    @BindView(R.id.top_up_submit_bank_details_swift_code_value) TextView mSubmitInfoSwiftCode;
    @BindView(R.id.top_up_submit_bank_details_bank_address_value) TextView mSubmitInfoBankAddress;

    @BindString(R.string.top_up_submit_bank_details_statement_with_pending) String mSubmitInfoWithPendingLabel;
    @BindString(R.string.top_up_submit_bank_details_statement_without_pending) String mSubmitInfoWithoutPendingLabel;
    @BindString(R.string.top_up_submit_bank_details_indicate) String mSubmitInfoIndicateLabel;
    private Context mContext;
    private static final String LOG_TAG = TopUpSubmitViewHolder.class.getSimpleName();

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

        mSubmitInfoIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_bank)
                .sizeRes(R.dimen.top_up_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

    }

    public void fillAccountInfo(final MemberInfoResponse memberInfoResponse){
        long totalPendingAmount = (memberInfoResponse.totalPendingBidAmount -
                memberInfoResponse.availableCashBalance);

        if(totalPendingAmount <= 0) {
            //enough money
            mSubmitInfoStatement.setText(mSubmitInfoWithoutPendingLabel);
        }else{
            //top up amount
            String statement = String.format(mSubmitInfoWithPendingLabel,
                    NumericUtils.formatCurrency(NumericUtils.IDR, totalPendingAmount, true));
            mSubmitInfoStatement.setText(statement);
        }

        String indicateStatement = String.format(mSubmitInfoIndicateLabel,
                memberInfoResponse.userId);
        Log.d(LOG_TAG, "APP fillAccountInfo indicateStatement: " + indicateStatement);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mSubmitInfoIndicateAccountInfo.setText(Html.fromHtml(indicateStatement,
                    Html.FROM_HTML_MODE_LEGACY));
        }else{
            mSubmitInfoIndicateAccountInfo.setText(Html.fromHtml(indicateStatement));
        }
    }
}
