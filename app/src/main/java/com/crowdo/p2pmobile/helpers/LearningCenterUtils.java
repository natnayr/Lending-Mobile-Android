package com.crowdo.p2pmobile.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.model.LearningCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenterUtils {

    private static final String LOG_TAG = LearningCenterUtils.class.getSimpleName();

    public void populateData(final Context context){
        Realm realm = Realm.getDefaultInstance();

        //on another thread
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                //clear all Learning Center
                realm.where(LearningCenter.class).findAll().deleteAllFromRealm();

                final List<String> csvCategories = Arrays.asList(context.getResources()
                        .getStringArray(R.array.learning_center_categories_csv));
                final String EN = ConstantVariables.LEARNING_CENTER_DB_EN;
                final String ID = ConstantVariables.LEARNING_CENTER_DB_ID;
                String json = null;

                try {
                    InputStream is = context.getAssets().open(ConstantVariables.LEARNING_CENTER_CSV_FILE_LOCATION);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ioe) {
                    Log.e(LOG_TAG, "ERROR: " + ioe.getMessage(), ioe);
                }

                try {
                    JSONArray arrRecords = new JSONArray(json);
                    int numRecords = arrRecords.length();
                    for(int i=0; i<numRecords; i++){
                        JSONObject rec = arrRecords.getJSONObject(i);
                        if(csvCategories.contains(rec.getString("Category"))){
                            LearningCenter enLearningCenter = realm.createObject(LearningCenter.class);
                            enLearningCenter.setLanguage(EN);
                            enLearningCenter.setCategory(rec.getString("Category"));
                            enLearningCenter.setQuestion(rec.getString("Eng_Ques"));
                            enLearningCenter.setAnswer(rec.getString("Eng_Ans"));

                            LearningCenter idLearningCenter = realm.createObject(LearningCenter.class);
                            idLearningCenter.setLanguage(ID);
                            idLearningCenter.setCategory(rec.getString("Category"));
                            idLearningCenter.setQuestion(rec.getString("Indo_Ques"));
                            idLearningCenter.setAnswer(rec.getString("Indo_Ans"));
                        }
                    }
                } catch(JSONException je){
                    Log.e(LOG_TAG, "ERROR: " + je.getMessage(), je);
                }

            }
        }, new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {
//                SharedPreferencesUtils.setSharePrefBool(context,
//                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, true);
            }
        }, new Realm.Transaction.OnError(){
            @Override
            public void onError(Throwable error) {
                Log.e(LOG_TAG, "ERROR: " + error.getMessage(), error);
                SharedPreferencesUtils.setSharePrefBool(context,
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);
            }
        });


        String search = "Generally, income derived from transactions processed through the platform are subject to tax. For Indonesia Tax Residents, the reference tax rate is 15% (income tax) applied to the interest earned from their investment returns";

        RealmResults<LearningCenter> results = realm.where(LearningCenter.class)
                .equalTo("language", ConstantVariables.LEARNING_CENTER_DB_EN)
                .beginGroup()
                    .contains("question", search)
                    .or()
                    .contains("answer", search)
                .endGroup().findAll();

        Iterator<LearningCenter> rit = results.iterator();
        while (rit.hasNext()){
            LearningCenter lc = rit.next();
            Log.d(LOG_TAG, "APP: REALM LANG ["+lc.getLanguage()+"]");
            Log.d(LOG_TAG, "APP: REALM CATE ["+lc.getCategory()+"]");
            Log.d(LOG_TAG, "APP: REALM QUES ["+lc.getQuestion()+"]");
            Log.d(LOG_TAG, "APP: REALM ANSW ["+lc.getAnswer()+"]");

            Toast.makeText(context, lc.getAnswer(), Toast.LENGTH_LONG).show();

        }

    }

}
