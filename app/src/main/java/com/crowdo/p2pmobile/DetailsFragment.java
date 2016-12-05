package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crowdo.p2pmobile.custom_ui.GoalProgressBar;
import com.crowdo.p2pmobile.helper.FontManager;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    @BindView(R.id.loan_detail_progress_bar) GoalProgressBar mProgressBar;
    @BindView(R.id.loan_detail_collateral_icon_container) TextView mSecurityIcon;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, rootView);

        mSecurityIcon.setText(R.string.fa_shield);
        Typeface iconFont = FontManager.getTypeface(getActivity(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(mSecurityIcon, iconFont);

        return rootView;
    }

    @Override
    public void onResume() {
        mProgressBar.setProgress(75);
        super.onResume();
    }
}
