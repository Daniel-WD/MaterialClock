package danielweidensdoerfer.com.clock;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

public class Module {

    private float x, y;

    private final String[] mContent;
    private int mIndex = 0;

    private float mOffsetY = 0;
    private float mDef = 0;

    private TextPaint mTPaint;

    private Clock mClock;

    private float mMoveDistance = 0;

    private ObjectAnimator anim;

    Module(Clock clock, float x, float y, String[] content) {
        mContent = content;
        mClock = clock;

        this.x = x;
        this.y = y;

        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setColor(clock.getContext().getColor(R.color.snow));
        mTPaint.setTextSize(clock.getContext().getResources().getDimensionPixelSize(R.dimen.textSize));
        mTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        mTPaint.setTextAlign(Paint.Align.LEFT);

        mDef = -(mTPaint.descent() + mTPaint.ascent()) / 2;

        mMoveDistance = 1.5f*mTPaint.getTextSize();

        anim = ObjectAnimator.ofFloat(this, "offsetY", mMoveDistance);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationEnd(Animator animation) {
                mIndex = nextIndex(mIndex);
                setOffsetY(0);
            }
            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);

        canvas.drawText(mContent[nextIndex(nextIndex(mIndex))], 0,
                mOffsetY + mDef -2*mMoveDistance, mTPaint);
        canvas.drawText(mContent[nextIndex(mIndex)], 0,
                mOffsetY + mDef -mMoveDistance, mTPaint);
        canvas.drawText(mContent[mIndex], 0,
                mOffsetY + mDef, mTPaint);
        canvas.drawText(mContent[previousIndex(mIndex)], 0,
                mOffsetY + mDef +mMoveDistance, mTPaint);

        canvas.restore();
    }

    private int nextIndex(int i) {
        return i+1 >= mContent.length ? 0 : i+1;
    }

    private int previousIndex(int i) {
        return i-1 < 0 ? mContent.length-1 : i-1;
    }

    void next() {
        anim.start();
    }

    void set(String value) {
        for(int i = 0; i < mContent.length; i++) {
            if(value.equals(mContent[i])) {
                mIndex = i;
                mClock.invalidate();
                break;
            }
        }
    }

    public void setOffsetY(float offset) {
        this.mOffsetY = offset;
        mClock.invalidate();
    }

}
