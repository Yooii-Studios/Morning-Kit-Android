package com.stevenkim.waterlily;

import android.content.Context;
import android.graphics.Bitmap;

import com.stevenkim.waterlily.bitmapfun.util.ImageResizer;
import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 11.
 *
 * SKWaterLily
 *  스케일에 맞춰 비트맵을 가져오는 클래스
 */
public class SKWaterLily {
    private SKWaterLily() { throw new AssertionError("You MUST not create this class!"); }

    public static Bitmap getPortraitBitmap(Context context, int reqWidth, int reqHeight) {
        return ImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                R.drawable.water_lily_2048x1536_tablet_portrait, reqWidth, reqHeight, null);
    }

    public static Bitmap getLandscapeBitmap(Context context, int reqWidth, int reqHeight) {
        return ImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                R.drawable.water_lily_2048x1536_tablet_landscape, reqWidth, reqHeight, null);
    }

    /*
    private static Bitmap rotateImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(-90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }
    */
}
