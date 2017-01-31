package com.crowdo.p2pmobile.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.WebViewFileSaveClient;
import com.crowdo.p2pmobile.helpers.SnackBarUtil;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import java.io.File;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;
import rx.Observer;
import rx.Subscription;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    public static final String LOG_TAG = WebViewActivity.class.getSimpleName();


    @BindView(R.id.webview) AdvancedWebView mWebView;
    @BindView(R.id.toolbar_webview) Toolbar mToolbar;
    @BindView(R.id.webview_swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.webview_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.webview_root) View rootView;
    @BindColor(R.color.color_icons_text) int colorIconText;
    @InjectExtra public int id;
    @InjectExtra public String url;

    Subscription webviewDownloadSubscription;

    private static final String pdfMimeType = "pdf";
    private static final String imgMimeType = "image/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        ButterKnife.bind(this);

        //mToolbar view
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //inject intent settings
        Dart.inject(this);

        mWebView.setListener(this, this);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mWebView.loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                Log.d(LOG_TAG, "APP: Permission Requested");
                request.grant(request.getResources());
            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color_primary_light,
                R.color.color_primary, R.color.color_primary_dark);

    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }


    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDownloadRequested(final String url, final String suggestedFilename,
                                    final String downloadMimeType, long contentLength,
                                    String contentDisposition, String userAgent) {

        final String mimeType = MimeTypeMap.getFileExtensionFromUrl(url);

        Log.d(LOG_TAG, "APP: URL:" + url +
                " suggestedFileName:" + suggestedFilename +
                " mimeType:" + mimeType +
                " downloadMimeType:" + downloadMimeType);

        https://cwd-nexus.s3-ap-southeast-1.amazonaws.com/documents/top_up/425/transaction_proof/IMG-20170126-WA0034.jpeg?X-Amz-Expires=600&X-Amz-Date=20170131T141032Z&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJIR32J6J7253L5TA/20170131/ap-southeast-1/s3/aws4_request&X-Amz-SignedHeaders=host&X-Amz-Signature=e4d24193b753b8943aaf78593f1b87e2f05510cd91b77763cdd87f846e4274bf


        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        webviewDownloadSubscription = WebViewFileSaveClient
            .getInstance(this, url, suggestedFilename)
            .getDownloadFile()
            .subscribe(new Observer<File>() {
                @Override
                public void onCompleted() {
                    Log.d(LOG_TAG, "APP: webViewDownload complete");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                }

                @Override
                public void onNext(final File file) {
                    final Snackbar snackBarOnNext = SnackBarUtil.snackBarCreate(rootView,
                            file.getName(),
                            colorIconText, Snackbar.LENGTH_LONG);

                    snackBarOnNext.setAction("OPEN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);

                            if(mimeType.equals(pdfMimeType)){
                                Log.d(LOG_TAG, "APP: YOOOO HOOOO");
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                            }else if(mimeType.equals("jpeg")){
                                intent.setDataAndType(Uri.fromFile(file), "image/*");
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Intent chooserIntent = Intent.createChooser(intent, "Open With");

                            try{
                                startActivity(chooserIntent);
                            }catch (ActivityNotFoundException e){
                                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                final Snackbar snackBarError = SnackBarUtil.snackBarCreate(
                                        rootView, "Error opening file", colorIconText);

                                snackBarError.setAction("OK", new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        snackBarError.dismiss();
                                    }
                                });

                                snackBarError.show();
                            }
                        }
                    });
                    snackBarOnNext.show();
                }
            });
    }

    @Override
    public void onExternalPageRequest(String url) { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean toBackStackOrParent(){
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //If no backstack then navigate to logical main list view
            NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
    }


    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public void goBack(){
            Log.d(LOG_TAG, "APP: JavaScriptInterface goBack() called");
            finish();
        }
    }
}
