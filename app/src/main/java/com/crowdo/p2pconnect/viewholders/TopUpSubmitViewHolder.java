package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 19/7/17.
 */

public class TopUpSubmitViewHolder {

    @BindView(R.id.top_up_submit_payment_icon) ImageView mSubmitPaymentIcon;
    @BindView(R.id.top_up_submit_payment_upload_icon) ImageView mSubmitPaymentUploadIcon;
    @BindView(R.id.top_up_submit_payment_reference_edittext) public EditText mSubmitPaymentReferenceEditText;
    @BindView(R.id.top_up_submit_bank_details_icon) ImageView mSubmitBankIcon;
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

        mSubmitBankIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_bank)
                .sizeRes(R.dimen.top_up_info_header_icon_size)
                .colorRes(R.color.color_primary_text));

    }
}
