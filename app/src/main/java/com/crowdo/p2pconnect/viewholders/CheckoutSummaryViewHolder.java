package com.crowdo.p2pconnect.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crowdo.p2pconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 23/5/17.
 */

public class CheckoutSummaryViewHolder {

    private static final String LOG_TAG = CheckoutSummaryViewHolder.class.getSimpleName();

    @BindView(R.id.checkout_summary_expand_btn) ImageView mSummaryExpandBtn;
    @BindView(R.id.checkout_summary_expand_icon) LinearLayout mSummaryButton;

    public CheckoutSummaryViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
