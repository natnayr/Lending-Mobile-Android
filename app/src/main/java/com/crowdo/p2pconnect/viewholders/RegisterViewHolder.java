package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 21/3/17.
 */
public class RegisterViewHolder {

    @BindView(R.id.auth_register_name_edittext) AppCompatEditText mRegisterNameEditText;
    @BindView(R.id.auth_register_email_edittext) AppCompatEditText mRegisterEmailEditText;
    @BindView(R.id.auth_register_password_edittext) AppCompatEditText mRegisterPasswordEmailText;
    @BindView(R.id.auth_register_confirm_password_edittext) AppCompatEditText mRegisterConfirmPasswdEditText;
    @BindView(R.id.auth_register_exit_btn) public ImageButton mExitImageButton;
    @BindView(R.id.auth_register_submit_btn) public LinearLayout mSubmitButton;

    private Context mContext;
    private static final String LOG_TAG = RegisterViewHolder.class.getSimpleName();

    public RegisterViewHolder(View view, Context mContext){
        ButterKnife.bind(this, view);
        this.mContext = mContext;
    }

    public void init(){
        mSubmitButton.setEnabled(false);

        //To set icons
        mRegisterNameEditText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_account)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterEmailEditText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_email_outline)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterPasswordEmailText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_key)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterConfirmPasswdEditText.setCompoundDrawables(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_key_plus)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);


        //To set submit button behavior
        mRegisterNameEditText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                mSubmitButton));

        mRegisterEmailEditText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                mSubmitButton));

        mRegisterPasswordEmailText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                mSubmitButton));

        mRegisterConfirmPasswdEditText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                mSubmitButton));

        mExitImageButton.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_close)
                        .sizeRes(R.dimen.auth_btn_drawable_close_icon_size));
    }


    class SubmitEnablerTextWatcher implements TextWatcher {
        View view;
        EditText[] editTextList;

        public SubmitEnablerTextWatcher(EditText[] editTextList, View view) {
            this.view = view;
            this.editTextList = editTextList;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for(EditText editText : editTextList){
                if(editText.getText().length() <= 0 ){
                    view.setEnabled(false);
                    return;
                }
            }
            view.setEnabled(true); //all pass
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
