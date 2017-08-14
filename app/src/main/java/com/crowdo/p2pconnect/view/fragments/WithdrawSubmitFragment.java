package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.viewholders.WithdrawSubmitViewHolder;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitFragment extends Fragment {

    private WithdrawSubmitViewHolder viewHolder;
    private long avalibleCashBalance;

    @BindString(R.string.withdraw_submit_enter_amount_warning_above_amount) String mAmountTooHighWarning;
    @BindString(R.string.withdraw_submit_enter_amount_warning_empty_entry) String mAmountEmptyStatement;

    private static final String LOG_TAG = WithdrawSubmitFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_withdraw_submit, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new WithdrawSubmitViewHolder(rootView, getActivity());
        viewHolder.initView();

        getMemberDetails();

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{viewHolder.mSubmitTransferAmountEditText});
        viewHolder.mSubmitTransferAmountEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            SoftInputHelper.hideSoftKeyboardOfView(view,view);
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        viewHolder.mSubmitTransferAmountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    formatAndCheckEntry();
                }
            }
        });

        viewHolder.mSubmitTransferSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user transfer ammount
                String amountStrInput = viewHolder.mSubmitTransferAmountEditText
                        .getText().toString().trim().replaceAll("[^\\d.]", "");
                if("".equals(amountStrInput)){
                    SnackBarUtil.snackBarForInfoCreate(getView(), mAmountEmptyStatement,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                transferAmount(Long.parseLong(amountStrInput));
            }
        });

        return rootView;
    }

    private void getMemberDetails(){
        MemberInfoRetrieval memberRetrieval = new MemberInfoRetrieval();
        memberRetrieval.retrieveInfo(getActivity(), new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                if(memberInfoResponse.getBankInfo() != null) {
                    if(viewHolder != null){
                        viewHolder.populateView(memberInfoResponse.getBankInfo());
                    }
                    avalibleCashBalance = memberInfoResponse.getAvailableCashBalance();
                }
            }
        });
    }

    private void formatAndCheckEntry(){
        if(viewHolder != null) {
            String amountInput = viewHolder.mSubmitTransferAmountEditText
                    .getText().toString().trim().replaceAll("[^\\d.]", "");

            if(!"".equals(amountInput)) {
                double amount = Double.parseDouble(amountInput);

                if(amount > this.avalibleCashBalance){
                    String cashBalanceFigure = NumericUtils.formatCurrency(NumericUtils.IDR, (double) this.avalibleCashBalance, false);
                    String statement = String.format(mAmountTooHighWarning, NumericUtils.IDR+" "+cashBalanceFigure);

                    SnackBarUtil.snackBarForWarningCreate(getView(),statement, Snackbar.LENGTH_SHORT).show();
                    viewHolder.mSubmitTransferAmountEditText.setText(cashBalanceFigure);
                }else{
                    String amountFigure = NumericUtils.formatCurrency(NumericUtils.IDR, amount, false);
                    viewHolder.mSubmitTransferAmountEditText.setText(amountFigure);
                }
                viewHolder.mSubmitTransferSubmitButton.setEnabled(true);
            }else{
                SnackBarUtil.snackBarForInfoCreate(getView(), mAmountEmptyStatement,
                        Snackbar.LENGTH_SHORT).show();

                viewHolder.mSubmitTransferSubmitButton.setEnabled(false);
            }
        }
    }

    public void transferAmount(long amount){
        //amount is confirmed here

    }
}
