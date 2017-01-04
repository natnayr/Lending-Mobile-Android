package com.crowdo.p2pmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.HensonNavigable;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.processor.HensonNavigatorGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 4/1/17.
 */

public class WebViewActivity extends AppCompatActivity {

    public static final String LOG_TAG = WebViewActivity.class.getSimpleName();

    @BindView(R.id.webview) WebView webview;
    @BindView(R.id.toolbar_webview) Toolbar toolbar;
    @InjectExtra public int id;
    @InjectExtra public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.bind(this);

        //toolbar view
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //inject intent settings
        Dart.inject(this);
        webview.setWebViewClient(new EmbedWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

    private class EmbedWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
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
}
