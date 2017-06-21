package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.view.adapters.CheckoutSummaryAdapter;
import com.loopeer.itemtouchhelperextension.Extension;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 26/5/17.
 */

public class ItemCheckoutSummaryViewHolder extends RecyclerView.ViewHolder implements Extension{

    @Nullable@BindView(R.id.item_checkout_summary_container) public RelativeLayout mItemContainer;

    @Nullable @BindView(R.id.item_checkout_summary_loanid_value) TextView mItemLoanId;
    @Nullable @BindView(R.id.item_checkout_summary_tenure_value) TextView mItemTenure;
    @Nullable @BindView(R.id.item_checkout_summary_grade_value) TextView mItemGrade;
    @Nullable @BindView(R.id.item_checkout_summary_interest_rate_value) TextView mItemInterestRate;

    @Nullable @BindView(R.id.item_checkout_summary_bid_edit_minus_btn) ImageButton mItemBidMinusBtn;
    @Nullable @BindView(R.id.item_checkout_summary_bid_edit_plus_btn) ImageButton mItemBidPlusBtn;
    @Nullable @BindView(R.id.item_checkout_summary_enter_amount_edittext) EditText mItemEditText;

    @Nullable @BindView(R.id.item_checkout_summary_delete_btn) public LinearLayout mItemDeleteBtn;
    @Nullable @BindView(R.id.item_checkout_summary_delete_icon) ImageView mItemDeleteIcon;

    @Nullable @BindString(R.string.item_checkout_summary_tenure_label) String mItemTenureLabel;
    @Nullable @BindString(R.string.item_checkout_summary_interest_rate_label) String mItemInterestRateLabel;

    @Nullable @BindColor(R.color.grade_color_A_plus) int mColorAPlus;
    @Nullable @BindColor(R.color.grade_color_A) int mColorA;
    @Nullable @BindColor(R.color.grade_color_B_plus) int mColorBPlus;
    @Nullable @BindColor(R.color.grade_colorB) int mColorB;
    @Nullable @BindColor(R.color.grade_colorC) int mColorC;
    @Nullable @BindColor(R.color.grade_color_D) int mColorD;
    @Nullable @BindColor(R.color.grade_color_E) int mColorE;

    @Nullable @BindDrawable(R.drawable.item_checkout_summary_minus_btn_enabled) Drawable mMinusEnabledDrawable;
    @Nullable @BindDrawable(R.drawable.item_checkout_summary_minus_btn_pressed) Drawable mMinusPressedDrawable;
    @Nullable @BindDrawable(R.drawable.item_checkout_summary_plus_btn_enabled) Drawable mPlusEnabledDrawable;
    @Nullable @BindDrawable(R.drawable.item_checkout_summary_plus_btn_pressed) Drawable mPlusPressedDrawable;


    private static final int AMOUNT_UNIT = 1;
    private static final int ENTER_AMOUNT_MAX_LENGTH = 4;

    private Context mContext;
    private CheckoutSummaryAdapter mAdapter;
    private Investment bidInvestmentItem;
    private Loan bidLoanItem;

    public ItemCheckoutSummaryViewHolder(View itemView, Context context, CheckoutSummaryAdapter adapter) {
        super(itemView);
        this.mContext = context;
        this.mAdapter = adapter;
        ButterKnife.bind(this, itemView);
    }

    public void initView(){

        mItemBidMinusBtn.setBackground(mMinusEnabledDrawable);
        mItemBidPlusBtn.setBackground(mPlusEnabledDrawable);

        mItemDeleteIcon.setImageDrawable(new IconicsDrawable(mContext)
            .icon(CommunityMaterial.Icon.cmd_delete)
            .colorRes(R.color.color_icons_text)
            .sizeRes(R.dimen.item_checkout_summary_cart_action_icon_size));
    }

