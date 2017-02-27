package com.crowdo.p2pconnect.helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crowdo.p2pconnect.model.RegisteredMemberCheck;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class PerformEmailIdentityCheckTemp {

    private Context context;

    public PerformEmailIdentityCheckTemp(Context context){
        this.context = context;
    }

    public boolean onResponseCode(String LOG_TAG, String enteredEmail,
                                      RegisteredMemberCheck response, View view){
        try {
            RegisteredMemberCheck registeredMemberCheck = response;

            if(registeredMemberCheck.name == null ||
                    registeredMemberCheck.email == null) {
                throw new NullPointerException();
            }

            SharedPreferencesUtils.setSharePrefInt(context,
                    ConstantVariables.PREF_KEY_USER_ID,
                    registeredMemberCheck.memberId);

            SharedPreferencesUtils.setSharePrefBool(context,
                    ConstantVariables.PREF_KEY_USER_IS_MEMBER,
                    registeredMemberCheck.isMember);

            SharedPreferencesUtils.setSharePrefString(context,
                    ConstantVariables.PREF_KEY_USER_NAME,
                    WordUtils.capitalizeFully(registeredMemberCheck.name));

            SharedPreferencesUtils.setSharePrefString(context,
                    ConstantVariables.PREF_KEY_USER_EMAIL,
                    enteredEmail);

            SharedPreferencesUtils.setSharePrefBool(context,
                    ConstantVariables.PREF_KEY_USER_INVESTOR_APPROVAL_SGD,
                    registeredMemberCheck.canInvestSgd);

            SharedPreferencesUtils.setSharePrefBool(context,
                    ConstantVariables.PREF_KEY_USER_INVESTOR_APPROVAL_IDR,
                    registeredMemberCheck.canInvestIdr);

            Toast.makeText(context, "Welcome, " +
                    WordUtils.capitalizeFully(registeredMemberCheck.name),
                    Toast.LENGTH_SHORT).show();

            return true; //return success

        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "ERROR: " + npe.getMessage() + "on email: " + enteredEmail , npe);
            Toast.makeText(context, "There is no account associated to "+ enteredEmail, Toast.LENGTH_SHORT).show();
            SharedPreferencesUtils.resetUserAccountSharedPreferences(context);
        }
        return false;
    }

    public void onFailure(String LOG_TAG, String enteredEmail, Throwable t, View view){
        Log.e(LOG_TAG, "ERROR: CALL FAILURE: " + t.getMessage());
        Toast.makeText(context, "There is no account associated to "+ enteredEmail,
                Toast.LENGTH_SHORT).show();

        SharedPreferencesUtils.resetUserAccountSharedPreferences(context);
    }

}
