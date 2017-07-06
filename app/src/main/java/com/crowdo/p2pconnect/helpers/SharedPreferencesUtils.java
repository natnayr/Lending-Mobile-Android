package com.crowdo.p2pconnect.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import java.util.Set;
import java.util.prefs.Preferences;

/**
 * Created by cwdsg05 on 29/12/16.
 */

public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getSharedPrefString(Context context, String key, String defaultValue){
        return getSharedPref(context).getString(key, defaultValue);
    }

    public static boolean setSharePrefString(Context context, String key, String value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static int getSharedPrefInt(Context context, String key, int defaultValue){
        return getSharedPref(context).getInt(key, defaultValue); //null == -1
    }

    public static boolean setSharePrefInt(Context context, String key, int value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean getSharedPrefBool(Context context, String key, boolean defaultValue){
        return getSharedPref(context).getBoolean(key, defaultValue); //null == -1
    }

    public static boolean setSharePrefBool(Context context, String key, boolean value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putBoolean(key, value);
        return editor.commit();

    }

    public static Set<String> getSharedPrefStringSet(Context context, String key, Set<String> defaultValue){
        return getSharedPref(context).getStringSet(key, defaultValue); //null default
    }

    public static boolean setSharedPrefStringSet(Context context, String key, Set<String> value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    public static void setOnSharedPrefChanged(Context context, final CallBackUtil callBackUtil){
        SharedPreferences sharedPreferences = getSharedPref(context);
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                callBackUtil.eventCallBack(key);
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static boolean clearAllSharedPref(Context context){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.clear();
        return editor.commit();
    }


}
