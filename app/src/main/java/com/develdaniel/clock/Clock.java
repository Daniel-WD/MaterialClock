package com.develdaniel.clock;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Clock extends View {

    private static final String[] NUMBERS_10 = {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"
    };

    private float mWidth, mHeight, mCenterX, mCenterY;

    private float mModuleWidth, mOffsetX;

    private ArrayList<Module> mModules = new ArrayList<>();

    private Handler mHandler = new Handler();

    private Paint mPTBlur, mPBBlur, mPBlack;
    private float mTBBound, mBBBound, mBHeight = 10;

    //clock
    private Module mModSecRight, mModSecLeft,
            mModMinRight, mModMinLeft,
            mModHrsRight, mModHrsLeft,
            mModColonLeft, mModColonRight;

    private boolean mFirstLoop = true;
    private boolean mSecondChange = false;
    private String mLastRSecond = "-";
    private Runnable mClockLoop = new Runnable() {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();

            if(mFirstLoop) {
                mLastRSecond = Utils.getRightNumber(calendar.get(Calendar.SECOND));
                mFirstLoop = false;
            }

            String curRSec = Utils.getRightNumber(calendar.get(Calendar.SECOND));
            if(!curRSec.equals(mLastRSecond)) {
                mSecondChange = true;
            }
            if(mSecondChange) {
                mModColonLeft.next(":");
                mModColonRight.next(":");

                mModSecRight.next(Utils.getRightNumber(calendar.get(Calendar.SECOND)));
                mModSecLeft.next(Utils.getLeftNumber(calendar.get(Calendar.SECOND)));
                mModMinRight.next(Utils.getRightNumber(calendar.get(Calendar.MINUTE)));
                mModMinLeft.next(Utils.getLeftNumber(calendar.get(Calendar.MINUTE)));
                mModHrsRight.next(Utils.getRightNumber(calendar.get(Calendar.HOUR_OF_DAY)));
                mModHrsLeft.next(Utils.getLeftNumber(calendar.get(Calendar.HOUR_OF_DAY)));
            }

            mHandler.postDelayed(mClockLoop, 200);
        }
    };

    private float mBgHue = 0;
    private float[] mBgHsv = new float[] {0, 1f, 0.8f};
    private Runnable mColorLoop = new Runnable() {
        @Override
        public void run() {
            if(++mBgHue > 360) mBgHue = 0;
            mBgHsv[0] = mBgHue;
//            for (Module module : mModules) {
//                module.setColor();
//            }
            //setBackgroundColor(Color.HSVToColor(mBgHsv));
            //mHandler.postDelayed(this, 1000);
        }
    };

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    void vibrate(float factor) {
        if(factor <= 0) return;
        Random r = new Random();
        int xRange = (int)(20 * factor);
        int yRange = (int)(20 * factor);

        ValueAnimator anim = ValueAnimator.ofFloat(1, 0);
        anim.addUpdateListener(animation -> {
            float f = (float) animation.getAnimatedValue();
            //setTranslationX(f * (r.nextInt(xRange)-xRange/2));
            //setTranslationY(f * (r.nextInt(yRange)-yRange/2));
            float tX, tY;
            //red
            /*tX = f * (r.nextInt(xRange)-xRange/2);
            tY = f * (r.nextInt(yRange)-yRange/2);
            for(Module module : mModules) {
                module.setTranslationRX(0.5f * tX);
                module.setTranslationRY(0.5f  * tY);
            }
            //green
            tX = f * (r.nextInt(xRange)-xRange/2);
            tY = f * (r.nextInt(yRange)-yRange/2);
            for(Module module : mModules) {
                module.setTranslationGX(tX);
                module.setTranslationGY(tY);
            }
            //blue
            tX = f * (r.nextInt(xRange)-xRange/2);
            tY = f * (r.nextInt(yRange)-yRange/2);
            for(Module module : mModules) {
                module.setTranslationBX(0.5f * tX);
                module.setTranslationBY(0.5f * tY);
            }*/
            //pos
            tX = f * (r.nextInt(xRange)-xRange/2);
            tY = f * (r.nextInt(yRange)-yRange/2);
            for(Module module : mModules) {
                module.setTranslationX(tX);
                module.setTranslationY(tY);
            }

            invalidate();
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
            @Override public void onAnimationEnd(Animator animation) {
//                setTranslationX(0);
//                setTranslationY(0);
                for (Module module : mModules) {
                    module.setTranslationX(0);
                    module.setTranslationY(0);
                }
            }
        });
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration((int)(100 * factor));
        anim.start();
    }

    private void init() {
        mModules.clear();

        float textSize = getResources().getDimensionPixelSize(R.dimen.textSize);

        mModuleWidth = Utils.widthBySize(getContext(), textSize, NUMBERS_10);
        mOffsetX = (mWidth-(8*mModuleWidth))/2 + mModuleWidth/2;

        //clock
        mModHrsLeft = new Module(this, 6/*6*/, mOffsetX + mModuleWidth*0, mCenterY , textSize);
        mModHrsRight = new Module(this, 5/*5*/, mOffsetX + mModuleWidth*1, mCenterY, textSize);
        mModColonLeft = new Module(this, 1/*1*/, mOffsetX + mModuleWidth*2, mCenterY, textSize);
        mModMinLeft = new Module(this, 4/*4*/, mOffsetX + mModuleWidth*3, mCenterY, textSize);
        mModMinRight = new Module(this, 3/*3*/, mOffsetX + mModuleWidth*4, mCenterY, textSize);
        mModColonRight = new Module(this, 1/*1*/, mOffsetX + mModuleWidth*5, mCenterY, textSize);
        mModSecLeft = new Module(this, 2/*2*/, mOffsetX + mModuleWidth*6, mCenterY, textSize);
        mModSecRight = new Module(this, 1/*1*/, mOffsetX + mModuleWidth*7, mCenterY, textSize);

        mModules.add(mModSecRight);
        mModules.add(mModSecLeft);
        mModules.add(mModColonLeft);
        mModules.add(mModMinRight);
        mModules.add(mModMinLeft);
        mModules.add(mModColonRight);
        mModules.add(mModHrsRight);
        mModules.add(mModHrsLeft);

        textSize *= 1.2;

        mTBBound = mCenterY - textSize/2;
        mBBBound = mCenterY + textSize/2;

        mPTBlur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPTBlur.setStyle(Paint.Style.FILL);
        mPTBlur.setShader(new LinearGradient(0, mTBBound - mBHeight /2, 0, mTBBound - mBHeight /2 + mBHeight,
                Color.BLACK, Color.TRANSPARENT, Shader.TileMode.MIRROR));

        mPBBlur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPBBlur.setStyle(Paint.Style.FILL);
        mPBBlur.setShader(new LinearGradient(0, mBBBound - mBHeight /2, 0, mBBBound - mBHeight /2 + mBHeight,
                Color.TRANSPARENT, Color.BLACK, Shader.TileMode.MIRROR));

        mPBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPBlack.setColor(Color.BLACK);
        mPBlack.setStyle(Paint.Style.FILL);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(mClockLoop);
        mHandler.post(mColorLoop);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Module module : mModules) {
            module.draw(canvas);
        }

        //black top
        canvas.drawRect(0, 0, mWidth, mTBBound - mBHeight /2, mPBlack);
        //blur top
        canvas.drawRect(0, mTBBound - mBHeight /2, mWidth, mTBBound - mBHeight /2 + mBHeight, mPTBlur);
        //blur top
        canvas.drawRect(0, mBBBound - mBHeight /2, mWidth, mBBBound - mBHeight /2 + mBHeight, mPBBlur);
        //black bottom
        canvas.drawRect(0, mBBBound + mBHeight /2, mWidth, mHeight, mPBlack);

    }
}
