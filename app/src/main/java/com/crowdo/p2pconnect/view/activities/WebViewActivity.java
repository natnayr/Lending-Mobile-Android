package com.crowdo.p2pconnect.view.activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
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

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.esafirm.rxdownloader.RxDownloader;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    public static final String LOG_TAG = WebViewActivity.class.getSimpleName();

    @BindView(R.id.webview) AdvancedWebView mWebView;
    @BindView(R.id.toolbar_webview) Toolbar mToolbar;
    @BindView(R.id.webview_swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.webview_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.webview_root) CoordinatorLayout rootView;

    @BindColor(R.color.color_icons_text) int colorIconText;

    @BindString(R.string.downloading_label) String mLabelDownloading;
    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.cancel_label) String mLabelCancel;
    @BindString(R.string.open_label) String mLabelOpen;
    @BindString(R.string.intent_file_chooser) String mLabelIntentFile;
    @BindString(R.string.webview_unable_open_file) String mLabelErrorOpenFile;
    @BindString(R.string.webview_download_to) String mLabelDownloadedTo;
    @BindString(R.string.permissions_write_request) String mLabelExternalDriveRequest;
    @BindString(R.string.permissions_no_write_statement) String mLabelCannotWrite;

    @InjectExtra public int id;
    @InjectExtra public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        ButterKnife.bind(WebViewActivity.this);

        //mToolbar view
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //inject intent settings
        Dart.inject(WebViewActivity.this);

        mWebView.setListener(WebViewActivity.this, WebViewActivity.this);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mWebView.loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d(LOG_TAG, "APP: Permission Requested");
                    request.grant(request.getResources());
                }
            }
        });


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color_primary_100,
                R.color.color_primary, R.color.color_primary_700);

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
        int count = getSupportFragmentManager()
                .getBackStackEntryCount();

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
    public void onDownloadRequested(final String url, final String fileName,
                                    final String downloadMimeType, long contentLength,
                                    String contentDisposition, String userAgent) {

        //check permissions
        if(PermissionsUtils.checkPermissionAndRequestActivity(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE) == false){
            return;
        }

        String mimeType = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null) {
            mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(extension);
        }

        Toast.makeText(this, mLabelDownloading,
                Toast.LENGTH_SHORT).show();

        final String usageMimeType = mimeType;

        RxDownloader.getInstance(this)
                .download(url, fileName, mimeType)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP: Completed WebView Download");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(final String s) {
                        Log.d(LOG_TAG, "APP: file is now in " + s);

                        final Snackbar snackbar = SnackBarUtil
                                .snackBarCreate(rootView, mLabelDownloadedTo + s,
                                colorIconText, Snackbar.LENGTH_LONG);


                        snackbar.setAction(mLabelOpen, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    File downloadFile = new File(new URI(s));

                                    intent.setDataAndType(Uri.fromFile(downloadFile), usageMimeType);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    Intent chooserIntent = Intent.createChooser(intent, mLabelIntentFile);


                                    startActivity(chooserIntent);
                                }catch(URISyntaxException ue){
                                    Log.e(LOG_TAG, "ERROR: " + ue.getMessage(), ue);
                                    final Snackbar snackbar = SnackBarUtil.snackBarCreate(rootView,
                                            mLabelErrorOpenFile,
                                            colorIconText);

                                    snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                }catch (ActivityNotFoundException anfe){
                                    Log.e(LOG_TAG, "ERROR: " + anfe.getMessage(), anfe);
                                    final Snackbar snackbar = SnackBarUtil.snackBarCreate(rootView,
                                            mLabelErrorOpenFile,
                                            colorIconText);

                                    snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                        snackbar.show();
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

    @Override
    public void onSupportActionModeStarted(@NonNull android.support.v7.view.ActionMode mode) {
        //cause style.xml windowActionBarOverlay doesnt work
        mToolbar.setVisibility(View.GONE);
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(@NonNull android.support.v7.view.ActionMode mode) {
        //cause style.xml windowActionBarOverlay doesnt work
        super.onSupportActionModeFinished(mode);
        mToolbar.animate()
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToolbar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
