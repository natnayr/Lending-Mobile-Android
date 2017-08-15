package com.crowdo.p2pconnect.support;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.view.activities.Henson;

/**
 * Created by cwdsg05 on 8/8/17.
 */

public class InvestorAccreditationReaction {

    public static Snackbar failedInvestorAcreditationSnackbar(String snackbarResponseLabel, String signUpButtonLabel,
                                                              final View rootView, final Activity activity){
        Snackbar investorInvalidSnackbar = SnackBarUtil.snackBarForWarningCreate(rootView,
                snackbarResponseLabel, Snackbar.LENGTH_LONG);

        investorInvalidSnackbar.setAction(signUpButtonLabel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webViewUrl = APIServices.P2P_BASE_URL +
                        "mobile2/register_as_investor" +
                        "?lang=" + LocaleHelper.getLanguage(activity) +
                        "&device_id=" +
                        ConstantVariables.getUniqueAndroidID(activity);


                Intent intent = Henson.with(activity)
                        .gotoWebViewActivity()
                        .mUrl(webViewUrl)
                        .build();
                activity.startActivity(intent);
            }
        });

        return investorInvalidSnackbar;
    }
}
