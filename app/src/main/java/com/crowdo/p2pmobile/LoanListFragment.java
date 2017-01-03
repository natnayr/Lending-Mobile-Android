package com.crowdo.p2pmobile;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pmobile.data.LoanListItem;
import com.crowdo.p2pmobile.data.LoanListClient;
import com.crowdo.p2pmobile.data.RegisteredMemberCheck;
import com.crowdo.p2pmobile.data.RegisteredMemberCheckClient;
import com.crowdo.p2pmobile.helper.SharedPreferencesHelper;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoanListFragment extends Fragment {

    private static final String LOG_TAG = LoanListFragment.class.getSimpleName();

    private ListView mListView;
    private LoanListAdapter mLoanAdapter;
    private Subscription loanListSubscription;
    private SwipeRefreshLayout swipeContainer;
    private AlertDialog alertDialog;

    public LoanListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoanAdapter = new LoanListAdapter(getActivity());
        populateLoansList();

        //call for email address to identify user.
        dialogEmailPrompt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_loans);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoanListView);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateLoansList();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color_primary_light,
                R.color.color_primary, R.color.color_primary_dark);

        mListView.setAdapter(mLoanAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {

                LoanListItem item = (LoanListItem) adapterView.getItemAtPosition(position);
                Intent intent = Henson.with(getActivity())
                        .gotoLoanDetailsActivity()
                        .id(item.id)
                        .build();
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onDestroy() {
        if(loanListSubscription != null &&
                !loanListSubscription.isUnsubscribed()){
            loanListSubscription.unsubscribe();
        }

        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        SharedPreferencesHelper.setSharePrefBool(getActivity(),
                getActivity().getString(R.string.pref_is_email_dialog_run_key),
                true);
    }

    private void populateLoansList(){
        loanListSubscription = LoanListClient.getInstance()
                .getLiveLoans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoanListItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "TEST: populateLoansList Rx onComplete");
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<LoanListItem> loanListItems) {
                        Log.d(LOG_TAG, "TEST: populateLoansList Rx onNext with "
                                + loanListItems.size() + " items retreived.");
                        mLoanAdapter.setLoans(loanListItems);
                    }
                });
    }

    @BindView(android.R.id.message) TextView memberCheckEmailTextView;
    @BindView(android.R.id.edit) EditText memberCheckEmailEditText;
    @BindString(R.string.member_check_email_dialog_label) String memberCheckEmailDialogLabel;
    @BindString(R.string.pref_user_email_dialog_default_value) String memberCheckEmailDialogDefaultValue;
    private void dialogEmailPrompt(){

        boolean enterEmailPopUpShown = SharedPreferencesHelper.getSharedPrefBool(getActivity(),
                getActivity().getString(R.string.pref_is_email_dialog_run_key),
                true);

        int acctMemberId = SharedPreferencesHelper.getSharedPrefInt(getActivity(),
                getActivity().getString(R.string.pref_user_id_key),
                -1);

        Log.d(LOG_TAG, "TEST: enteredEmailYet=" + enterEmailPopUpShown +
                " and acctMemberId=" + acctMemberId);

        if(enterEmailPopUpShown && acctMemberId == -1) {
            Log.d(LOG_TAG, "TEST: email dialog pop up start");
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View dialogView = inflater.inflate(R.layout.pref_dialog_edittext_fix, null);
            ButterKnife.bind(this, dialogView);

            // setting dialog layout
            memberCheckEmailTextView.setText(memberCheckEmailDialogLabel);
            memberCheckEmailEditText.setHint(memberCheckEmailDialogDefaultValue);
            memberCheckEmailEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            AlertDialog.Builder alertDialogBuilderInput = new AlertDialog.Builder(getActivity());
            alertDialogBuilderInput.setView(dialogView);

            alertDialogBuilderInput
                    .setCancelable(false)
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            final String enteredEmail = memberCheckEmailEditText.getText()
                                    .toString().toLowerCase().trim();

                            Call<RegisteredMemberCheck> call = RegisteredMemberCheckClient.getInstance()
                                    .postUserCheck(enteredEmail);

                            call.enqueue(new Callback<RegisteredMemberCheck>() {
                                @Override
                                public void onResponse(Call<RegisteredMemberCheck> call,
                                                       Response<RegisteredMemberCheck> response) {

                                    Context context = getActivity();

                                    if(response.body() != null){
                                        RegisteredMemberCheck registeredMemberCheck = response.body();
                                        Log.d(LOG_TAG, "TEST: onResponse " + registeredMemberCheck.name);

                                        SharedPreferencesHelper.setSharePrefInt(context,
                                                context.getString(R.string.pref_user_id_key),
                                                registeredMemberCheck.id);

                                        SharedPreferencesHelper.setSharePrefBool(getActivity(),
                                                context.getString(R.string.pref_is_user_sg_registered_key),
                                                registeredMemberCheck.registeredSingapore);

                                        SharedPreferencesHelper.setSharePrefBool(getActivity(),
                                                context.getString(R.string.pref_is_user_indo_registered_key),
                                                registeredMemberCheck.registeredIndonesia);

                                        SharedPreferencesHelper.setSharePrefString(getActivity(),
                                                context.getString(R.string.pref_user_name_key),
                                                WordUtils.capitalizeFully(registeredMemberCheck.name));

                                        //store keyed in one
                                        SharedPreferencesHelper.setSharePrefString(getActivity(),
                                                context.getString(R.string.pref_user_email_key),
                                                enteredEmail);

                                        Toast.makeText(getActivity(), "Welcome, " +
                                                        WordUtils.capitalizeFully(registeredMemberCheck.name),
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        Log.d(LOG_TAG, "TEST: failed to get email: " + enteredEmail );
                                        Toast.makeText(getActivity(), "Sorry, "+ enteredEmail +" did not match anything",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<RegisteredMemberCheck> call, Throwable t) {
                                    Log.e(LOG_TAG, "ERROR: CALL FAILURE: " + t.getMessage());
                                    Toast.makeText(getActivity(), "Sorry, "+ enteredEmail +" did not match anything",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.cancel();
                        }
                    });

            alertDialog = alertDialogBuilderInput.create();
            alertDialog.show();
        }

        SharedPreferencesHelper.setSharePrefBool(getActivity(),
                getActivity().getString(R.string.pref_is_email_dialog_run_key),
                false);
    }

}
