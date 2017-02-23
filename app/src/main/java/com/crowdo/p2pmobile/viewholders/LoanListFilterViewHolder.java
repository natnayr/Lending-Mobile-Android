package com.crowdo.p2pmobile.viewholders;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.view.fragments.LoanListAdapter;
import com.github.zagum.switchicon.SwitchIconView;
import com.hanks.library.AnimateCheckBox;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 22/2/17.
 */

public class LoanListFilterViewHolder {

    @BindView(R.id.loan_list_view_filtering_term_3_button) LinearLayout term3Button;
    @BindView(R.id.loan_list_view_filtering_term_4_button) LinearLayout term4Button;
    @BindView(R.id.loan_list_view_filtering_term_5_button) LinearLayout term5Button;
    @BindView(R.id.loan_list_view_filtering_term_6_button) LinearLayout term6Button;
    @BindView(R.id.loan_list_view_filtering_term_9_button) LinearLayout term9Button;
    @BindView(R.id.loan_list_view_filtering_term_3_switch) SwitchIconView term3SwitchIconView;
    @BindView(R.id.loan_list_view_filtering_term_4_switch) SwitchIconView term4SwitchIconView;
    @BindView(R.id.loan_list_view_filtering_term_5_switch) SwitchIconView term5SwitchIconView;
    @BindView(R.id.loan_list_view_filtering_term_6_switch) SwitchIconView term6SwitchIconView;
    @BindView(R.id.loan_list_view_filtering_term_9_switch) SwitchIconView term9SwitchIconView;

