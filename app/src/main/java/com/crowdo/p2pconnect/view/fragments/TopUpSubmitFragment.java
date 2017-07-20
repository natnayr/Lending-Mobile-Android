package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.view.activities.TopUpActivity;
import com.crowdo.p2pconnect.viewholders.TopUpSubmitViewHolder;

import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpSubmitFragment extends Fragment{

    private TopUpSubmitViewHolder viewHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_up_submit, container, false);

        viewHolder = new TopUpSubmitViewHolder(rootView, getActivity());
        viewHolder.initView();

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{viewHolder.mSubmitPaymentReferenceEditText});
        viewHolder.mSubmitPaymentReferenceEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            SoftInputHelper.hideSoftKeyboardOfView(view,view);
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        viewHolder.mSubmitPaymentReferenceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ((TopUpActivity) getActivity()).mTopUpBalanceExpandableLayout.collapse();
                }
            }
        });


        return rootView;
    }
}
