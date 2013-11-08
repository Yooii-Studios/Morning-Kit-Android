package com.yooiistudios.morningkit.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;


public class MNDeviceSizeChecker 
{
	public static final String TAG = "MNDeviceSizeChecker";
	public static boolean isDeviceLargerThan7inch(Context context) 
	{
		//사이즈 계산
		Configuration config = context.getResources().getConfiguration();
		if (config.smallestScreenWidthDp >= 600) {
			return true;
		} else {
			return false;
		}	
	}
	
	// DipToPixel의 픽셀이 미세하게 안맞는 경우가 있어 Device 크키는 새로 구현 - 우성 
	@SuppressWarnings("deprecation")
	public static int getDeviceHeight(Context context) {
		/*
		Configuration config = context.getResources().getConfiguration();
		
		return DipToPixel.getPixel(context, config.screenHeightDp);
		*/
		
//		Log.i(TAG, "heightPixels: " + context.getResources().getDisplayMetrics().heightPixels);
		
//		return context.getResources().getDisplayMetrics().heightPixels;
		
		int measuredHeight = 0;
		Point size = new Point();
		
		WindowManager w = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
	          w.getDefaultDisplay().getSize(size);

	          measuredHeight = size.y; 
        }else{
          Display d = w.getDefaultDisplay(); 
          measuredHeight = d.getHeight(); 
        }
	    
	    return measuredHeight;
	}
	
	@SuppressWarnings("deprecation")
	public static int getDeviceWidth(Context context) {
		/*
		Configuration config = context.getResources().getConfiguration();
		Log.i(TAG, "" + config.screenWidthDp);
		
		return DipToPixel.getPixel(context, config.screenWidthDp);
		*/

		/*
		Configuration config = context.getResources().getConfiguration();
		
		Log.i(TAG, "DipToPixel heightPixels: " + DipToPixel.getPixel(context, config.screenHeightDp));
		Log.i(TAG, "heightPixels: " + context.getResources().getDisplayMetrics().heightPixels);
		Log.i(TAG, "widthPixels: " + context.getResources().getDisplayMetrics().widthPixels);
		*/
		
		int measuredWidth = 0;
		Point size = new Point();
		
		WindowManager w = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
	          w.getDefaultDisplay().getSize(size);

	          measuredWidth = size.x;
        }else{
          Display d = w.getDefaultDisplay(); 
          measuredWidth = d.getWidth(); 
        }
	    
	    return measuredWidth;
	}
}

