package com.crowdo.p2pmobile.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.databinding.FragmentLearningCenterBinding;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.helpers.LearningCenterUtils;
import com.crowdo.p2pmobile.helpers.SharedPreferencesUtils;
import com.crowdo.p2pmobile.model.LearningCenter;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenterFragment extends Fragment{

    public static final String LOG_TAG = LearningCenterFragment.class.getSimpleName();
    private Realm realm;
    private String lang;
    private RealmResults<LearningCenter> generalResults;
    private RealmResults<LearningCenter> investorResults;
    private RealmResults<LearningCenter> borrowerResults;
    @BindString(R.string.learning_center_categories_label_general) String mGeneralLabel;
    @BindString(R.string.learning_center_categories_label_investor) String mInvestorLabel;
    @BindString(R.string.learning_center_categories_label_borrower) String mBorrowerLabel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        //load csv db
        initLoadLearningDB(getActivity());

        if(ConstantVariables.APP_LANG_SET.equals(ConstantVariables.LEARNING_CENTER_DB_ID)){
            this.lang = ConstantVariables.LEARNING_CENTER_DB_ID;
        }else{
            //default english
            this.lang = ConstantVariables.LEARNING_CENTER_DB_EN;
        }

        //do realm call db transactions
        realm.beginTransaction();
        generalResults = realm.where(LearningCenter.class)
                .equalTo("language", this.lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_GENERAL)
                .findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        investorResults = realm.where(LearningCenter.class)
                .equalTo("language", this.lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_INVESTOR)
                .findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        borrowerResults = realm.where(LearningCenter.class)
                .equalTo("language", this.lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_BORROWER)
                .findAll();
        realm.commitTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learning_center, container, false);
        ButterKnife.bind(this, rootView);

        FragmentLearningCenterBinding binding = DataBindingUtil.setContentView(getActivity(),
                R.layout.fragment_learning_center);

        //General RecycleView setAdapter
        RecyclerView.LayoutManager generalLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewGeneral.setLayoutManager(generalLayoutManager);
        LearningCenterAdapter generalAdapter = new LearningCenterAdapter(getActivity(), generalResults);
        binding.learningCenterRecycleViewGeneral.setAdapter(generalAdapter);

        //Investor RecycleView setAdapter
        RecyclerView.LayoutManager investorLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewInvestor.setLayoutManager(investorLayoutManager);
        LearningCenterAdapter investorAdapter = new LearningCenterAdapter(getActivity(), investorResults);
        binding.learningCenterRecycleViewInvestor.setAdapter(investorAdapter);

        //Borrower RecycleView setAdapter
        RecyclerView.LayoutManager borrowerLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewBorrowers.setLayoutManager(borrowerLayoutManager);
        LearningCenterAdapter borrowerAdapter = new LearningCenterAdapter(getActivity(), borrowerResults);
        binding.learningCenterRecycleViewBorrowers.setAdapter(borrowerAdapter);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    private void initLoadLearningDB(final Context context){
        boolean loadedLearningCenterDB = SharedPreferencesUtils
                .getSharedPrefBool(context,
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);

        if(!loadedLearningCenterDB){
            new LearningCenterUtils().populateData(context, realm);
        }
    }
}
