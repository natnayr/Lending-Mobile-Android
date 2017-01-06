package com.crowdo.p2pmobile.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.RegisteredMemberCheck;

import org.apache.commons.lang3.text.WordUtils;

import retrofit2.Response;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class PerformEmailIdentityCheckTemp {

    private Context context;

    public PerformEmailIdentityCheckTemp(Context context){
        this.context = context;
    }

    public boolean onResponseCode(String LOG_TAG, String enteredEmail,
                                      Response<RegisteredMemberCheck> response){
        try {
            RegisteredMemberCheck registeredMemberCheck = response.body();

            if(registeredMemberCheck.name == null ||
                    registeredMemberCheck.id <= 0)
                throw new NullPointerException();

            SharedPreferencesHelper.setSharePrefInt(context,
                    context.getString(R.string.pref_user_id_key),
                    registeredMemberCheck.id);

            SharedPreferencesHelper.setSharePrefBool(context,
                    context.getString(R.string.pref_is_user_sg_registered_key),
                    registeredMemberCheck.registeredSingapore);

            SharedPreferencesHelper.setSharePrefBool(context,
                    context.getString(R.string.pref_is_user_indo_registered_key),
                    registeredMemberCheck.registeredIndonesia);

            SharedPreferencesHelper.setSharePrefString(context,
                    context.getString(R.string.pref_user_name_key),
                    WordUtils.capitalizeFully(registeredMemberCheck.name));

            //store keyed in one
            SharedPreferencesHelper.setSharePrefString(context,
                    context.getString(R.string.pref_user_email_key),
                    enteredEmail);

            Toast.makeText(context, "Welcome, " +
                            WordUtils.capitalizeFully(registeredMemberCheck.name),
                    Toast.LENGTH_SHORT).show();
            if(registeredMemberCheck.id > 0 )
                return true; //return success

        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "ERROR: " + npe.getMessage() + "on email: " + enteredEmail , npe);
            Toast.makeText(context, "Sorry, "+ enteredEmail +
                    " did not match anything", Toast.LENGTH_SHORT).show();
            SharedPreferencesHelper.resetUserAccountSharedPreferences(context);
        }
        return false;
    }

    public void onFailure(String LOG_TAG, String enteredEmail, Throwable t){
        Log.e(LOG_TAG, "ERROR: CALL FAILURE: " + t.getMessage());
        Toast.makeText(context, "Sorry, "+
                        enteredEmail +" did not match anything",
                Toast.LENGTH_SHORT).show();

        SharedPreferencesHelper.resetUserAccountSharedPreferences(context);
    }

}
