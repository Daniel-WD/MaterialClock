package danielweidensdoerfer.com.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

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
    private static final String[] NUMBERS_6 = {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5"
    };

    private float mWidth, mHeight, mCenterX, mCenterY;

    private ArrayList<Module> mModules = new ArrayList<>();

    private Handler mHandler = new Handler();

    private Module mModSecRight, mModSecLeft,
            mModMinRight, mModMinLeft,
            mModHrsRight, mModHrsLeft;

    private String mLastSecRight, mLastSecLeft, mLastMinRight, mLastMinLeft, mLastHrsRight, mLastHrsLeft;
    private String mCurSecRight, mCurSecLeft, mCurMinRight, mCurMinLeft, mCurHrsRight, mCurHrsLeft;

    private Runnable loop = new Runnable() {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            mCurSecRight = Utils.getRightNumber(calendar.get(Calendar.SECOND));
            if(!mCurSecRight.equals(mLastSecRight)) {
                Log.d("sldjf", "sdlkf:::" + mCurSecRight);
                mModSecRight.next();
                mLastSecRight = mCurSecRight;
            }
            mCurSecLeft = Utils.getLeftNumber(calendar.get(Calendar.SECOND));
            if(!mCurSecLeft.equals(mLastSecLeft)) {
                mModSecLeft.next();
                mLastSecLeft = mCurSecLeft;
            }
            mCurMinRight = Utils.getRightNumber(calendar.get(Calendar.MINUTE));
            if(!mCurMinRight.equals(mLastMinRight)) {
                mModMinRight.next();
                mLastMinRight = mCurMinRight;
            }
            mCurMinLeft = Utils.getLeftNumber(calendar.get(Calendar.MINUTE));
            if(!mCurMinLeft.equals(mLastMinLeft)) {
                mModMinLeft.next();
                mLastMinLeft = mCurMinLeft;
            }
            mCurHrsRight = Utils.getRightNumber(calendar.get(Calendar.HOUR_OF_DAY));
            if(!mCurHrsRight.equals(mLastHrsRight)) {
                mModHrsRight.next();
                mLastHrsRight = mCurHrsRight;
            }
            mCurHrsLeft = Utils.getLeftNumber(calendar.get(Calendar.HOUR_OF_DAY));
            if(!mCurHrsLeft.equals(mLastHrsLeft)) {
                mModHrsLeft.next();
                mLastHrsLeft = mCurHrsLeft;
            }

            mHandler.postDelayed(loop, 500);
        }
    };

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    private void init() {
        mModules.clear();

        float moduleWidth = Utils.widthBySize(getResources().getDimensionPixelSize(R.dimen.textSize), NUMBERS_10);

        float offset = (mWidth-(8*moduleWidth))/2;

        mModHrsLeft = new Module(this, offset + moduleWidth*0, mCenterY, NUMBERS_10);
        mModHrsRight = new Module(this, offset + moduleWidth*1, mCenterY, NUMBERS_10);
        mModMinLeft = new Module(this, offset + moduleWidth*3, mCenterY, NUMBERS_6);
        mModMinRight = new Module(this, offset + moduleWidth*4, mCenterY, NUMBERS_10);
        mModSecLeft = new Module(this, offset + moduleWidth*6, mCenterY, NUMBERS_6);
        mModSecRight = new Module(this, offset + moduleWidth*7, mCenterY, NUMBERS_10);

        mModules.add(mModSecRight);
        mModules.add(mModSecLeft);
        mModules.add(mModMinRight);
        mModules.add(mModMinLeft);
        mModules.add(mModHrsRight);
        mModules.add(mModHrsLeft);

        Calendar c = Calendar.getInstance();
        mLastSecRight = Utils.getRightNumber(c.get(Calendar.SECOND));
        mLastSecLeft = Utils.getLeftNumber(c.get(Calendar.SECOND));
        mLastMinRight = Utils.getRightNumber(c.get(Calendar.MINUTE));
        mLastMinLeft = Utils.getLeftNumber(c.get(Calendar.MINUTE));
        mLastHrsRight = Utils.getRightNumber(c.get(Calendar.HOUR_OF_DAY));
        mLastHrsLeft = Utils.getLeftNumber(c.get(Calendar.HOUR_OF_DAY));

        mModSecRight.set(mLastSecRight);
        mModSecLeft.set(mLastSecLeft);
        mModMinRight.set(mLastMinRight);
        mModMinLeft.set(mLastMinLeft);
        mModHrsRight.set(mLastHrsRight);
        mModHrsLeft.set(mLastHrsLeft);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(loop);
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
    }
}
