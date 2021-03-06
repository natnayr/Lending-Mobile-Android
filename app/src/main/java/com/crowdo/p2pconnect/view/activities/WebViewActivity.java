package com.crowdo.p2pconnect.view.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andretietz.retroauth.AuthAccountManager;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.oauth.SocialAuthConstant;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    @BindView(R.id.webview) AdvancedWebView mWebView;
    @BindView(R.id.toolbar_webview) Toolbar mToolbar;
    @BindView(R.id.webview_swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.webview_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.webview_root) CoordinatorLayout rootView;

    @BindColor(R.color.color_snackbar_teal_A200) int colorSnackbarColorText;

    @BindString(R.string.downloading_label) String mLabelDownloading;
    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.cancel_label) String mLabelCancel;
    @BindString(R.string.open_label) String mLabelOpen;
    @BindString(R.string.intent_file_chooser) String mLabelIntentFile;
    @BindString(R.string.unable_open_file_label) String mLabelErrorOpenFile;
    @BindString(R.string.downloaded_to_label) String mLabelDownloadedTo;
    @BindString(R.string.permissions_write_request) String mLabelExternalDriveRequest;
    @BindString(R.string.permissions_no_write_statement) String mLabelCannotWrite;
    @BindString(R.string.error_unable_to_pass_data) String mLabelUnableData;
    public static final String URL_TARGET_EXTRA = "EXTRA_URL_TARGET";
    public static final String REQUIRE_AUTH_TOKEN_EXTRA = "EXTRA_REQUIRE_AUTH_TOKEN";

    public static final String LOG_TAG = WebViewActivity.class.getSimpleName();
    private String mUrl;
    private boolean mRequireAuthTokenCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ButterKnife.bind(WebViewActivity.this);

        this.mUrl = getIntent().getStringExtra(URL_TARGET_EXTRA);
        this.mRequireAuthTokenCheck = getIntent().getBooleanExtra(REQUIRE_AUTH_TOKEN_EXTRA, true);

        if(mUrl == null){
            SnackBarUtil.snackBarForErrorCreate(findViewById(android.R.id.content),
                    mLabelUnableData, Snackbar.LENGTH_LONG).show();
            finish();
        }

        //mToolbar view
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mWebView.setListener(WebViewActivity.this, WebViewActivity.this);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        final Map<String, String> headerMap = new HashMap<>();

        //if able to get authToken
        if(mRequireAuthTokenCheck) {
            AuthAccountManager authAccountManager = new AuthAccountManager();
            Account activeAccount = authAccountManager.getActiveAccount(getString(R.string.authentication_ACCOUNT));
            AccountManager.get(this).getAuthToken(activeAccount, getString(R.string.authentication_TOKEN),
                    null, this, new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                Bundle tokenBundle = future.getResult();
                                String authToken = tokenBundle.getString(AccountManager.KEY_AUTHTOKEN);

                                if (authToken != null) {
                                    headerMap.put("Authorization", authToken);
                                }

                                mWebView.loadUrl(mUrl, headerMap);

                            } catch (Exception e) {
                                Log.e(LOG_TAG, e.getMessage(), e);
                            }
                        }
                    }, null);
        }else{
            mWebView.loadUrl(mUrl);
        }

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d(LOG_TAG, "APP Permission Requested");
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
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.clearFormData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(getApplicationContext());
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }

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
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }

        finish();
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

        Log.d(LOG_TAG, "APP onDownloadRequested Triggered");

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
                .subscribe(new rx.Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP Completed WebView Download");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "ERROR onError " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(final String location) {
                        Log.d(LOG_TAG, "APP file is now in " + location);

                        final Snackbar snackbar = SnackBarUtil
                                .snackBarForSuccessCreate(rootView, mLabelDownloadedTo + location,
                                        Snackbar.LENGTH_LONG)
                                .setAction(mLabelOpen, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    File downloadFile = new File(new URI(location));

                                    intent.setDataAndType(Uri.fromFile(downloadFile), usageMimeType);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    Intent chooserIntent = Intent.createChooser(intent, mLabelIntentFile);


                                    startActivity(chooserIntent);
                                }catch(URISyntaxException ue){
                                    Log.e(LOG_TAG, "ERROR " + ue.getMessage(), ue);
                                    final Snackbar snackbar = SnackBarUtil
                                            .snackBarForErrorCreate(rootView, mLabelErrorOpenFile,
                                                    Snackbar.LENGTH_LONG);

                                    snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                }catch (ActivityNotFoundException anfe){
                                    Log.e(LOG_TAG, "ERROR " + anfe.getMessage(), anfe);
                                    final Snackbar snackbar = SnackBarUtil.snackBarForErrorCreate(
                                            rootView, mLabelErrorOpenFile,
                                            Snackbar.LENGTH_LONG);

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
                if(mWebView.canGoBack()){
                    //cut off and go to prev activity
                    super.onBackPressed();
                    return true;
                }
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context){
            mContext = context;
        }

        @JavascriptInterface
        public void goBack(){
            Log.d(LOG_TAG, "APP JavaScriptInterface goBack() called");
            finish();
        }

        @JavascriptInterface
        public void goToListing(){
            Log.d(LOG_TAG, "APP JavaScriptInterface goBackToListing() called");
            Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void authSuccess(String email, String name, String authToken, String locale){
            Intent data = new Intent();
            data.putExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_EMAIL_EXTRA, email);
            data.putExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_NAME_EXTRA, name);
            data.putExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_TOKEN_EXTRA, authToken);
            data.putExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_LOCALE_EXTRA, locale);
            setResult(RESULT_OK, data);
            finish();
        }

        @JavascriptInterface
        public void authFailed(String reason){
            Intent data = new Intent();
            data.putExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_FAILURE_EXTRA, reason);
            setResult(RESULT_CANCELED, data);
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
