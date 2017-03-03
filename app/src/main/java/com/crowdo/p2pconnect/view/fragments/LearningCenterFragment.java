package com.crowdo.p2pconnect.view.fragments;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.R2;
import com.crowdo.p2pconnect.databinding.FragmentLearningCenterBinding;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.LearningCenter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by cwdsg05 on 9/2/17.
 */
public class LearningCenterFragment extends Fragment{

    @BindString(R2.string.learning_center_categories_label_general) String mGeneralLabel;
    @BindString(R2.string.learning_center_categories_label_investor) String mInvestorLabel;
    @BindString(R2.string.learning_center_categories_label_borrower) String mBorrowerLabel;
    @BindString(R2.string.learning_center_categories_item_count_label) String mItemCountTailLabel;
    @BindView(R2.id.learning_center_input_edittext_search) EditText mSearchInput;
    @BindView(R2.id.learning_center_item_count_general) TextView mGeneralItemCountLabel;
    @BindView(R2.id.learning_center_item_count_investor) TextView mInvestorItemCountLabel;
    @BindView(R2.id.learning_center_item_count_borrower) TextView mBorrowerItemCountLabel;

    public static final String LOG_TAG = LearningCenterFragment.class.getSimpleName();
    private Realm realm;
    private String lang;
    private RealmResults<LearningCenter> mGeneralResults;
    private RealmResults<LearningCenter> mInvestorResults;
    private RealmResults<LearningCenter> mBorrowerResults;
    private LearningCenterAdapter mGeneralAdapter;
    private LearningCenterAdapter mInvestorAdapter;
    private LearningCenterAdapter mBorrowerAdapter;
    private int mGeneralItemCount, mInvestorItemCount, mBorrowerItemCount;

