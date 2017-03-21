package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.crowdo.p2pconnect.R;
import com.f2prateek.dart.Dart;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 8/3/17.
 */

public class LoginFragment extends Fragment{

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.auth_login_email_edittext) AppCompatEditText mEmailEditText;
    @BindView(R.id.auth_login_password_edittext) AppCompatEditText mPasswdEditText;
    @BindView(R.id.auth_login_exit_btn) ImageButton mExitImageButton;
    @BindDimen(R.dimen.auth_field_drawable_padding) int mDrawablePadding;

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

        mEmailEditText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_account)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mPasswdEditText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_lock)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mExitImageButton.setImageDrawable(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_close)
                        .sizeRes(R.dimen.auth_btn_drawable_close_icon_size));

        mExitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }
}
