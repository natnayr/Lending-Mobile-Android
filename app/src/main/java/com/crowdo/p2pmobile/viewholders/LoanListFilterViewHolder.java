package com.crowdo.p2pmobile.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.github.zagum.switchicon.SwitchIconView;
import com.hanks.library.AnimateCheckBox;

import butterknife.BindColor;
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

    @BindColor(R.color.color_divider_dark) int colorDividerDark;
    @BindColor(R.color.color_primary_text) int colorPrimaryText;

    public LoanListFilterViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void initView() {
        term3Button.setOnClickListener(new FilteringTermButtonClickListener(term3SwitchIconView));
        term4Button.setOnClickListener(new FilteringTermButtonClickListener(term4SwitchIconView));
        term5Button.setOnClickListener(new FilteringTermButtonClickListener(term5SwitchIconView));
        term6Button.setOnClickListener(new FilteringTermButtonClickListener(term6SwitchIconView));
        term9Button.setOnClickListener(new FilteringTermButtonClickListener(term9SwitchIconView));

        gradeAPlusButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeAPlusCheckBox));
        gradeAButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeACheckBox));
        gradeBPlusButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeBPlusCheckBox));
        gradeBButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeBCheckBox));
        gradeCButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeCCheckBox));
        gradeDButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeDCheckBox));
        gradeEButton.setOnClickListener(new FilteringGradeButtonClickListener(gradeECheckBox));

        securityCollaterialisedButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityCollaterialisedSwitch, securityCollaterialisedLabel));

        securityUncollaterialisedButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityUncollaterialisedSwitch, securityUncollaterialisedLabel));

        securityWorkOrderInvoiceButton.setOnClickListener(new FilteringSecurityButtonClickListener(
                securityWorkOrderInvoiceSwitch, securityWorkOrderInvoiceLabel));
    }

    class FilteringSecurityButtonClickListener implements View.OnClickListener{
        private SwitchIconView switchIconView;
        private TextView securityLabel;

        public FilteringSecurityButtonClickListener(SwitchIconView switchIconView, TextView securityLabel){
            this.switchIconView = switchIconView;
            this.securityLabel = securityLabel;
        }

        @Override
        public void onClick(View v) {
            if(switchIconView.isIconEnabled()){
                securityLabel.setTextColor(colorDividerDark);
                switchIconView.setIconEnabled(false);
            }else{
                securityLabel.setTextColor(colorPrimaryText);
                switchIconView.setIconEnabled(true);
            }
        }
    }

    class FilteringGradeButtonClickListener implements View.OnClickListener{
        private AnimateCheckBox animateCheckBox;
        public FilteringGradeButtonClickListener(AnimateCheckBox animateCheckBox){
            this.animateCheckBox = animateCheckBox;
        }

        @Override
        public void onClick(View v) {
            animateCheckBox.setChecked(!animateCheckBox.isChecked());
        }
    }

    class FilteringTermButtonClickListener implements View.OnClickListener{
        private SwitchIconView switchIconView;

        public FilteringTermButtonClickListener(SwitchIconView switchIconView){
            this.switchIconView = switchIconView;
        }

        @Override
        public void onClick(View v) {
            this.switchIconView.switchState();
        }
    }
}