    private ProgressDialog progress;
    private FragmentLearningCenterBinding binding;
    private View rootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        this.lang = LocaleHelper.getLanguage(getActivity());

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Please wait while loading..");
        progress.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_learning_center,
                container, false);

        rootView = binding.getRoot();
        ButterKnife.bind(this, rootView);

        //Set RecyclerViews
        RecyclerView.LayoutManager generalLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewGeneral.setLayoutManager(generalLayoutManager);

        RecyclerView.LayoutManager investorLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewInvestor.setLayoutManager(investorLayoutManager);

        RecyclerView.LayoutManager borrowerLayoutManager = new LinearLayoutManager(getActivity());
        binding.learningCenterRecycleViewBorrowers.setLayoutManager(borrowerLayoutManager);

        binding.learningCenterRelativeLayoutGeneral.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.learningCenterExpandGeneral.isExpanded()){
                    createRotateAnimator(binding.learningCenterTriangleGeneral, 180f, 0f).start();
                }else{
                    createRotateAnimator(binding.learningCenterTriangleGeneral, 0f, 180f).start();
                }
                binding.learningCenterExpandGeneral.toggle();
            }
        });

        binding.learningCenterRelativeLayoutInvestor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.learningCenterExpandInvestor.isExpanded()){
                    createRotateAnimator(binding.learningCenterTriangleInvestor, 180f, 0f).start();
                }else{
                    createRotateAnimator(binding.learningCenterTriangleInvestor, 0f, 180f).start();
                }
                binding.learningCenterExpandInvestor.toggle();
            }
        });

        binding.learningCenterRelativeLayoutBorrower.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.learningCenterExpandBorrowers.isExpanded()){
                    createRotateAnimator(binding.learningCenterTriangleBorrower, 180f, 0f).start();
                }else{
                    createRotateAnimator(binding.learningCenterTriangleBorrower, 0f, 180f).start();
                }
                binding.learningCenterExpandBorrowers.toggle();
            }
        });

        binding.learningCenterRelativeLayoutFees.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.learningCenterExpandFees.isExpanded()){
                    createRotateAnimator(binding.learningCenterTriangleFees, 180f, 0f).start();
                }else{
                    createRotateAnimator(binding.learningCenterTriangleFees, 0f, 180f).start();
                }
                binding.learningCenterExpandFees.toggle();
            }
        });

        binding.learningCenterRelativeLayoutLoanDocuments.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.learningCenterExpandLoanDocuments.isExpanded()){
                    createRotateAnimator(binding.learningCenterTriangleLoanDocuments, 180f, 0f).start();
                }else{
                    createRotateAnimator(binding.learningCenterTriangleLoanDocuments, 0f, 180f).start();
                }
                binding.learningCenterExpandLoanDocuments.toggle();
            }
        });

        //Long process
        populateData();

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{mSearchInput});
        mSearchInput.addTextChangedListener(new SearchEditTextWatcher());

        mSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            mSearchInput.clearFocus();
                            InputMethodManager imm = (InputMethodManager)
                                    v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        return rootView;
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
        realm.close();
        super.onDestroy();
    }


    //Load Data from CSV
    public void populateData(){
        //show progress dialog
//        progress.show();
        //on another thread
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                boolean loadedLearningCenterDB = SharedPreferencesUtils.getSharedPrefBool(getActivity(),
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);

                if(!loadedLearningCenterDB) {

                    final List<String> csvCategories = new ArrayList<String>();
                    csvCategories.add(ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_GENERAL);
                    csvCategories.add(ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_INVESTOR);
                    csvCategories.add(ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_BORROWER);

                    //init counters
                    int[] counterEn = {1, 1, 1};
                    int[] counterId = {1, 1, 1};

                    try {
                        InputStreamReader isr = new InputStreamReader(getActivity().getAssets()
                                .open(ConstantVariables.LEARNING_CENTER_CSV_FILE_LOCATION));
                        BufferedReader reader = new BufferedReader(isr);
                        CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);
                        for (final CSVRecord rec : parser) {
                            if (!rec.get(0).equals("") && !rec.get(1).equals("") && !rec.get(2).equals("")
                                    && !rec.get(4).equals("") && !rec.get(5).equals("")) {
                                if (csvCategories.contains(rec.get(0))) {
                                    int csvIdx = csvCategories.indexOf(rec.get(0));
                                    LearningCenter enLearningCenter = realm.createObject(LearningCenter.class);
                                    enLearningCenter.setLanguage(ConstantVariables.APP_LANG_EN);
                                    enLearningCenter.setCategory(rec.get(0));
                                    enLearningCenter.setQuestion(rec.get(1));
                                    enLearningCenter.setAnswer(rec.get(2));
                                    enLearningCenter.setIndex(Integer.toString(counterEn[csvIdx]) + ". ");
                                    counterEn[csvIdx]++;

                                    LearningCenter idLearningCenter = realm.createObject(LearningCenter.class);
                                    idLearningCenter.setLanguage(ConstantVariables.APP_LANG_IN);
                                    idLearningCenter.setCategory(rec.get(0));
                                    idLearningCenter.setQuestion(rec.get(4));
                                    idLearningCenter.setAnswer(rec.get(5));
                                    idLearningCenter.setIndex(Integer.toString(counterId[csvIdx]) + ". ");
                                    counterId[csvIdx]++;
                                }
                            }
                        }
                        isr.close();
                    } catch (IOException ioe) {
                        Log.e(LOG_TAG, "ERROR: " + ioe.getMessage(), ioe);
                    }
                }

                Log.d(LOG_TAG, "APP: loadedLearningCenterDB SharePref Boolean = " + loadedLearningCenterDB);

                //do realm call db transactions
                mGeneralResults = realm.where(LearningCenter.class)
                        .equalTo("language", lang)
                        .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_GENERAL)
                        .findAll();

                mInvestorResults = realm.where(LearningCenter.class)
                        .equalTo("language", lang)
                        .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_INVESTOR)
                        .findAll();

                mBorrowerResults = realm.where(LearningCenter.class)
                        .equalTo("language", lang)
                        .equalTo("category", ConstantVariables.LEARNING_CENTER_DB_CATEGORY_KEY_BORROWER)
                        .findAll();

                //Init RecycleView setAdapter
                mGeneralAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mGeneralResults));
                mInvestorAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mInvestorResults));
                mBorrowerAdapter = new LearningCenterAdapter(getActivity(), realm.copyFromRealm(mBorrowerResults));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                progress.dismiss();

                Log.d(LOG_TAG, "APP: Realm database is done processing from CSV");
                if(getActivity() != null) {
                    SharedPreferencesUtils.setSharePrefBool(getActivity(),
                            ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, true);

                    //set adapters
                    binding.learningCenterRecycleViewGeneral.setAdapter(mGeneralAdapter);
                    binding.learningCenterRecycleViewInvestor.setAdapter(mInvestorAdapter);
                    binding.learningCenterRecycleViewBorrowers.setAdapter(mBorrowerAdapter);

                    mGeneralItemCount = mGeneralAdapter.getItemCount();
                    mInvestorItemCount = mInvestorAdapter.getItemCount();
                    mBorrowerItemCount = mBorrowerAdapter.getItemCount();

                    mGeneralItemCountLabel.setText(String.valueOf(mGeneralItemCount) + mItemCountTailLabel);
                    mInvestorItemCountLabel.setText(String.valueOf(mInvestorItemCount) + mItemCountTailLabel);
                    mBorrowerItemCountLabel.setText(String.valueOf(mBorrowerItemCount) + mItemCountTailLabel);
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(LOG_TAG, "ERROR: " + error.getMessage(), error);
                SharedPreferencesUtils.setSharePrefBool(getActivity(),
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);
                Toast.makeText(getActivity(), "Sry, I looks like there was an error loading " +
                        "the learning center information..", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private class SearchEditTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s != null) {
                try {
                    mSearchInput.removeTextChangedListener(this);
                    String searchStr = s.toString();
                    if (mGeneralAdapter != null && mInvestorAdapter != null
                            && mBorrowerAdapter != null) {
                        mGeneralAdapter.search(searchStr);
                        mInvestorAdapter.search(searchStr);
                        mBorrowerAdapter.search(searchStr);

                        mGeneralItemCount = mGeneralAdapter.getItemCount();
                        mInvestorItemCount = mInvestorAdapter.getItemCount();
                        mBorrowerItemCount = mBorrowerAdapter.getItemCount();

                        mGeneralItemCountLabel.setText(String.valueOf(mGeneralItemCount) + mItemCountTailLabel);
                        mInvestorItemCountLabel.setText(String.valueOf(mInvestorItemCount) + mItemCountTailLabel);
                        mBorrowerItemCountLabel.setText(String.valueOf(mBorrowerItemCount) + mItemCountTailLabel);
                    }
                } catch (NullPointerException ne) {
                    Log.e(LOG_TAG, "ERROR: " + ne.getMessage(), ne);
                } catch (IndexOutOfBoundsException e) {
                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                } finally {
                    mSearchInput.addTextChangedListener(this);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

}
