package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 21/3/17.
 */

public class LoginViewHolder {

    @BindView(R.id.auth_login_email_edittext) public AppCompatEditText mLoginEmailEditText;
    @BindView(R.id.auth_login_password_edittext) public AppCompatEditText mLoginPasswdEditText;
    @BindView(R.id.auth_login_exit_btn) public LinearLayout mLoginExitButton;
    @BindView(R.id.auth_login_exit_image_view) ImageView mLoginExitImageView;
    @BindView(R.id.auth_login_submit_btn) public LinearLayout mLoginSubmitButton;
    @BindView(R.id.auth_login_submit_btn_text) TextView mLoginSubmitTextView;
    @BindView(R.id.auth_login_redirect_to_register_link) public TextView mLoginRedirectToRegisterTextView;
    @BindView(R.id.auth_login_lang_spinner) public MaterialSpinner mLoginLangSpinner;

    private static final String LOG_TAG = LoginViewHolder.class.getSimpleName();
    private Context mContext;

    public LoginViewHolder(View view, Context mContext) {
        this.mContext = mContext;
        ButterKnife.bind(this, view);
    }

    public void initView(){
        mLoginSubmitButton.setEnabled(false); //init
        mLoginSubmitTextView.setEnabled(false);
        mLoginEmailEditText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_account)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mLoginPasswdEditText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_lock)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mLoginExitImageView.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_window_close)
                        .sizeRes(R.dimen.auth_btn_drawable_close_icon_size));

        mLoginEmailEditText.addTextChangedListener(
                new SubmitEnablerTextWatcher(
                        new EditText[]{mLoginEmailEditText, mLoginPasswdEditText},
                        new View[]{mLoginSubmitButton, mLoginSubmitTextView}));

        mLoginPasswdEditText.addTextChangedListener(
                new SubmitEnablerTextWatcher(
                        new EditText[]{mLoginEmailEditText, mLoginPasswdEditText},
                        new View[]{mLoginSubmitButton, mLoginSubmitTextView}));

        List<String> languageSet = new LinkedList<>(Arrays.asList("English", "Bahasa"));
        mLoginLangSpinner.setItems(languageSet);


        mLoginLangSpinner.setSelectedIndex(0);
        String localeVal = LocaleHelper.getLanguage(mContext);
        if(localeVal.equals(ConstantVariables.APP_LANG_ID)){
            mLoginLangSpinner.setSelectedIndex(1);
        }


        mLoginLangSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(mContext, item + " selected", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private class SubmitEnablerTextWatcher implements TextWatcher {
        View[] viewsToEnable;
        EditText[] editTextList;

        public SubmitEnablerTextWatcher(EditText[] editTextList, View[] viewsToEnable) {
            this.viewsToEnable = viewsToEnable;
            this.editTextList = editTextList;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for(EditText editText : editTextList){
                if(editText.getText().length() <= 0 ){
                    for(View view : viewsToEnable){
                        view.setEnabled(false);
                    }
                    return;
                }
            }
            for(View view : viewsToEnable){
                //all pass
                view.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
