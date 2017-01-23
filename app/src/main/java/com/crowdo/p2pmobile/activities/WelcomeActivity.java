package com.crowdo.p2pmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.crowdo.p2pmobile.R;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryan on 19/10/16.
 */

public class WelcomeActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{

    private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();
    @BindView(R.id.welcome_get_started_btn) Button mWelcomeGetStartedButton;
    @BindView(R.id.welcome_pager) ViewPager mViewPager;
    @BindView(R.id.welcome_pager_tabdots) TabLayout mTabLayout;

    private SurfaceView mSurfaceView;
    private int stopPosition;
    private  MediaPlayer mPlayer;
    private SurfaceHolder mHolder;
    private Uri videoUri;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        videoUri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.crowdo_video_phone);

        mContext = this;

        mSurfaceView = (SurfaceView) findViewById(R.id.welcome_surface_view);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mPlayer = new MediaPlayer();
                mPlayer.setDisplay(mHolder);
                mPlayer.setLooping(true);
                try{
                    mPlayer.setDataSource(mContext, videoUri);
                    mPlayer.setOnPreparedListener(WelcomeActivity.this);
                    mPlayer.prepare();

                }catch (IOException e){
                    Log.e(LOG_TAG, "ERROR: surfaceCreated error " + e.getMessage(), e);
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

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
    public void onPrepared(MediaPlayer mp) {
        int videoWidth = mPlayer.getVideoWidth();
        int videoHeight = mPlayer.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        float screenProportion = (float) screenWidth / (float) screenHeight;
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        if(videoProportion > screenProportion){
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        }else{
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }

        mSurfaceView.setLayoutParams(lp);
        if(!mPlayer.isPlaying()){
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPlayer != null) {
            stopPosition = mPlayer.getCurrentPosition();
            mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPlayer != null) {
            mPlayer.seekTo(stopPosition);
            mPlayer.start();
        }
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

        ONE(R.layout.welcome_intro_1),
        TWO(R.layout.welcome_intro_2),
        THREE(R.layout.welcome_intro_3);

        private int mLayoutResId;

        WelcomeLayoutEnum(int layoutResId){
            this.mLayoutResId = layoutResId;
        }

        public int getLayoutResId() {
            return this.mLayoutResId;
        }
    }

}
