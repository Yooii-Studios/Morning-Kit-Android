package com.yooiistudios.morningkit.common.size;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by MNDeviceSizeInfo in MorningKit from Yooii Studios Co., LTD. on 2013. 10. 22.
 *
 * MNDeviceSizeInfo (유틸리티 클래스)
 *  1. 기기의 사이즈 확인
 *  2. 폰 or 태블릿 여부 확인(> 7인치)
 */
public class MNDeviceSizeInfo {
	public static final String TAG = "MNDeviceSizeInfo";

    private MNDeviceSizeInfo() { throw new AssertionError(); } // You must not create instance

    /**
     * Checking Tablet
     * 600dp = 7inch, 720dp = 10inch
     */
	public static boolean isTablet(Context context) {
//        // smallestScreenWidthDp가 SDK 13 이상부터 사용가능
//        Configuration config = context.getResources().getConfiguration();
//        // sw600dp 이상이면 태블릿으로 인식
//        return config.smallestScreenWidthDp >= 600;

        // http://stackoverflow.com/questions/10238652/smallestscreenwidthdp-for-pre-3-2-devices
        return (getSmallestScreenWidthDp(context) >= 600);
    }

    private static int getSmallestScreenWidthDp(Context context) {
        Resources resources = context.getResources();
        try {
            Field field = Configuration.class.getDeclaredField("smallestScreenWidthDp");
            return (Integer) field.get(resources.getConfiguration());
        } catch (Exception e) {
            // not perfect because reported screen size might not include status and button bars
            // example on my Galaxy Tab 10.1 the actual value is 800 whereas the computed value is only 752
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int smallestScreenWidthPixels = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            return Math.round(smallestScreenWidthPixels / displayMetrics.density);
        }
    }

    // http://stackoverflow.com/questions/16784101/how-to-find-tablet-or-phone-in-android-programmatically
//    public static boolean isTablet(Context context) {
//        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
//        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
//        return (xlarge || large);
//    }

    public static int getStatusBarHeight(Context context) {
//        xxhdpi 75dp
//        xhdpi 50dp
        if (!(context instanceof Activity)) {
            throw new AssertionError("Context must be instanceof Activity");
        }
        Rect CheckRect = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(CheckRect);
        return CheckRect.top;
    }

    // DipToPixel의 픽셀이 미세하게 안맞는 경우가 있어 Device 크키는 새로 구현 - 우성
	public static int getDeviceHeight(Context context) {
		int measuredHeight;
		Point size = new Point();
		
		WindowManager windowManager =
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	          windowManager.getDefaultDisplay().getSize(size);
	          measuredHeight = size.y;
        } else {
            Display d = windowManager.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }
	    return measuredHeight;
	}
	
	public static int getDeviceWidth(Context context) {
		int measuredWidth;
		Point size = new Point();
		
		WindowManager windowManager =
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	          windowManager.getDefaultDisplay().getSize(size);
	          measuredWidth = size.x;
        } else {
            Display d = windowManager.getDefaultDisplay();
            measuredWidth = d.getWidth();
        }
	    return measuredWidth;
	}
}

