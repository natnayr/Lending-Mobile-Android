package com.crowdo.p2pconnect.view.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.NumericUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.model.response.WithdrawSubmitResponse;
import com.crowdo.p2pconnect.support.InvestorAccreditationReaction;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.view.activities.WithdrawActivity;
import com.crowdo.p2pconnect.viewholders.WithdrawSubmitViewHolder;

import butterknife.BindString;
import butterknife.ButterKnife;
import de.mateware.snacky.Snacky;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitFragment extends Fragment {

    private WithdrawSubmitViewHolder viewHolder;
    private long avalibleCashBalance;

    @BindString(R.string.withdraw_invalid_investor_label) String mInvestorInvalidLabel;
    @BindString(R.string.withdraw_invalid_investor_button_label) String mInvestorInvalidButtonLabel;
    @BindString(R.string.withdraw_submit_complete) String mSubmitSuccessLabel;
    @BindString(R.string.withdraw_submit_enter_amount_warning_above_amount) String mAmountTooHighWarning;
    @BindString(R.string.withdraw_submit_enter_amount_warning_empty_entry) String mAmountEmptyStatement;

    private Disposable disposablePostRequestWithdraw;
    private MaterialDialog waitForUpload;
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
                String amountInput = viewHolder.mSubmitTransferAmountEditText
                        .getText().toString().trim().replaceAll("[^\\d.]", "");
                if("".equals(amountInput) || Long.parseLong(amountInput) <= 0){
                    SnackBarUtil.snackBarForInfoCreate(getView(), mAmountEmptyStatement,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                transferAmount(amountInput);
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

                    SnackBarUtil.snackBarForWarningCreate(getView(),statement, Snackbar.LENGTH_LONG).show();
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

    public void transferAmount(String amountInput){
        //amount is confirmed here

        waitForUpload = new MaterialDialog.Builder(getActivity())
                .content(R.string.wait_message)
                .progress(true, 0)
                .cancelable(false)
                .show();

        final WalletClient walletClient = WalletClient.getInstance(getActivity());

        walletClient.postRequestWithdraw(amountInput,
                ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<WithdrawSubmitResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposablePostRequestWithdraw = d;
                    }

                    @Override
                    public void onNext(Response<WithdrawSubmitResponse> response) {
                        if(response.isSuccessful()){
                            //reset fields
                            viewHolder.mSubmitTransferAmountEditText.setText("");
                            viewHolder.mSubmitTransferSubmitButton.setEnabled(false);

                            Spanned topUpStatement;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                topUpStatement = Html.fromHtml(mSubmitSuccessLabel,
                                        Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                topUpStatement = Html.fromHtml(mSubmitSuccessLabel);
                            }

                            Snacky.builder().setView(getView())
                                    .setText(topUpStatement)
                                    .setDuration(Snackbar.LENGTH_LONG)
                                    .success().show();
                        }else{
                            if (HTTPResponseUtils.check4xxClientError(response.code())){
                                if(ConstantVariables.HTTP_INVESTOR_FAILED_ACCREDITATION == response.code()){
                                    //option to signup given
                                    Snackbar investorInvalidSnackbar = InvestorAccreditationReaction
                                            .failedInvestorAcreditationSnackbar(
                                                    mInvestorInvalidLabel, mInvestorInvalidButtonLabel,
                                                    getView(), getActivity());
                                    investorInvalidSnackbar.show();
                                }else {
                                    //all other 4xx codes
                                    String serverErrorMessage = HTTPResponseUtils
                                            .errorServerResponseConvert(walletClient,
                                                    response.errorBody());

                                    SnackBarUtil.snackBarForWarningCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                        if(waitForUpload.isShowing()) waitForUpload.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP onComplete");
                        if(waitForUpload.isShowing()) waitForUpload.dismiss();
                        ((WithdrawActivity) getActivity()).refreshAllFragments();
                        ((WithdrawActivity) getActivity()).getMemberDetails(); //re-evatuate totals
                    }
                });

    }

    public void refreshFragment(){
        Log.d(LOG_TAG, "APP refreshFragment called");
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.detach(WithdrawSubmitFragment.this);
        ftr.attach(WithdrawSubmitFragment.this);
        ftr.commit();
    }

    @Override
    public void onPause() {
        if(disposablePostRequestWithdraw != null){
            if(disposablePostRequestWithdraw.isDisposed()){
                disposablePostRequestWithdraw.dispose();
            }
        }

        super.onPause();
    }
}
