package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.viewholders.LoginViewHolder;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 8/3/17.
 */

public class LoginFragment extends Fragment{

    @BindColor(R.color.color_snackbar_teal_A200) int mColorSnackbarText;
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    private LoginViewHolder viewHolder;
    private AccountManager mAccountManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new LoginViewHolder(rootView, getActivity());
        viewHolder.initView();

        viewHolder.mExitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        return rootView;
    }

    private void submit(){
        String inputEmail = viewHolder.mEmailEditText.getText().toString().toLowerCase().trim();

        if(!RegexValidationUtil.isValidEmailID(inputEmail)){
            final Snackbar snack = SnackBarUtil.snackBarCreate(getView(),
                    "User Email in Incorrect Format", mColorSnackbarText);
            snack.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snack.dismiss();
                }
            });
            snack.show();
            return;
        }


    }


}
