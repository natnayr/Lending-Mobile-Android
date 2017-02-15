package com.crowdo.p2pmobile.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.databinding.FragmentLearningCenterBinding;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.helpers.LearningCenterUtils;
import com.crowdo.p2pmobile.helpers.SharedPreferencesUtils;
import com.crowdo.p2pmobile.helpers.SoftInputHelper;
import com.crowdo.p2pmobile.model.LearningCenter;

import butterknife.BindString;
import butterknife.BindView;
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
    private RealmResults<LearningCenter> mGeneralResults;
    private RealmResults<LearningCenter> mInvestorResults;
    private RealmResults<LearningCenter> mBorrowerResults;
    private LearningCenterAdapter mGeneralAdapter;
    private LearningCenterAdapter mInvestorAdapter;
    private LearningCenterAdapter mBorrowerAdapter;

    @BindString(R.string.learning_center_categories_label_general) String mGeneralLabel;
    @BindString(R.string.learning_center_categories_label_investor) String mInvestorLabel;
    @BindString(R.string.learning_center_categories_label_borrower) String mBorrowerLabel;
    @BindView(R.id.learning_center_root_layout) RelativeLayout mRootRelativeLayout;
    @BindView(R.id.learning_center_input_edittext_search) EditText mLearningCenterSearchInput;

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
        mGeneralResults = realm.where(LearningCenter.class)
                .equalTo("language", lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_GENERAL)
                .findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        mInvestorResults = realm.where(LearningCenter.class)
                .equalTo("language", lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_INVESTOR)
                .findAll();
        realm.commitTransaction();

        realm.beginTransaction();
        mBorrowerResults = realm.where(LearningCenter.class)
                .equalTo("language", lang)
                .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_BORROWER)
                .findAll();
        realm.commitTransaction();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLearningCenterBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_learning_center,
                container, false);

        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        SoftInputHelper.setupUI(view, getActivity(), new EditText[]{mLearningCenterSearchInput});
        mLearningCenterSearchInput.addTextChangedListener(new SearchEditTextWatcher());


        //General RecycleView setAdapter
        RecyclerView.LayoutManager generalLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewGeneral.setLayoutManager(generalLayoutManager);
        mGeneralAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mGeneralResults));
        binding.learningCenterRecycleViewGeneral.setAdapter(mGeneralAdapter);

        //Investor RecycleView setAdapter
        RecyclerView.LayoutManager investorLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewInvestor.setLayoutManager(investorLayoutManager);
        mInvestorAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mInvestorResults));
        binding.learningCenterRecycleViewInvestor.setAdapter(mInvestorAdapter);

        //Borrower RecycleView setAdapter
        RecyclerView.LayoutManager borrowerLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewBorrowers.setLayoutManager(borrowerLayoutManager);
        mBorrowerAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mBorrowerResults));
        binding.learningCenterRecycleViewBorrowers.setAdapter(mBorrowerAdapter);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onDestroy() {
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

    private class SearchEditTextWatcher implements TextWatcher{
        String searchStr;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                mLearningCenterSearchInput.removeTextChangedListener(this);
                searchStr = mLearningCenterSearchInput.getText().toString();

                mGeneralAdapter.search(searchStr);
                mInvestorAdapter.search(searchStr);
                mBorrowerAdapter.search(searchStr);

            }catch(IndexOutOfBoundsException e){
                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                e.printStackTrace();
            }finally{
                mLearningCenterSearchInput.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
