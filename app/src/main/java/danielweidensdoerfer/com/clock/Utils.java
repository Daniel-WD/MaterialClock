package danielweidensdoerfer.com.clock;

import android.graphics.Paint;
import android.graphics.Typeface;

public class Utils {

    static float widthBySize(float size, String[] strings) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        float result = 0;
        for(String s : strings) {
            result = Math.max(paint.measureText(s), result);
        }
        return result;
    }

    static String getRightNumber(int number) {
        String s = String.valueOf(number);
        return String.valueOf(s.charAt(s.length()-1));
    }

    static String getLeftNumber(int number) {
        String s = String.valueOf(number);
        if(s.length() < 2) return "0";
        return String.valueOf(s.charAt(s.length()-2));
    }

}
