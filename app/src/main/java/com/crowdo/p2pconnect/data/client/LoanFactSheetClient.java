package com.crowdo.p2pconnect.data.client;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.HardwareUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.functions.Function;
import rx.Subscriber;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(APIServices.API_BASE_URL)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanFactSheetClient getInstance(Context context, int loanId){
        return new LoanFactSheetClient(context, loanId);
    }

    public Observable<File> getLoanFactSheet(){
        return  apiServices.getLoanFactSheet(this.loanId, LocaleHelper.getLanguage(mContext))
            .flatMap(processResponse())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private Function<Response<ResponseBody>, Observable<File>> processResponse(){
//        return new Func1<Response<ResponseBody>, Observable<File>>() {
//            @Override
//            public Observable<File> call(Response<ResponseBody> responseBodyResponse) {
//                return saveFile(responseBodyResponse);
//            }
//        };
    } //TODO: replace with RxDownload!!!

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
                            Log.d(LOG_TAG, "APP: Shared/ExternalStorage State Storage Directory is set Environment.MEDIA_MOUNTED");
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
