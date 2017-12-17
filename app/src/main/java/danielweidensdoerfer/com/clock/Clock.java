package danielweidensdoerfer.com.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

    private float mWidth, mHeight, mCenterX, mCenterY;

    private float mModuleWidth, mOffsetX;

    private ArrayList<Module> mModules = new ArrayList<>();

    private Handler mHandler = new Handler();

    private Paint mPTBlur, mPBBlur, mPBlack;
    private float mTBBound, mBBBound, mBHeight = 30;

    private Module mModSecRight, mModSecLeft,
            mModMinRight, mModMinLeft,
            mModHrsRight, mModHrsLeft,
            mModColonLeft, mModColonRight;

    private Runnable loop = new Runnable() {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            mModColonLeft.next(":");
            mModColonRight.next(":");

            mModSecRight.next(Utils.getRightNumber(calendar.get(Calendar.SECOND)));
            mModSecLeft.next(Utils.getLeftNumber(calendar.get(Calendar.SECOND)));
            mModMinRight.next(Utils.getRightNumber(calendar.get(Calendar.MINUTE)));
            mModMinLeft.next(Utils.getLeftNumber(calendar.get(Calendar.MINUTE)));
            mModHrsRight.next(Utils.getRightNumber(calendar.get(Calendar.HOUR_OF_DAY)));
            mModHrsLeft.next(Utils.getLeftNumber(calendar.get(Calendar.HOUR_OF_DAY)));

            mHandler.postDelayed(loop, 200);
        }
    };

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        mModules.clear();

        float textSize = getResources().getDimensionPixelSize(R.dimen.textSize);

        mModuleWidth = Utils.widthBySize(textSize, NUMBERS_10);
        mOffsetX = (mWidth-(8*mModuleWidth))/2 + mModuleWidth/2;

        mModHrsLeft = new Module(this, mOffsetX + mModuleWidth*0, mCenterY);
        mModHrsRight = new Module(this, mOffsetX + mModuleWidth*1, mCenterY);
        mModColonLeft = new Module(this, mOffsetX + mModuleWidth*2, mCenterY);
        mModMinLeft = new Module(this, mOffsetX + mModuleWidth*3, mCenterY);
        mModMinRight = new Module(this, mOffsetX + mModuleWidth*4, mCenterY);
        mModColonRight = new Module(this, mOffsetX + mModuleWidth*5, mCenterY);
        mModSecLeft = new Module(this, mOffsetX + mModuleWidth*6, mCenterY);
        mModSecRight = new Module(this, mOffsetX + mModuleWidth*7, mCenterY);

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
