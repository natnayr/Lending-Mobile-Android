package com.crowdo.p2pmobile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    @BindView(R.id.welcome_pager) ViewPager mViewPager;
    @BindView(R.id.welcome_pager_tabdots) TabLayout mTabLayout;

    private int stopPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        try {
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.crowdo_video_phone);
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

        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setAdapter(new WelcomePagerAdapter(this));
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


    class WelcomePagerAdapter extends PagerAdapter{
        private Context mContext;

        public WelcomePagerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            WelcomeLayoutEnum welcomeEnum = WelcomeLayoutEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layoutView = (ViewGroup)
                    inflater.inflate(welcomeEnum.getLayoutResId(), collection, false);
            collection.addView(layoutView);
            return layoutView;
        }

        @Override
        public int getCount() {
            return WelcomeLayoutEnum.values().length;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    enum WelcomeLayoutEnum {

        FIRST_INTRO(R.layout.welcome_intro_1),
        SECOND_INTRO(R.layout.welcome_intro_2),
        THIRD_INTRO(R.layout.welcome_intro_3);

        private int mLayoutResId;

        WelcomeLayoutEnum(int layoutResId){
            this.mLayoutResId = layoutResId;
        }

        public int getLayoutResId() {
            return this.mLayoutResId;
        }
    }

}