    @BindView(R.id.loan_list_view_filtering_grade_Aplus_button) LinearLayout gradeAPlusButton;
    @BindView(R.id.loan_list_view_filtering_grade_A_button) LinearLayout gradeAButton;
    @BindView(R.id.loan_list_view_filtering_grade_Bplus_button) LinearLayout gradeBPlusButton;
    @BindView(R.id.loan_list_view_filtering_grade_B_button) LinearLayout gradeBButton;
    @BindView(R.id.loan_list_view_filtering_grade_C_button) LinearLayout gradeCButton;
    @BindView(R.id.loan_list_view_filtering_grade_D_button) LinearLayout gradeDButton;
    @BindView(R.id.loan_list_view_filtering_grade_E_button) LinearLayout gradeEButton;
    @BindView(R.id.loan_list_view_filtering_grade_Aplus_checkbox) AnimateCheckBox gradeAPlusCheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_A_checkbox) AnimateCheckBox gradeACheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_Bplus_checkbox) AnimateCheckBox gradeBPlusCheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_B_checkbox) AnimateCheckBox gradeBCheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_C_checkbox) AnimateCheckBox gradeCCheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_D_checkbox) AnimateCheckBox gradeDCheckBox;
    @BindView(R.id.loan_list_view_filtering_grade_E_checkbox) AnimateCheckBox gradeECheckBox;

    @BindView(R.id.loan_list_view_filtering_security_collateralized_button) LinearLayout securityCollaterialisedButton;
    @BindView(R.id.loan_list_view_filtering_security_uncollateralized_button) LinearLayout securityUncollaterialisedButton;
    @BindView(R.id.loan_list_view_filtering_security_work_order_invoice_button) LinearLayout securityWorkOrderInvoiceButton;
    @BindView(R.id.loan_list_view_filtering_security_collateralized_switch) SwitchIconView securityCollaterialisedSwitch;
    @BindView(R.id.loan_list_view_filtering_security_uncollateralized_switch) SwitchIconView securityUncollaterialisedSwitch;
    @BindView(R.id.loan_list_view_filtering_security_work_order_invoice_switch) SwitchIconView securityWorkOrderInvoiceSwitch;
    @BindView(R.id.loan_list_view_filtering_security_collateralized_label) TextView securityCollaterialisedLabel;
    @BindView(R.id.loan_list_view_filtering_security_uncollateralized_label) TextView securityUncollaterialisedLabel;
    @BindView(R.id.loan_list_view_filtering_security_work_order_invoice_label) TextView securityWorkOrderInvoiceLabel;

    @BindString(R.string.gradeA_plus) String gradeAPlus;
    @BindString(R.string.gradeA) String gradeA;
    @BindString(R.string.gradeB_plus) String gradeBPlus;
    @BindString(R.string.gradeB) String gradeB;
    @BindString(R.string.gradeC) String gradeC;
    @BindString(R.string.gradeD) String gradeD;
    @BindString(R.string.gradeE) String gradeE;

    @BindColor(R.color.color_divider_dark) int colorDividerDark;
    @BindColor(R.color.color_primary_text) int colorPrimaryText;

    private LoanListAdapter mLoanAdapter;

    public LoanListFilterViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void initView(LoanListAdapter mLoanAdapter) {
        this.mLoanAdapter = mLoanAdapter;
        term3Button.setOnClickListener(new FilteringTermButtonClickListener(term3SwitchIconView, ConstantVariables.IN_TERM_3));
        term4Button.setOnClickListener(new FilteringTermButtonClickListener(term4SwitchIconView, ConstantVariables.IN_TERM_4));
        term5Button.setOnClickListener(new FilteringTermButtonClickListener(term5SwitchIconView, ConstantVariables.IN_TERM_5));
        term6Button.setOnClickListener(new FilteringTermButtonClickListener(term6SwitchIconView, ConstantVariables.IN_TERM_6));
        term9Button.setOnClickListener(new FilteringTermButtonClickListener(term9SwitchIconView, ConstantVariables.IN_TERM_9));

        gradeAPlusButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeAPlusCheckBox, gradeAPlus));
        gradeAButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeACheckBox, gradeA));
        gradeBPlusButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeBPlusCheckBox, gradeBPlus));
        gradeBButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeBCheckBox, gradeB));
        gradeCButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeCCheckBox, gradeC));
        gradeDButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeDCheckBox, gradeD));
        gradeEButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeECheckBox, gradeE));

        gradeAPlusCheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeAPlusCheckBox, gradeAPlus));
        gradeACheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeACheckBox, gradeA));
        gradeBPlusCheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeBPlusCheckBox, gradeBPlus));
        gradeBCheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeBCheckBox, gradeB));
        gradeCCheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeCCheckBox, gradeC));
        gradeDCheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeDCheckBox, gradeD));
        gradeECheckBox.setOnClickListener(new FilteringGradeButtonClickListener(gradeECheckBox, gradeE));

        securityCollaterialisedButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityCollaterialisedSwitch, securityCollaterialisedLabel, ConstantVariables.IN_SEC_COLLATERALIZED));

        securityUncollaterialisedButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityUncollaterialisedSwitch, securityUncollaterialisedLabel, ConstantVariables.IN_SEC_UNCOLLATERALIZED));

        securityWorkOrderInvoiceButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityWorkOrderInvoiceSwitch, securityWorkOrderInvoiceLabel, ConstantVariables.IN_SEC_INVOICE_OR_CHEQUE));

    }

    class FilteringGradeButtonClickListener implements View.OnClickListener{
        private AnimateCheckBox animateCheckBox;
        private String grade;
        public FilteringGradeButtonClickListener(AnimateCheckBox animateCheckBox, String value){
            this.animateCheckBox = animateCheckBox;
            this.grade = value;
        }

        @Override
        public void onClick(View v) {
            if(animateCheckBox.isChecked()){
                animateCheckBox.setChecked(false);
                mLoanAdapter.getGradesToFilter().remove(this.grade);
            }else{
                animateCheckBox.setChecked(true);
                if(!mLoanAdapter.getGradesToFilter().contains(this.grade)){
                    mLoanAdapter.getGradesToFilter().add(this.grade);
                }
            }
            mLoanAdapter.searchLoans();
        }
    }

    class FilteringTermButtonClickListener implements View.OnClickListener{
        private SwitchIconView switchIconView;
        private int termPeriod;

        public FilteringTermButtonClickListener(SwitchIconView switchIconView, int termPeriod){
            this.switchIconView = switchIconView;
            this.termPeriod = termPeriod;
        }

        @Override
        public void onClick(View v) {
            if(switchIconView.isIconEnabled()){
                this.switchIconView.setIconEnabled(false);
                mLoanAdapter.getTermsToFilter().remove(new Integer(this.termPeriod));
            }else{
                this.switchIconView.setIconEnabled(true);
                if(!mLoanAdapter.getTermsToFilter().contains(this.termPeriod)){
                    mLoanAdapter.getTermsToFilter().add(new Integer(this.termPeriod));
                }
            }
            mLoanAdapter.searchLoans();
        }
    }

    class FilteringSecurityButtonClickListener implements View.OnClickListener{
        private SwitchIconView switchIconView;
        private TextView securityLabel;
        private String securityValue;

        public FilteringSecurityButtonClickListener(SwitchIconView switchIconView,
                                                    TextView securityLabel, String value){
            this.switchIconView = switchIconView;
            this.securityLabel = securityLabel;
            this.securityValue = value;
        }

        @Override
        public void onClick(View v) {
            if(switchIconView.isIconEnabled()){
                securityLabel.setTextColor(colorDividerDark);
                switchIconView.setIconEnabled(false);
                mLoanAdapter.getSecurityToFilter().remove(this.securityValue);
            }else{
                securityLabel.setTextColor(colorPrimaryText);
                switchIconView.setIconEnabled(true);
                if(!mLoanAdapter.getSecurityToFilter().contains(this.securityValue)){
                    mLoanAdapter.getSecurityToFilter().add(this.securityValue);
                }
            }
            mLoanAdapter.searchLoans();
        }
    }
}
