package com.crowdo.p2pmobile.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pmobile.helpers.HardwareUtils;
import com.crowdo.p2pmobile.helpers.SnackBarUtil;

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
 * Created by cwdsg05 on 31/1/17.
 */

public class WebViewFileSaveClient {

    public static final String LOG_TAG = WebViewFileSaveClient.class.getSimpleName();
    private APIServices apiServices;
    private Context mContext;
    private String url;
    private String fileName;


    public WebViewFileSaveClient(Context context, String endpoint, String fileName){
        this.mContext = context;
        this.url = endpoint;
        this.fileName = fileName;

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(endpoint + "/")
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static WebViewFileSaveClient getInstance(Context context, String url, String fileName){
        return new WebViewFileSaveClient(context, url, fileName);
    }

    public Observable<File> getDownloadFile(){
        return apiServices.getWebViewDownloadFile(this.url)
                .flatMap(processResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Func1<Response<ResponseBody>, Observable<File>> processResponse(){
        return new Func1<Response<ResponseBody>, Observable<File>>() {
            @Override
            public Observable<File> call(Response<ResponseBody> response) {
                return saveFile(response);
            }
        };
    }

    private Observable<File> saveFile(final Response<ResponseBody> response){
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {
                    File finalFile, externalRoot;
                    if (HardwareUtils.isExternalStorageReadableAndWritable()) {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            externalRoot = mContext.getExternalCacheDir().getAbsoluteFile();
                        } else {
                            externalRoot = Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .getAbsoluteFile();
                        }
                        finalFile = new File(externalRoot, fileName);
                    } else {
                        Toast.makeText(mContext,
                                "External storage is missing, downloading to Internal Downloads Folder",
                                Toast.LENGTH_LONG).show();
                        finalFile = new File(mContext.getFilesDir(), fileName);
                    }

                    Log.d(LOG_TAG, "APP: Begining to download into " + finalFile.getAbsolutePath());
                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(finalFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    subscriber.onNext(finalFile);
                    subscriber.onCompleted();
                }catch (IOException e){
                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

}
