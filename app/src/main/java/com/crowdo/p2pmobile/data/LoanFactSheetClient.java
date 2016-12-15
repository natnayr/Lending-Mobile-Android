package com.crowdo.p2pmobile.data;

import android.os.Environment;
import android.util.Log;

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

    public LoanFactSheetClient(){

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(APIServices.P2P_BASE_URL)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanFactSheetClient getInstance(){
        if(instance == null)
            instance = new LoanFactSheetClient();
        return instance;
    }

    public Observable<File> getLoanFactSheet(int id){
        return  apiServices.getLoanFactSheet(id)
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
                    String fileName = header.replace("attachment; filename=", "");
                    Log.d(LOG_TAG, "TEST: filename is " +  fileName);

                    File file = new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .getAbsoluteFile(), fileName);


                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

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
