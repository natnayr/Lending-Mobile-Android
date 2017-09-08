package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.oauth.SocialAuthConstant;
import com.facebook.login.widget.LoginButton;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 21/3/17.
 */
public class RegisterViewHolder {

    @BindView(R.id.auth_register_name_edittext) public AppCompatEditText mRegisterNameEditText;
    @BindView(R.id.auth_register_email_edittext) public AppCompatEditText mRegisterEmailEditText;
    @BindView(R.id.auth_register_password_edittext) public AppCompatEditText mRegisterPasswordEmailText;
    @BindView(R.id.auth_register_confirm_password_edittext) public AppCompatEditText mRegisterConfirmPasswdEditText;
    @BindView(R.id.auth_register_exit_btn) public LinearLayout mRegisterExitButton;
    @BindView(R.id.auth_register_exit_image_view) ImageView mRegisterExitImageView;
    @BindView(R.id.auth_register_submit_btn) public LinearLayout mRegisterSubmitButton;
    @BindView(R.id.auth_register_submit_text) public TextView mRegisterSubmitTextView;
    @BindView(R.id.auth_register_redirect_to_login_link) public TextView mRegisterRedirectToLoginTextView;

    @BindView(R.id.auth_register_fb_button_shell_icon) ImageView mRegisterFBButtonIcon;
    @BindView(R.id.auth_register_fb_button_shell) LinearLayout mRegisterFBButtonShell;
    @BindView(R.id.auth_register_fb_button) public LoginButton mRegisterFBButton;
    @BindView(R.id.auth_register_linkedin_button_icon) ImageView mRegisterLinkedinButtonIcon;
    @BindView(R.id.auth_register_linkedin_button) public LinearLayout mRegisterLinkedinButton;

    @BindString(R.string.auth_social_wait) String mRegisterRequestWait;

    private Context mContext;
    private static final String LOG_TAG = RegisterViewHolder.class.getSimpleName();

    public RegisterViewHolder(View view, Context mContext){
        ButterKnife.bind(this, view);
        this.mContext = mContext;
    }

    public void init(){
        mRegisterSubmitButton.setEnabled(false);
        mRegisterSubmitTextView.setEnabled(false);

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
                new View[]{mRegisterSubmitButton, mRegisterSubmitTextView}));

        mRegisterEmailEditText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                new View[]{mRegisterSubmitButton, mRegisterSubmitTextView}));

        mRegisterPasswordEmailText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                new View[]{mRegisterSubmitButton, mRegisterSubmitTextView}));

        mRegisterConfirmPasswdEditText.addTextChangedListener(new SubmitEnablerTextWatcher(
                new EditText[]{mRegisterNameEditText, mRegisterEmailEditText,
                        mRegisterPasswordEmailText, mRegisterConfirmPasswdEditText},
                new View[]{mRegisterSubmitButton, mRegisterSubmitTextView}));

        mRegisterExitImageView.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_window_close)
                        .sizeRes(R.dimen.auth_btn_drawable_close_icon_size));

        mRegisterFBButtonIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_facebook)
                        .colorRes(R.color.color_icons_text)
                        .sizeRes(R.dimen.auth_social_btn_icon_size));

        mRegisterLinkedinButtonIcon.setImageDrawable(
                new IconicsDrawable(mContext)
                        .icon(CommunityMaterial.Icon.cmd_linkedin)
                        .colorRes(R.color.color_icons_text)
                        .sizeRes(R.dimen.auth_social_btn_icon_size));

        //request permission for
        mRegisterFBButton.setReadPermissions(SocialAuthConstant.AUTH_FACEBOOK_READ_PERMISSIONS);

        mRegisterFBButtonShell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == mRegisterFBButtonShell){
                    Toast.makeText(mContext, mRegisterRequestWait, Toast.LENGTH_LONG).show();
                    mRegisterFBButton.performClick();
                }
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
