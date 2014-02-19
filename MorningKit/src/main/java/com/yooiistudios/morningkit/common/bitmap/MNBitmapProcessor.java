package com.yooiistudios.morningkit.common.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 19.
 *
 * MNBitmapProcessor
 *  플리커에서 사용될 비트맵 처리기 - 그레이스케일, 라운딩, 크롭
 */
public class MNBitmapProcessor {
    private MNBitmapProcessor() { throw new AssertionError("You MUST not create this class!"); }

    // 크롭 이후 크기가 맞춰진 비트맵의 가공을 진행
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean isGrayScale, int radius) {
        if(bitmap != null)
        {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            // gray scale
            if (isGrayScale) {
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                paint.setColorFilter(f);
            }

            // round
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, radius, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
        return null;
    }

    /*
    // 라운딩과 통합
    public static Bitmap getGrayScaledBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width, height;
            height = bitmap.getHeight();
            width = bitmap.getWidth();

            Bitmap grayScaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(grayScaledBitmap);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return grayScaledBitmap;
        }
        return null;
    }
    */
}
