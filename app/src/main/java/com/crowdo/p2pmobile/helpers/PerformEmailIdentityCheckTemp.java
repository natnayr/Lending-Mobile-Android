package com.crowdo.p2pmobile.helpers;

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
                                      RegisteredMemberCheck response){
        try {
            RegisteredMemberCheck registeredMemberCheck = response;

            if(registeredMemberCheck.name == null ||
                    registeredMemberCheck.email == null)
                throw new NullPointerException();

            SharedPreferencesUtils.setSharePrefInt(context,
                    context.getString(R.string.pref_user_id_key),
                    registeredMemberCheck.memberId);

            SharedPreferencesUtils.setSharePrefBool(context,
                    context.getString(R.string.pref_user_is_member_key),
                    registeredMemberCheck.isMember);

            SharedPreferencesUtils.setSharePrefString(context,
                    context.getString(R.string.pref_user_name_key),
                    WordUtils.capitalizeFully(registeredMemberCheck.name));

            SharedPreferencesUtils.setSharePrefString(context,
                    context.getString(R.string.pref_user_email_key),
                    enteredEmail);

            SharedPreferencesUtils.setSharePrefBool(context,
                    context.getString(R.string.pref_user_investor_approval_sgd_key),
                    registeredMemberCheck.canInvestSgd);

            SharedPreferencesUtils.setSharePrefBool(context,
                    context.getString(R.string.pref_user_investor_approval_idr_key),
                    registeredMemberCheck.canInvestIdr);

            Toast.makeText(context, "Welcome, " +
                            WordUtils.capitalizeFully(registeredMemberCheck.name),
                    Toast.LENGTH_SHORT).show();

            return true; //return success

        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "ERROR: " + npe.getMessage() + "on email: " + enteredEmail , npe);
            Toast.makeText(context, "Sorry, "+ enteredEmail +
                    " did not match anything", Toast.LENGTH_SHORT).show();
            SharedPreferencesUtils.resetUserAccountSharedPreferences(context);
        }
        return false;
    }

    public void onFailure(String LOG_TAG, String enteredEmail, Throwable t){
        Log.e(LOG_TAG, "ERROR: CALL FAILURE: " + t.getMessage());
        Toast.makeText(context, "Sorry, "+
                        enteredEmail +" did not match anything",
                Toast.LENGTH_SHORT).show();

        SharedPreferencesUtils.resetUserAccountSharedPreferences(context);
    }

}
