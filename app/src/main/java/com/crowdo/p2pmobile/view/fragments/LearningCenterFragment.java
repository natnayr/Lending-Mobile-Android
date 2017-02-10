package com.crowdo.p2pmobile.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.helpers.LearningCenterUtils;
import com.crowdo.p2pmobile.helpers.SharedPreferencesUtils;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenterFragment extends Fragment{

    public static final String LOG_TAG = LearningCenterFragment.class.getSimpleName();
    private Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading..");
        progress.setCancelable(false);
        progress.show();

        initLoadLearningDB(getActivity());

        progress.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learning_center, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }

    private void initLoadLearningDB(final Context context){
        boolean loadedLearningCenterDB = SharedPreferencesUtils
                .getSharedPrefBool(context,
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);

        if(!loadedLearningCenterDB){
            new LearningCenterUtils().populateData(context);
        }
    }
}
