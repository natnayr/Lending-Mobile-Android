package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 15/3/17.
 */

public class RegisterFragment extends Fragment{

    private static final String LOG_TAG = RegisterFragment.class.getSimpleName();
    @BindView(R.id.auth_register_name_edittext) AppCompatEditText mRegisterNameEditText;
    @BindView(R.id.auth_register_email_edittext) AppCompatEditText mRegisterEmailEditText;
    @BindView(R.id.auth_register_password_edittext) AppCompatEditText mRegisterPasswordEmailText;
    @BindView(R.id.auth_register_confirm_password_edittext) AppCompatEditText mRegisterConfirmPasswdEditText;

    @BindDimen(R.dimen.auth_field_drawable_padding) int mDrawablePadding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, rootView);

        mRegisterNameEditText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_account)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterEmailEditText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_email_outline)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterPasswordEmailText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_key)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        mRegisterConfirmPasswdEditText.setCompoundDrawables(
                new IconicsDrawable(getActivity())
                        .icon(CommunityMaterial.Icon.cmd_key_plus)
                        .colorRes(R.color.color_secondary_text)
                        .sizeRes(R.dimen.auth_field_drawable_icon_size),
                null, null, null);

        return rootView;
    }
}
