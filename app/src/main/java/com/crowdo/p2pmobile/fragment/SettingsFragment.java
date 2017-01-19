package com.crowdo.p2pmobile.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.RegisteredMemberCheck;
import com.crowdo.p2pmobile.data.RegisteredMemberCheckClient;
import com.crowdo.p2pmobile.helper.PerformEmailIdentityCheckTemp;
import com.crowdo.p2pmobile.helper.SharedPreferencesUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 29/12/16.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    //SharedPreference object uses default provided from PreferenceManager
    SharedPreferences sharedPreferences;
    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = SharedPreferencesUtils.getSharedPref(getActivity());

        //load preference on Create
        for(int i=0; i<getPreferenceScreen().getPreferenceCount(); i++){
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
        }

        Preference exitSessBtn = findPreference(getString(R.string.pref_user_logout_key));
        exitSessBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(LOG_TAG, "APP: Session Cleared");
                Toast.makeText(getActivity(), "Cleared",
                        Toast.LENGTH_SHORT).show();

                SharedPreferencesUtils.resetUserAccountSharedPreferences(getActivity());
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    private void pickPreferenceObject(Preference pref){
        if(pref instanceof PreferenceCategory) {
            PreferenceCategory prefCategory = (PreferenceCategory) pref;
            for (int i = 0; i < prefCategory.getPreferenceCount(); i++) {
                pickPreferenceObject(prefCategory.getPreference(i));
            }
        }else{
            initSummary(pref);
        }
    }

    private void initSummary(Preference pref){
        if(pref != null) {
            if (pref instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference) pref;
                pref.setSummary(editTextPreference.getText());
            } else {
                //for static display preferences
                try {
                    Map<String, ?> keys = sharedPreferences.getAll();
                    if(keys.get(pref.getKey()) != null) {
                        if (keys.get(pref.getKey()).getClass().equals(String.class)) {
                            pref.setSummary(sharedPreferences.getString(pref.getKey(), ""));
                        } else if (keys.get(pref.getKey()).getClass().equals(Integer.class)) {
                            pref.setSummary(Integer.toString(sharedPreferences.getInt(pref.getKey(), -1)));
                        } else if (keys.get(pref.getKey()).getClass().equals(Boolean.class)) {
                            pref.setSummary(Boolean.toString(sharedPreferences.getBoolean(pref.getKey(), false)));
                        }
                    }
                }catch (NullPointerException npe){
                    Log.e(LOG_TAG, "ERROR: " + npe.getMessage(), npe);
                }
           }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        Log.d(LOG_TAG, "APP: Preference Dialog triggered for: " +
                getResources().getResourceName(preference.getLayoutResource()));

        super.onDisplayPreferenceDialog(preference);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(sharedPreferences != null || key != null) {
            Preference pref = findPreference(key);

            Log.d(LOG_TAG, "APP: onSharedPreferenceChanged called with key: " + key);
            if (pref instanceof EditTextPreference) {
                pref.setSummary(sharedPreferences.getString(key, ""));
            } else {
                try {
                    Map<String, ?> keys = sharedPreferences.getAll();
                    if(keys.get(pref.getKey()) != null) {
                        if (keys.get(pref.getKey()).getClass().equals(String.class)) {
                            pref.setSummary(sharedPreferences.getString(pref.getKey(), ""));
                        } else if (keys.get(pref.getKey()).getClass().equals(Integer.class)) {
                            pref.setSummary(Integer.toString(sharedPreferences.getInt(pref.getKey(), -1)));
                        } else if (keys.get(pref.getKey()).getClass().equals(Boolean.class)) {
                            pref.setSummary(Boolean.toString(sharedPreferences.getBoolean(pref.getKey(), false)));
                        }
                    }
                }catch (NullPointerException npe){
                    Log.e(LOG_TAG, "ERROR: " + npe.getMessage(), npe);
                }
            }

            //perform identify user
            if (key.equals(getString(R.string.pref_user_email_key))) {
                performEmailIdentify(sharedPreferences, key);
            }
        }
    }

    //Temp to update user in settings
    private void performEmailIdentify(SharedPreferences sharedPreferences, String key){

        final String enteredEmail = sharedPreferences
                .getString(getString(R.string.pref_user_email_key), "")
                .toString().toLowerCase().trim();

        Log.d(LOG_TAG, "APP: enteredEmail is " + enteredEmail);
        final PerformEmailIdentityCheckTemp idenCheck =
                new PerformEmailIdentityCheckTemp(getActivity());

        Call<RegisteredMemberCheck> call = RegisteredMemberCheckClient.getInstance()
                .postUserCheck(enteredEmail);

        call.enqueue(new Callback<RegisteredMemberCheck>() {
            @Override
            public void onResponse(Call<RegisteredMemberCheck> call,
                                   Response<RegisteredMemberCheck> response) {

                idenCheck.onResponseCode(LOG_TAG, enteredEmail, response);
            }

            @Override
            public void onFailure(Call<RegisteredMemberCheck> call, Throwable t) {
                idenCheck.onFailure(LOG_TAG, enteredEmail, t);
            }
        });
    }


}
