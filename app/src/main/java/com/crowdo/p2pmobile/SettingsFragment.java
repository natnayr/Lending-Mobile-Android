package com.crowdo.p2pmobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.crowdo.p2pmobile.helper.SharedPreferencesHelper;

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
        addPreferencesFromResource(R.xml.app_preferences);
        sharedPreferences = SharedPreferencesHelper.getSharedPref(getActivity());

        //load preference on Create
        for(int i=0; i<getPreferenceScreen().getPreferenceCount(); i++){
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
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
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference instanceof EditTextPreference ||
                preference instanceof Preference){
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    private void pickPreferenceObject(Preference pref){
        Log.d(LOG_TAG, "TEST: prefkey=" + pref.getKey());
        Log.d(LOG_TAG, "TEST: summary=" + pref.getSummary());
        Log.d(LOG_TAG, "TEST: value=" + sharedPreferences.getString(pref.getKey(), ""));

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
        if(pref instanceof EditTextPreference){
            EditTextPreference editTextPreference = (EditTextPreference) pref;
            pref.setSummary(editTextPreference.getText());
        }else if(pref instanceof Preference){
            pref.setSummary(sharedPreferences.getString(pref.getKey(),
                    getString(R.string.pref_user_name_summary)));
        }
    }

}
