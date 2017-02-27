package com.crowdo.p2pconnect.view.activities;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.view.fragments.LoanDetailsFragment;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanDetailsActivity extends AppCompatActivity {

    public static final String BUNDLE_ID_KEY = "BundleDetailsFragmentIDKey";
    @InjectExtra public int id;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);
        ButterKnife.bind(this);

        //mToolbar view
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.toolbar_title_loan_detail));
        LoanDetailsActivity.this.setTitle(getString(R.string.toolbar_title_loan_detail));

        //enable back buttons
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_ID_KEY, this.id);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT);

        if(fragment == null) {
            LoanDetailsFragment loanDetailsFragment = new LoanDetailsFragment();
            loanDetailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.loan_details_content, loanDetailsFragment,
                            LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT)
                    .addToBackStack(LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
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
                    public void onAnimationStart(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mToolbar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });

    }
}
