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
                .baseUrl(APIServices.P2P_BASE_URL)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanFactSheetClient getInstance(Context context, int loanId){
        return new LoanFactSheetClient(context, loanId);
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
                    String nowTime = DateTime.now().toString("yyyy-MM-dd'T'HH:mm");
                    String header = response.headers().get("Content-Disposition");
                    Log.d(LOG_TAG, "APP: header = [" + header + "]");
                    String fileName = loanId + "-" + nowTime + ".pdf";
                    Log.d(LOG_TAG, "APP: filename is " + fileName+".pdf");

                    File finalFile, externalRoot;
                    if(StorageHelper.isExternalStorageReadableAndWritable()){
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            //if external cacahe temp storage is avalible
                            externalRoot = mContext.getExternalCacheDir().getAbsoluteFile();
                        }else{
                            //if not, then store in external downloads directory
                            externalRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile();
                        }
                        finalFile = new File(externalRoot, fileName);
                    }else{
                        //last hope
                        Toast.makeText(mContext,
                                "External storage is missing, downloading to Internal Downloads Folder",
                                Toast.LENGTH_LONG).show();
                        finalFile = new File(mContext.getFilesDir(), fileName);
                    }

                    Log.d(LOG_TAG, "APP: Begining to download into " + finalFile.getAbsolutePath());
                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(finalFile));
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();

                    Log.d(LOG_TAG, "APP: bufferedSink done...");
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
