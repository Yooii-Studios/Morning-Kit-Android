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
 *  안드로이드에서는 각 해상도
 *  mdpi(320x480) : 갤럭시 S,
 *  hdpi(480x800) : 갤럭시 S2,
 *  xhdpi(800x1280) : 갤럭시 노트2,
 *  xxhdpi(1080x1920) : LG 옵티머스 G 프로
 *  에 맞게 전부 리사이징함
 */
public class SKWaterLily {
    private SKWaterLily() { throw new AssertionError("You MUST not create this class!"); }

    public static Bitmap getPortraitBitmap(Context context, int reqWidth, int reqHeight) {
        return ImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                R.drawable.water_lily_portrait, reqWidth, reqHeight, null);
    }

    public static Bitmap getLandscapeBitmap(Context context, int reqWidth, int reqHeight) {
        return ImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                R.drawable.water_lily_landscape, reqWidth, reqHeight, null);
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
