package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 27/6/17.
 */

public class CheckoutConfirmFragment extends Fragment{

    @BindView(R.id.toolbar_custom_left_btn) LinearLayout mConfirmCloseButton;
    @BindView(R.id.toolbar_custom_left_icon) ImageView mConfirmCloseIcon;

    @BindView(R.id.checkout_confirm_success_logo) ImageView mConfirmSuccessLogo;
    @BindView(R.id.checkout_confirm_continue_button) LinearLayout mConfirmContinueButton;

    private Context mContext;

    public CheckoutConfirmFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_checkout_confirm, container, false);
        ButterKnife.bind(this, rootView);

        mContext = getActivity();

        mConfirmSuccessLogo.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_check_circle)
                .colorRes(R.color.color_green_500)
                .sizeRes(R.dimen.checkout_confirm_success_logo_size));

        mConfirmCloseIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_close)
                .colorRes(R.color.color_secondary_text)
                .sizeRes(R.dimen.checkout_summary_success_close_icon_size));

        mConfirmCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mConfirmContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

}
