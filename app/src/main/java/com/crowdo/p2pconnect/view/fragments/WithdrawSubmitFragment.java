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
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.viewholders.WithdrawSubmitViewHolder;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitFragment extends Fragment {

    private WithdrawSubmitViewHolder viewHolder;
    private long mAvailableCashBalance;

    @BindString(R.string.withdraw_submit_enter_amount_warning_above_amount) String mAmountTooHighWarning;

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

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{viewHolder.mSubmitAmountEditText});
        viewHolder.mSubmitAmountEditText.setOnKeyListener(new View.OnKeyListener() {
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

        viewHolder.mSubmitAmountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    formatAndCheckEntry();
                }
            }
        });

        return rootView;
    }

    private void formatAndCheckEntry(){
        if(viewHolder != null) {
            String amountInput = viewHolder.mSubmitAmountEditText
                    .getText().toString().trim().replaceAll("[^\\d.]", "");

            if(!"".equals(amountInput)) {
                double amount = Double.parseDouble(amountInput);

                if(amount > this.mAvailableCashBalance){
                    String cashBalanceFigure = NumericUtils.formatCurrency(NumericUtils.IDR, (double) this.mAvailableCashBalance, false);
                    String statement = String.format(mAmountTooHighWarning, NumericUtils.IDR+" "+cashBalanceFigure);

                    SnackBarUtil.snackBarForWarningCreate(getView(),statement, Snackbar.LENGTH_SHORT).show();
                    viewHolder.mSubmitAmountEditText.setText(cashBalanceFigure);
                }else{
                    String amountFigure = NumericUtils.formatCurrency(NumericUtils.IDR, amount, false);
                    viewHolder.mSubmitAmountEditText.setText(amountFigure);
                }
            }
        }
    }

    public void setMemberInfo(BankInfo bankInfo, long cashBalance){
        viewHolder.populateView(bankInfo);
        this.mAvailableCashBalance = cashBalance;
    }
}
