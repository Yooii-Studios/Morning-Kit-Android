package com.yooiistudios.morningkit.common.size;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

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
     * Table Check
     */
	public static boolean isTablet(Context context)	{
		Configuration config = context.getResources().getConfiguration();
		if (config.smallestScreenWidthDp >= 600) {
			return true;
		} else {
			return false;
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

