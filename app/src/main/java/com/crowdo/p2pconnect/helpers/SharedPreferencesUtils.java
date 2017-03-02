package com.crowdo.p2pconnect.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

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

    public static void setSharePrefString(Context context, String key, String value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getSharedPrefInt(Context context, String key, int defaultValue){
        return getSharedPref(context).getInt(key, defaultValue); //null == -1
    }

    public static void setSharePrefInt(Context context, String key, int value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getSharedPrefBool(Context context, String key, boolean defaultValue){
        return getSharedPref(context).getBoolean(key, defaultValue); //null == -1
    }

    public static void setSharePrefBool(Context context, String key, boolean value){
        SharedPreferences.Editor editor = getSharedPref(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
