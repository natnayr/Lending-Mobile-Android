package com.crowdo.p2pmobile.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import com.crowdo.p2pmobile.R;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryan on 19/10/16.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();
    @BindView(R.id.welcome_video_view) VideoView mVideoView;
    @BindView(R.id.welcome_get_started_btn) Button mWelcomeGetStartedButton;
    @BindDrawable(R.drawable.welcome_get_started_btn_enable) Drawable mWelcomeGetStartedButtonEnabled;
    @BindDrawable(R.drawable.welcome_get_started_btn_pressed) Drawable mWelcomeGetStartedButtonPressed;

    private int stopPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        try {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.crowdo_phone_480p);
            mVideoView.setVideoURI(video);
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            mVideoView.requestFocus();
            mVideoView.start();
        }catch (Exception e){
            Log.e(LOG_TAG, "ERROR: mVideoView caught error " + e.getMessage(), e);
        }


        mWelcomeGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Intent intent = new Intent(WelcomeActivity.this, LoanListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.seekTo(stopPosition);
        mVideoView.start();
    }

}
