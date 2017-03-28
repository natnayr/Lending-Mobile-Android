package com.crowdo.p2pconnect.view.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
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
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.TypefaceUtils;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryan on 19/10/16.
 */
public class LaunchActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{

    @BindView(R.id.welcome_get_started_btn) Button mWelcomeGetStartedButton;
    @BindView(R.id.welcome_login_btn) Button mWelcomeLoginButton;
    @BindView(R.id.welcome_register_btn) Button mWelcomeRegisterButton;
    @BindView(R.id.welcome_pager) ViewPager mViewPager;
    @BindView(R.id.welcome_pager_tabdots) TabLayout mTabLayout;
    @BindString(R.string.pre_exit_question) String mPreExitQuestion;
    @BindView(R.id.welcome_logo_app_name) TextView mWelcomeAppNameView;

    private static final String LOG_TAG = LaunchActivity.class.getSimpleName();
    private static final int MAXPAGE = 2;
    private static final int TIMEFRAME = 13180;

    private int stopPosition;
    private  MediaPlayer mPlayer;
    private SurfaceHolder mHolder;
    private Uri videoUri;
    private Context mContext;
    private SurfaceView mSurfaceView;

    private Timer mTimer;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);

        videoUri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.crowdo_video_phone);

        this.page = 0;
        mContext = this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSurfaceView = (SurfaceView) findViewById(R.id.welcome_surface_view);

            if(mSurfaceView != null) {
                mHolder = mSurfaceView.getHolder();
                mHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        mPlayer = new MediaPlayer();
                        mPlayer.setDisplay(mHolder);
                        mPlayer.setLooping(true);
                        try {
                            mPlayer.setDataSource(mContext, videoUri);
                            mPlayer.setOnPreparedListener(LaunchActivity.this);
                            mPlayer.prepare();

                        } catch (IOException e) {
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
            }
        }

        mWelcomeGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        mWelcomeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LaunchActivity.this, AuthActivity.class);
                intent.putExtra(AuthActivity.FRAGMENT_CLASS_TAG_CALL, LoginFragment.LOGIN_FRAGMENT_TAG);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        mWelcomeRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = Henson.with(LaunchActivity.this)
//                        .gotoAuthActivity()
//                        .fragmentClass(RegisterFragment.class)
//                        .build();
                Intent intent = new Intent(LaunchActivity.this, AuthActivity.class);
                intent.putExtra(AuthActivity.FRAGMENT_CLASS_TAG_CALL, RegisterFragment.REGISTER_FRAGMENT_TAG);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setAdapter(new WelcomePagerAdapter(this));

        Typeface nycdTypeFace = TypefaceUtils.getNothingYouCouldDoTypeFace(this);
        mWelcomeAppNameView.setTypeface(nycdTypeFace);

        //Backwards Compatibility
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pageSwitcher(TIMEFRAME);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pageSwitcher(int timeframeEach){
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new FlipPageTimerTask(), 0, timeframeEach);
    }

    class FlipPageTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(page >= MAXPAGE) {
                        page = 0;
                    }
                    mViewPager.setCurrentItem(page++);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            finishAndRemoveTask();
        }else{
            finishAffinity();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
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
        if (mPlayer != null) {
            mPlayer.seekTo(stopPosition);
            mPlayer.start();
        }
    }

    class WelcomePagerAdapter extends PagerAdapter {
        private Context mContext;

        public WelcomePagerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LaunchLayoutEnum welcomeEnum = LaunchLayoutEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layoutView = (ViewGroup)
                    inflater.inflate(welcomeEnum.getLayoutResId(), collection, false);
            collection.addView(layoutView);
            return layoutView;
        }

        @Override
        public int getCount() {
            return LaunchLayoutEnum.values().length;
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

    enum LaunchLayoutEnum {

        ONE(R.layout.welcome_intro_1),
        TWO(R.layout.welcome_intro_2);

        private int mLayoutResId;

        LaunchLayoutEnum(int layoutResId){
            this.mLayoutResId = layoutResId;
        }

        public int getLayoutResId() {
            return this.mLayoutResId;
        }
    }


}
