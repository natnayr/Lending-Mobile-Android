package com.crowdo.p2pconnect.custom_ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.crowdo.p2pconnect.R;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 5/6/17.
 */

public class CartBadgeDrawable extends Drawable{

    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();

    private String mCount = "";
    private boolean mWillDraw;

    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_accent) int mColorAccent;


    public CartBadgeDrawable(Activity activity) {
        ButterKnife.bind(this, activity);
        Context context = activity.getApplicationContext();

        float mTextSize = context.getResources().getDimension(R.dimen.toolbar_badge_text_size);

        mBadgePaint = new Paint();
        mBadgePaint.setColor(mColorAccent);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(mColorIconText);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        if(!mWillDraw){
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        //badge position in top right quadrant
        //using Math.max rather than Math.min

        float radius = ((Math.max(width, height)/ 2)) / 2;
        float centerX = (width - radius - 1) + 8;
        float centerY = radius - 8;

        if(mCount.length() <= 2){
            //draw badge circle
            canvas.drawCircle(centerX, centerY, (int)(radius+3.5), mBadgePaint);
        }else{
            //double digits
            canvas.drawCircle(centerX, centerY, (int) (radius + 6.5), mBadgePaint);
        }

        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        if(mCount.length() > 2){
            canvas.drawText("99+", centerX, textY, mTextPaint);
        }else{
            canvas.drawText(mCount, centerX, textY, mTextPaint);
        }

    }

    public void setCount(String count){
        mCount = count;

        mWillDraw = !count.equalsIgnoreCase("0");
        invalidateSelf();

    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        //do nothing
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        //do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public static void setBadgeCount(Activity activity, LayerDrawable icon,
                                     String count){
        CartBadgeDrawable cartBadge;

        //reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if(reuse != null && reuse instanceof CartBadgeDrawable){
            cartBadge = (CartBadgeDrawable) reuse;
        }else{
            cartBadge = new CartBadgeDrawable(activity);
        }

        cartBadge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, cartBadge);
    }
}
