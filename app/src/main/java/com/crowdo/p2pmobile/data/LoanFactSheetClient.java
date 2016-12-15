package com.crowdo.p2pmobile.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.helper.StorageHelper;

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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by cwdsg05 on 15/12/16.
 */

public class LoanFactSheetClient {

    public static final String LOG_TAG = LoanFactSheetClient.class.getSimpleName();

    private static LoanFactSheetClient instance;
    private APIServices apiServices;
    private Context mContext;
    private int loanId;

    public LoanFactSheetClient(Context context, int loanId){
        this.mContext = context;
        this.loanId = loanId;

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(APIServices.P2P_BASE_URL)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanFactSheetClient getInstance(Context context, int loanId){
        if(instance == null)
            instance = new LoanFactSheetClient(context, loanId);
        return instance;
    }

    public Observable<File> getLoanFactSheet(){
        return  apiServices.getLoanFactSheet(this.loanId)
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
                    String header = response.headers().get("Content-Disposition");
                    Log.d(LOG_TAG, "TEST: header = [" + header + "]");
                    String fileName = loanId+"_"+ DateTime.now().toString("yyyy-MM-dd'T'HH:mm");
                    Log.d(LOG_TAG, "TEST: filename is " +  fileName+".pdf");

                    Log.d(LOG_TAG, "TEST: is storage readwrite? "
                            + StorageHelper.isExternalStorageReadableAndWritable());

                    File file;
                    if(StorageHelper.isExternalStorageReadableAndWritable()){
                        Log.d(LOG_TAG, "TEST: creating file in " + Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                .getAbsolutePath());
                        file = new File(Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                .getAbsoluteFile(), fileName);
                    }else{
                        Toast.makeText(mContext,
                                "External storage is missing, downloading to internal",
                                Toast.LENGTH_LONG).show();
                        file = new File(mContext.getFilesDir(), fileName);
                    }

                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    Log.d(LOG_TAG, "TEST: bufferedSink done...");
                    subscriber.onNext(file);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
