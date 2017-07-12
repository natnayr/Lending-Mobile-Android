package com.crowdo.p2pconnect.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crowdo.p2pconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/7/17.
 */

public class TopUpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
    }

}
