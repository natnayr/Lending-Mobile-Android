package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.viewholders.RegisterViewHolder;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 15/3/17.
 */

public class RegisterFragment extends Fragment{

    private static final String LOG_TAG = RegisterFragment.class.getSimpleName();
    public static final String REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT_TAG";
    private RegisterViewHolder viewHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        viewHolder = new RegisterViewHolder(rootView, getActivity());
        viewHolder.init();

        viewHolder.mExitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                submit();
            }
        });

        return rootView;
    }

    private void submit(){
        final String inputEmail = viewHolder.mRegisterEmailEditText.getText().toString().toLowerCase().trim();
        final String inputName = viewHolder.mRegisterNameEditText.getText().toString().trim();
        final String inputPassword = viewHolder.mRegisterPasswordEmailText.getText().toString();
        final String inputConfirmPassword = viewHolder.mRegisterConfirmPasswdEditText.getText().toString();
        //fix emailbox for user
        if(!inputEmail.equals(viewHolder.mRegisterEmailEditText.getText().toString())){
            viewHolder.mRegisterEmailEditText.setText(inputEmail);
        }
    }


}
