package com.yooiistudios.morningkit.common.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.crashlytics.android.Crashlytics;
import com.yooiistudios.morningkit.common.file.ExternalStorageManager;
import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 19.
 *
 * MNBitmapProcessor
 *  플리커에서 사용될 비트맵 처리기 - 그레이스케일, 라운딩, 크롭
 */
public class MNBitmapProcessor {
    private MNBitmapProcessor() { throw new AssertionError("You MUST not create this class!"); }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (bitmap != null) {

            Bitmap croppedBitmap;
            double frameRatio = (double) targetWidth / (double) targetHeight;

            // 이미지의 가로가 세로보다 같거나 김
            if (bitmap.getWidth() >= bitmap.getHeight()) {
                // frame.width : bitmap.width (a)와 frame.height : bitmap.height (b)를 비교

                // 로직 수정: 비율이 1 밑으로 내려갈 것 까지 감안해서 로직을 구성하는 것으로 변경
                // 이전 로직인 무조건 1 이상의 비율만 구해서 구현하면 bitmap이 640/224 처럼 가로로 아주 길 경우
                // 가로 비율은 1이상인데, 세로 비율은 반대로 계산해 1이상이 나오게 만들어서 제대로 된 값이 나오지 않았음
                // 한쪽 비율만 계산해서 1미만이면 그쪽으로 줄일 수 있게 변경
                double widthRatio;
//                if (targetWidth > bitmap.getWidth()) {
//                    widthRatio = (double) bitmap.getWidth() / (double) targetWidth;
//                } else {
                    widthRatio = (double) targetWidth / (double) bitmap.getWidth();
//                }
//
                double heightRatio;
//                if (targetHeight > bitmap.getHeight()) {
//                    heightRatio = (double) bitmap.getHeight() / (double) targetHeight;
//                } else {
                    heightRatio = (double) targetHeight / (double) bitmap.getHeight();
//                }

                // (a)와 (b) 중 작은 쪽으로 이미지를 줄인다
                if (widthRatio < heightRatio) {
                    // (a)가 작다면 bitmap의 height는 frame.height, width는 frame.height * ratio
                    Point newBitmapSize = new Point((int) (bitmap.getHeight() * frameRatio), bitmap.getHeight());

                    // 자를 위치는 bitmap.width/2 - frame.width/2 에서 frame.width 만큼 자름

                    // 수정:
                    // bitmap.getWidth() / 2 - newBitmapSize.x / 2 를 사용해도 되지만,
                    // 혹시 모를 음수값을 대비해 절대값을 사용해 계산한다
                    // 수정 후에도 거의 없는 일이지만 bitmap의 크기가 더 작은 경우는 아래 조건이 성립해 버리는 문제가 생긴다
                    // 자를 수 있는 영역보다 더 넓은 영역을 자르기 때문이. 이것을 어떻게 해결할 수 있을지 고민해보자
                    // 앱이 크래쉬되기에, 기록해 두었다가 나중에 해결하도록 하자
                    if (Math.abs(bitmap.getWidth() - newBitmapSize.x) / 2 + newBitmapSize.x > bitmap.getWidth()) {
                        MNLog.now("(a)가 작다면 bitmap의 height는 frame.height, width는 frame.height * ratio");
                        MNLog.now("bitmap.getWidth() - newBitmapSize.x: " + (bitmap.getWidth() - newBitmapSize.x));
                        MNLog.now("targetWidth: " + targetWidth);
                        MNLog.now("targetHeight: " + targetHeight);
                        MNLog.now("newBitmapSize.x: " + newBitmapSize.x);
                        MNLog.now("newBitmapSize.y: " + newBitmapSize.y);
                        MNLog.now("bitmap.getWidth(): " + bitmap.getWidth());
                        MNLog.now("bitmap.getHeight(): " + bitmap.getHeight());
                        return null;
                    }
                    try {
                        croppedBitmap = Bitmap.createBitmap(bitmap,
                                Math.abs(bitmap.getWidth() - newBitmapSize.x) / 2, 0,
                                newBitmapSize.x, newBitmapSize.y);
                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                        croppedBitmap = null;
                    } catch (IllegalArgumentException e) {
                        reportCrash(bitmap, targetWidth, targetHeight, newBitmapSize, e);
                        croppedBitmap = null;
                    }
                } else {
                    // 이미지의 세로가 가로보다 같거나 김

                    // (b)가 작다면 bitmap 의 width 는 frame.width, height 는 frame.width / ratio
                    Point newBitmapSize = new Point(bitmap.getWidth(), (int) (bitmap.getWidth() / frameRatio));

                    // 자를 위치는 Image.height/2 - frame.height/2 에서 frame.height 만큼 자름

                    // 수정:
                    // bitmap.getHeight() / 2 - newBitmapSize.y / 2 를 사용해도 되지만,
                    // 이 값이 음수가 나올 경우가 있다. (getHeight()더 작다거나)
                    // 따라서 음수가 나오지 않는 아래 방법을 사용한다
                    // 수정 후에도 거의 없는 일이지만 bitmap 의 크기가 더 작은 경우는 아래 조건이 성립해 버리는 문제가 생긴다
                    // 자를 수 있는 영역보다 더 넓은 영역을 자르기 때문이. 이것을 어떻게 해결할 수 있을지 고민해보자
                    // 앱이 크래쉬되기에, 기록해 두었다가 나중에 해결하도록 하자
                    if (Math.abs(bitmap.getHeight() - newBitmapSize.y) / 2 + newBitmapSize.y > bitmap.getHeight()) {
                        MNLog.now("(b)가 작다면 bitmap의 width는 frame.width, height는 frame.width / ratio");
                        MNLog.now("bitmap.getHeight() - newBitmapSize.y: " + (bitmap.getHeight() - newBitmapSize.y));
                        MNLog.now("targetWidth: " + targetWidth);
                        MNLog.now("targetHeight: " + targetHeight);
                        MNLog.now("newBitmapSize.x: " + newBitmapSize.x);
                        MNLog.now("newBitmapSize.y: " + newBitmapSize.y);
                        MNLog.now("bitmap.getWidth(): " + bitmap.getWidth());
                        MNLog.now("bitmap.getHeight(): " + bitmap.getHeight());
                        return null;
                    }
                    try {
                        croppedBitmap = Bitmap.createBitmap(bitmap,
                                0, Math.abs(bitmap.getHeight() - newBitmapSize.y) / 2,
                                newBitmapSize.x, newBitmapSize.y);
                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                        croppedBitmap = null;
                    } catch (IllegalArgumentException e) {
                        reportCrash(bitmap, targetWidth, targetHeight, newBitmapSize, e);
                        croppedBitmap = null;
                    }
                }
            } else {
                // 이미지의 가로가 세로보다 짧음

                // 이미지 조절. 새 이미지의 width 는 frame 의 width 로, 이미지와 frame width 의 비율만큼
                // 이미지 height 를 조절해서 이미지를 resize 한다.
                Point newBitmapSize = new Point(bitmap.getWidth(), (int) (bitmap.getWidth() / frameRatio));

                // 위에서 15% 아래에서부터 자름(인물 사진이라 가정)
                double offset15PercentFromTop = bitmap.getHeight() * 0.15;

                if (offset15PercentFromTop + newBitmapSize.y <= bitmap.getHeight()) {
                    croppedBitmap = Bitmap.createBitmap(bitmap, 0, (int) offset15PercentFromTop,
                            newBitmapSize.x, newBitmapSize.y);
                } else {
                    // 2014년 6월 13일 버그 발견. 아직 수정 못함. 아마 bitmap이 newBitmapSize보다 작은 경우가 아닐까 추정 중
                    if (Math.abs(bitmap.getHeight() - newBitmapSize.y) + newBitmapSize.y > bitmap.getHeight()) {
                        MNLog.now("위에서 15% + 높이가 넘칠 경우 중앙을 자름");
                        MNLog.now("bitmap.getHeight() - newBitmapSize.y: " + (bitmap.getHeight() - newBitmapSize.y));
                        MNLog.now("targetWidth: " + targetWidth);
                        MNLog.now("targetHeight: " + targetHeight);
                        MNLog.now("newBitmapSize.x: " + newBitmapSize.x);
                        MNLog.now("newBitmapSize.y: " + newBitmapSize.y);
                        MNLog.now("bitmap.getWidth(): " + bitmap.getWidth());
                        MNLog.now("bitmap.getHeight(): " + bitmap.getHeight());
                        return null;
                    }
                    // 15%해서 넘어가버리는 경우는 중앙을 잘라서 보여주게 구현
                    try {
                        croppedBitmap = Bitmap.createBitmap(bitmap,
                                0, Math.abs(bitmap.getHeight() - newBitmapSize.y) / 2,
                                newBitmapSize.x, newBitmapSize.y);
                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                        croppedBitmap = null;
                    } catch (IllegalArgumentException e) {
                        reportCrash(bitmap, targetWidth, targetHeight, newBitmapSize, e);
                        croppedBitmap = null;
                    }
                }
            }
            return croppedBitmap;
        }
        return null;
    }

    private static void reportCrash(Bitmap bitmap, int targetWidth, int targetHeight, Point newBitmapSize, IllegalArgumentException e) {
        e.printStackTrace();
        Crashlytics.log("(b)가 작다면 bitmap의 width는 frame.width, height는 frame.width / ratio");
        Crashlytics.log("bitmap.getHeight() - newBitmapSize.y: " + (bitmap.getHeight() - newBitmapSize.y));
        Crashlytics.log("targetWidth: " + targetWidth);
        Crashlytics.log("targetHeight: " + targetHeight);
        Crashlytics.log("newBitmapSize.x: " + newBitmapSize.x);
        Crashlytics.log("newBitmapSize.y: " + newBitmapSize.y);
        Crashlytics.log("bitmap.getWidth(): " + bitmap.getWidth());
        Crashlytics.log("bitmap.getHeight(): " + bitmap.getHeight());
    }

    // 크롭 이후 패널 크기에 맞게 축소 & 가공을 진행
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int targetWidth, int targetHeight,
                                                boolean isGrayScale, int radius) throws OutOfMemoryError {
        if(bitmap != null) {
            // 프레임에 맞게 비트맵 scaling
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            bitmap.recycle();

            Bitmap outputBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);

            // outputBitmap 에 캔버스를 생성에 scaleBitmap의 가공 내용을 draw
            Canvas canvas = new Canvas(outputBitmap);

            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
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
            canvas.drawBitmap(scaledBitmap, rect, rect, paint);
            scaledBitmap.recycle();
            return outputBitmap;
        }
        return null;
    }

    public static Bitmap getGrayScaledBitmap(Bitmap originalBitmap) {
        if(originalBitmap != null) {
            Bitmap grayScaledBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
                    originalBitmap.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(grayScaledBitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            canvas.drawBitmap(originalBitmap, 0, 0, paint);

            // 그리고 난 뒤 리사이클
            originalBitmap.recycle();

            return grayScaledBitmap;
        }
        return null;
    }

    // 비트맵을 JSON으로 전달. 속도가 느려서 취소함
//    public static String getStringFromBitmap(Bitmap bitmap) {
//        /*
//        * This functions converts Bitmap picture to a string which can be
//        * JSONified.
//        */
//        final int COMPRESSION_QUALITY = 100;
//        String encodedImage;
//        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
//                byteArrayBitmapStream);
//        byte[] b = byteArrayBitmapStream.toByteArray();
//        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//    public static Bitmap getBitmapFromString(String jsonString) {
//        /*
//        * This Function converts the String back to Bitmap
//        */
//        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//    }

    /**
     * 비트맵을 로컬에 저장
     */
    public static String saveBitmapToDirectory(Context context, Bitmap image,
                                               String fileName, String directory) throws IOException {
        // Create an image file name - 동현 코드 사용해서 래핑
        File storageDir = ExternalStorageManager.createExternalDirectory(context, directory);

        // Save a file
        String imagePath = storageDir + "/" + fileName + ".jpg";

        FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

        // 이미지 저장
        fileOutputStream.flush();
        fileOutputStream.close();

        return imagePath;
    }

    public static Bitmap loadBitmapFromDirectory(Context context, String fileName, String directory) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 1;
        option.inPurgeable = true;
        option.inDither = true;

        File storageDir = ExternalStorageManager.createExternalDirectory(context, directory);
        return BitmapFactory.decodeFile(storageDir + "/" + fileName + ".jpg", option);
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

    public static Bitmap createSampleSizedBitmap(File file,
                                                  int desiredWidth, int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        BitmapFactory.Options resizeOpts = new BitmapFactory.Options();
        resizeOpts.inSampleSize = calculateInSampleSize(options, desiredWidth, desiredHeight);
        resizeOpts.inScaled = false;
        resizeOpts.inDither = false;
        resizeOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeFile(file.getAbsolutePath(), resizeOpts);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