    public void populateItemDetails(final int layoutPosition, final Investment bidInvestmentItem, final Loan bidLoanItem){

        this.bidInvestmentItem = bidInvestmentItem;
        this.bidLoanItem = bidLoanItem;

        final IconicsDrawable minusEnabled = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_minus)
                .colorRes(R.color.color_primary_text)
                .sizeRes(R.dimen.item_checkout_summary_cart_edit_icon_size);
        final IconicsDrawable minusPressed = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_minus)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.item_checkout_summary_cart_edit_icon_size);

        final IconicsDrawable plusEnabled = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_plus)
                .colorRes(R.color.color_primary_text)
                .sizeRes(R.dimen.item_checkout_summary_cart_edit_icon_size);
        final IconicsDrawable plusPressed = new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_plus)
                .colorRes(R.color.color_icons_text)
                .sizeRes(R.dimen.item_checkout_summary_cart_edit_icon_size);

        mItemBidMinusBtn.setImageDrawable(minusEnabled);
        mItemBidPlusBtn.setImageDrawable(plusEnabled);

        if(!"".equals(bidLoanItem.getLoanId().trim())) {
            mItemLoanId.setText(bidLoanItem.getLoanId().toUpperCase().trim());
        }

        mItemTenure.setText(Integer.toString(bidLoanItem.getTenure())
                + mItemTenureLabel);

        if(!"".equals(bidLoanItem.getGrade().trim())) {
            String grade = bidLoanItem.getGrade().trim();
            mItemGrade.setText(grade);
            switch (grade) {
                case "A+":
                    mItemGrade.setTextColor(mColorAPlus);
                    break;
                case "A":
                    mItemGrade.setTextColor(mColorA);
                    break;
                case "B+":
                    mItemGrade.setTextColor(mColorBPlus);
                    break;
                case "B":
                    mItemGrade.setTextColor(mColorB);
                    break;
                case "C":
                    mItemGrade.setTextColor(mColorC);
                    break;
                case "D":
                    mItemGrade.setTextColor(mColorD);
                    break;
                case "E":
                    mItemGrade.setTextColor(mColorE);
                    break;
                default:
            }
        }

        mItemInterestRate.setText(Double.toString(bidLoanItem.getInterestRate())
                + mItemInterestRateLabel);

        long originalInvestAmount = Double.valueOf(bidInvestmentItem.getInvestAmount()).longValue();
        if(originalInvestAmount > 0){
            Long unitInvestAmount = originalInvestAmount / ConstantVariables.IDR_BASE_UNIT;
            mItemEditText.setText(Long.toString(unitInvestAmount));
        }

        mItemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.doDelete(layoutPosition, bidInvestmentItem, bidLoanItem);
            }
        });

        mItemBidMinusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mItemBidMinusBtn.setBackground(mMinusPressedDrawable);
                        mItemBidMinusBtn.setImageDrawable(minusPressed);
                        addToEnterAmount(-AMOUNT_UNIT);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mItemBidMinusBtn.setBackground(mMinusEnabledDrawable);
                        mItemBidMinusBtn.setImageDrawable(minusEnabled);
                        return true;
                }
                return false;
            }
        });

        mItemBidPlusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mItemBidPlusBtn.setBackground(mPlusPressedDrawable);
                        mItemBidPlusBtn.setImageDrawable(plusPressed);
                        addToEnterAmount(AMOUNT_UNIT);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mItemBidPlusBtn.setBackground(mPlusEnabledDrawable);
                        mItemBidPlusBtn.setImageDrawable(plusEnabled);
                        return true;
                }

                return false;
            }
        });

    }

    private void addToEnterAmount(int unitAmount) {
        String cleanString = mItemEditText.getText().toString()
                .replaceAll("[^\\d]", "").trim();

        if ("0".equals(cleanString) || "".equals(cleanString)) {
            if (unitAmount > 0) {
                //post byAmount
                mItemEditText.setText(Integer.toString(AMOUNT_UNIT));
            }
        } else if (cleanString.length() <= ENTER_AMOUNT_MAX_LENGTH) {
            int curAmount = Integer.parseInt(cleanString);
            if ((curAmount + unitAmount) >= 0 &&
                    (curAmount + unitAmount) < (Math.pow(10, ENTER_AMOUNT_MAX_LENGTH))) {
                mItemEditText.setText(Integer.toString(curAmount + unitAmount));
            }
        }
    }


    @Override
    public float getActionWidth() {
        return mItemDeleteBtn.getWidth();
    }
}
