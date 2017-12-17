package danielweidensdoerfer.com.clock;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

public class Module {

    private float x, y;

    private String[] mContent = new String[] {"0", "0"};

    private float mOffsetY = 0;
    private float mDef = 0;

    private TextPaint mTPaint;

    private Clock mClock;

    private float mMoveDistance = 0;

    private ObjectAnimator anim;

    Module(Clock clock, float x, float y) {
        mClock = clock;

        this.x = x;
        this.y = y;

        mTPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTPaint.setColor(clock.getContext().getColor(R.color.snow));
        mTPaint.setTextSize(clock.getContext().getResources().getDimensionPixelSize(R.dimen.textSize));
        mTPaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        mTPaint.setTextAlign(Paint.Align.CENTER);

        mDef = -(mTPaint.descent() + mTPaint.ascent()) / 2;

        mMoveDistance = 1.5f*mTPaint.getTextSize();

        anim = ObjectAnimator.ofFloat(this, "offsetY", -mMoveDistance, 0);
        anim.setDuration(900);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);

        canvas.drawText(mContent[0], 0,
                mOffsetY + mDef, mTPaint);
        canvas.drawText(mContent[1], 0,
                mOffsetY + mDef +mMoveDistance, mTPaint);

        canvas.restore();
    }

    void next(String text) {
        if(mContent[0].equals(text)) return;
        mContent[1] = mContent[0];
        mContent[0] = text;
        anim.start();
    }

    public void setOffsetY(float offset) {
        this.mOffsetY = offset;
        mClock.invalidate();
    }

    public void setColor(int color) {
        mTPaint.setColor(color);
    }

}
