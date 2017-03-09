package com.crowdo.p2pconnect.view.fragments;

/**
 * Created by cwdsg05 on 8/3/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;

public class LoginFragment extends Fragment{

    public static final String TAG_LOGIN_FRAGMENT = "LOGIN_FRAGMENT_TAG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);




        return rootView;
    }
}
