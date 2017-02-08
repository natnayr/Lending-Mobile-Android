package com.crowdo.p2pmobile.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.helpers.HardwareUtils;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 15/12/16.
 */

public class LoanFactSheetClient {

    public static final String LOG_TAG = LoanFactSheetClient.class.getSimpleName();
    private APIServices apiServices;
    private Context mContext;
    private int loanId;

    public LoanFactSheetClient(Context context, int loanId){
        this.mContext = context;
        this.loanId = loanId;

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(APIServices.API_BASE_URL)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanFactSheetClient getInstance(Context context, int loanId){
        return new LoanFactSheetClient(context, loanId);
    }

    public Observable<File> getLoanFactSheet(){
        return  apiServices.getLoanFactSheet(this.loanId,
                ConstantVariables.APP_LANG_SET)
            .flatMap(processResponse())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private Func1<Response<ResponseBody>, Observable<File>> processResponse(){
        return new Func1<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> call(Response<ResponseBody> responseBodyResponse) {
                return saveFile(responseBodyResponse);
            }
        };
    }

    private Observable<File> saveFile(final Response<ResponseBody> response) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {
                    String header = response.headers().get("Content-Disposition")
                            .replace("attachment; filename=", "")
                            .replace("\"", "");
                    Log.d(LOG_TAG, "APP: header = [" + header + "]");
                    String fileName = header;

                    File finalFile, externalRoot;
                    if(HardwareUtils.isExternalStorageReadableAndWritable()){
                        Log.d(LOG_TAG, "APP: isExternalStorageReadableAndWritable() is true");
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            //if not, then store in external downloads directory
                            Log.d(LOG_TAG, "APP: Shared/External Storage Directory set as root");
                            externalRoot = Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .getAbsoluteFile();
                        }else{
                            //if external cacahe temp storage is avalible
                            Log.d(LOG_TAG, "APP: External Storage Cache Directory set as root");
                            externalRoot = mContext.getExternalCacheDir().getAbsoluteFile();
                        }
                        finalFile = new File(externalRoot, fileName);
                    }else{
                        Log.d(LOG_TAG, "APP: isExternalStorageReadableAndWritable() is false");

                        //last hope
                        Toast.makeText(mContext, mContext.getString(
                                R.string.loan_detail_factsheet_prog_toast_external_storage_miss),
                                Toast.LENGTH_LONG).show();
                        finalFile = new File(mContext.getFilesDir(), fileName);
                    }

                    Log.d(LOG_TAG, "APP: Begining to download into " + finalFile.getAbsolutePath());
                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(finalFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    subscriber.onNext(finalFile);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
