package com.yooiistudios.morningkit.common.dp;

/**
 * Created by yongbinbae on 2013. 10. 24..
 */

import android.content.Context;

public class DipToPixel {

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

}

