package com.yooiistudios.morningkit.common.dp;

/**
 * Created by yongbinbae on 2013. 10. 24..
 *
 * DipToPixel (유틸리티 클래스)
 *  1. dp -> pixel
 *  2. pixel -> dp
 */

import android.content.Context;
import android.util.TypedValue;

public class DipToPixel {

    private DipToPixel() { throw new AssertionError(); } // You must not create instance
    public static float getDisplayMetricsDensity(Context context)
    {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getPixel(Context context, int dp)
    {
        double density = getDisplayMetricsDensity(context);
        if(density != 1)
        {
            return (int)(dp * density + 0.5);
        }
        return dp;
    }

    public static double getPixel(Context context, double dp)
    {
        double density = getDisplayMetricsDensity(context);
        if(density != 1)
        {
            return (double) (dp * density + 0.5);
        }
        return dp;
    }

    public static float getPixel(Context context, float dp)
    {
        float density = getDisplayMetricsDensity(context);
        if(density != 1)
        {
            return (float) (dp * density + 0.5);
        }
        return dp;
    }

    public static int dpToPixel(Context c, float dp){
        int result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
        if (result < 1)
            result = 1;
        return result;
    }

    public static int pixelToDP(Context c, int pixel){
        float scale = c.getResources().getDisplayMetrics().density;

        return (int)(pixel * scale);
    }
}

