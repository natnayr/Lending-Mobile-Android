package com.crowdo.p2pmobile;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.data.RegisteredMemberCheck;
import com.crowdo.p2pmobile.data.RegisteredMemberCheckClient;
import com.crowdo.p2pmobile.helper.SharedPreferencesHelper;

import org.junit.Test;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import static org.junit.Assert.assertTrue;

/**
 * Created by cwdsg05 on 21/11/16.
 */

public class TestRetrofitCalls {

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";
    public static final String LOG_TAG = TestRetrofitCalls.class.getSimpleName();

    @Test
    public void testDownloadList() throws Exception{
        
    }


}
