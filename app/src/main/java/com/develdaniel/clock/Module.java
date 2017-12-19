package com.develdaniel.clock;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.view.animation.AccelerateInterpolator;

public class Module {

    private float mX, mY, mTransX = 0, mTransY = 0;
    private float mTransRX = 0, mTransRY = 0,
            mTransGX = 0, mTransGY = 0,
            mTransBX = 0, mTransBY = 0;

    private String[] mContent = new String[] {"-", "-"};

    private float mOffsetY = 0;
    private float mTYMani = 0;

    private TextPaint mTPaint;
    private int mColor, mRColor, mBColor, mGColor;

    private Clock mClock;

    private float mMoveDistance = 0;

    private ObjectAnimator anim;

    private float mVibrationFactor = 1;

    Module(Clock clock, float vFactor, float x, float y) {
        mClock = clock;

        this.mX = x;
        this.mY = y;
        mVibrationFactor = vFactor;

        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setTextSize(clock.getContext().getResources().getDimensionPixelSize(R.dimen.textSize));
        mTPaint.setTypeface(ResourcesCompat.getFont(clock.getContext(), R.font.black_ops_one_regular));
        mTPaint.setTextAlign(Paint.Align.CENTER);
        mTPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        mColor = ContextCompat.getColor(clock.getContext(), R.color.snow);
        mRColor = Color.rgb(Color.red(mColor), 0, 0);
        mGColor = Color.rgb(0, Color.green(mColor), 0);
        mBColor = Color.rgb(0, 0, Color.blue(mColor));

        mTYMani = -(mTPaint.descent() + mTPaint.ascent()) / 2;

        mMoveDistance = 1.5f*mTPaint.getTextSize();

        anim = ObjectAnimator.ofFloat(this, "offsetY", -mMoveDistance, 0);
        anim.setDuration(700);
        anim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
            @Override public void onAnimationEnd(Animator animation) {
                mClock.vibrate(mVibrationFactor);
            }
        });
        anim.setInterpolator(new AccelerateInterpolator(4));
    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mX + mTransX, mY + mTransY);

        //normal
        /*mTPaint.setColor(mColor);
        canvas.drawText(mContent[0], 0, mOffsetY + mTYMani, mTPaint);
        canvas.drawText(mContent[1], 0, mOffsetY + mTYMani + mMoveDistance, mTPaint);*/

        //red
        mTPaint.setColor(mRColor);
        canvas.drawText(mContent[0], mTransRX, mOffsetY + mTYMani + mTransRY, mTPaint);
        canvas.drawText(mContent[1], mTransRX, mOffsetY + mTYMani + mMoveDistance + mTransRY, mTPaint);

        //green
        mTPaint.setColor(mGColor);
        canvas.drawText(mContent[0], mTransGX, mOffsetY + mTYMani + mTransGY, mTPaint);
        canvas.drawText(mContent[1], mTransGX, mOffsetY + mTYMani + mMoveDistance + mTransGY, mTPaint);

        //blue
        mTPaint.setColor(mBColor);
        canvas.drawText(mContent[0], mTransBX, mOffsetY + mTYMani + mTransBY, mTPaint);
        canvas.drawText(mContent[1], mTransBX, mOffsetY + mTYMani + mMoveDistance + mTransBY, mTPaint);

        canvas.restore();
    }

    boolean next(String text) {
        if(mContent[0].equals(text)) return false;
        mContent[1] = mContent[0];
        mContent[0] = text;
        anim.start();
        return true;
    }

//    void vibrate() {
//        Random r = new Random();
//        int xRange = 20;
//        int yRange = 5;
//
//        ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
//        anim.addUpdateListener(animation -> {
//            float v = (float) animation.getAnimatedValue();
//            mTransX = v * (r.nextInt(xRange)-xRange/2);
//            //mTransY = v * (r.nextInt(yRange)-yRange/2);
//            mClock.invalidate();
//        });
//        anim.addListener(new Animator.AnimatorListener() {
//            @Override public void onAnimationStart(Animator animation) {}
//            @Override public void onAnimationCancel(Animator animation) {}
//            @Override public void onAnimationRepeat(Animator animation) {}
//            @Override public void onAnimationEnd(Animator animation) {
//                mTransX = 0;
//                mTransY = 0;
//            }
//        });
//        anim.setInterpolator(new DecelerateInterpolator());
//        anim.setDuration(100);
//        anim.start();
//    }

    public void setOffsetY(float offset) {
        this.mOffsetY = offset;
        mClock.invalidate();
    }

    void setTranslationX(float tX) {
        mTransX = tX;
        mClock.invalidate();
    }

    void setTranslationY(float tY) {
        mTransY = tY;
        mClock.invalidate();
    }

    void setTranslationRX(float tX) {
        mTransRX = tX;
        mClock.invalidate();
    }

    void setTranslationRY(float tY) {
        mTransRY = tY;
        mClock.invalidate();
    }

    void setTranslationGX(float tX) {
        mTransGX = tX;
        mClock.invalidate();
    }

    void setTranslationGY(float tY) {
        mTransGY = tY;
        mClock.invalidate();
    }

    void setTranslationBX(float tX) {
        mTransBX = tX;
        mClock.invalidate();
    }

    void setTranslationBY(float tY) {
        mTransBY = tY;
        mClock.invalidate();
    }

    public void setColor(int color) {
        mTPaint.setColor(color);
    }

}
