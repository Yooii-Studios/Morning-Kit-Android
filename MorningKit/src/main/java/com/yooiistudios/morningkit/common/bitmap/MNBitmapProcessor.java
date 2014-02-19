package com.yooiistudios.morningkit.common.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
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
    private static final String TAG = "MNBitmapProcessor";
    private MNBitmapProcessor() { throw new AssertionError("You MUST not create this class!"); }

    public static Bitmap getCroppedBiamtp(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (bitmap != null) {

//            MNLog.i(TAG, "bitmap: " + bitmap.toString());
//            MNLog.i(TAG, "targetWidth: " + targetWidth);
//            MNLog.i(TAG, "targetHeight: " + targetHeight);

            Bitmap croppedBitmap;
            double frameRatio = (double) targetWidth / (double) targetHeight;

            if (bitmap.getWidth() >= bitmap.getHeight()) {
                // 이미지의 가로가 세로보다 같거나 김

                // frame.width : bitmap.width (a)와 frame.height : bitmap.height (b)를 비교
                double widthRatio;
                if (targetWidth > bitmap.getWidth()) {
                    widthRatio = bitmap.getWidth() / (double) targetWidth;
                } else {
                    widthRatio = (double) targetWidth / bitmap.getWidth();
                }

                double heightRatio;
                if (targetHeight > bitmap.getHeight()) {
                    heightRatio = bitmap.getHeight() / (double) targetHeight;
                } else {
                    heightRatio = (double) targetHeight / bitmap.getHeight();
                }

                // (a)와 (b) 중 작은 쪽으로 이미지를 줄인다
                if (widthRatio < heightRatio) {
                    // (a)가 작다면 bitmap의 height는 frame.height, width는 frame.height * ratio
                    Point newBitmapSize = new Point((int) (bitmap.getHeight() * frameRatio), bitmap.getHeight());

                    // 자를 위치는 bitmap.width/2 - frame.width/2 에서 frame.width 만큼 자름
                    croppedBitmap = Bitmap.createBitmap(bitmap,
                            bitmap.getWidth() / 2 - newBitmapSize.x / 2, 0,
                            newBitmapSize.x, newBitmapSize.y);
                } else {
                    // (b)가 작다면 bitmap의 width는 frame.width, height는 frame.width / ratio
                    Point newBitmapSize = new Point(bitmap.getWidth(), (int) (bitmap.getWidth() / frameRatio));

                    // 자를 위치는 Image.height/2 - frame.height/2 에서 frame.height 만큼 자름
                    croppedBitmap = Bitmap.createBitmap(bitmap,
                            0, bitmap.getHeight() / 2 - newBitmapSize.y / 2,
                            newBitmapSize.x, newBitmapSize.y);
                }
            } else {
                // 이미지의 가로가 세로보다 짧음

                // 이미지 조절. 새 이미지의 width는 frame의 width로, 이미지와 frame width의 비율만큼
                // 이미지 height를 조절해서 이미지를 resize한다.
                Point newBitmapSize = new Point(bitmap.getWidth(), (int) (bitmap.getWidth() / frameRatio));

                // 위에서 15% 아래에서부터 자름(인물 사진이라 가정)
                double offset15PercentFromTop = newBitmapSize.y * 0.15;
                croppedBitmap = Bitmap.createBitmap(bitmap, 0, (int) offset15PercentFromTop,
                        newBitmapSize.x, newBitmapSize.y + (int) offset15PercentFromTop);
            }
            return croppedBitmap;
        }
        return null;
    }

    // 크롭 이후 크기가 맞춰진 비트맵의 가공을 진행
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean isGrayScale, int radius) {
        if(bitmap != null) {
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
