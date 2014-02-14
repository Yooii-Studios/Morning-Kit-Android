package com.stevenkim.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class SKBitmapLoader {

    public final static int REQ_CODE_PICK_IMAGE_PORTRAIT = 4567;
    public final static int REQ_CODE_PICK_IMAGE_LANDSCAPE = 5678;
    public final static int REQ_CROP_FROM_PORTRAIT = 6789;
    public final static int REQ_CROP_FROM_LANDSCAPE = 7890;

	private static final String TEMP_PHOTO_FILE_PORTRAIT = "backgroundImage_portrait.jpg"; // Portrait 용 임시 저장파일
	private static final String TEMP_PHOTO_FILE_LANDSCAPE = "backgroundImage_landscape.jpg"; // Landscape 용 임시 저장파일
	
	/** 임시 저장 파일의 경로를 반환 */
	public static Uri getPortraitImageUri() {
        if (getPortraitTempFile() != null) {
            return Uri.fromFile(getPortraitTempFile());
        } else {
            return null;
        }
    }

    public static Uri getLandscapeImageUri() {
        if (getLandscapeTempFile() != null) {
            return Uri.fromFile(getLandscapeTempFile());
        } else {
            return null;
        }
	}

	/** SD카드가 마운트 되어 있는지 확인 */
	public static boolean isSdCardMounted() {
		String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

	/** 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환 */
	public static File getPortraitTempFile() {
		if (isSdCardMounted()) {
            // 외장메모리, 경로, temp.jpg 파일 생성
			File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_PORTRAIT);
			try {
                if (file.createNewFile()) {
                    return file;
                }
			} catch (IOException e) {
                e.printStackTrace();
			}
			return file;
		}
        return null;
	}
	
	public static File getLandscapeTempFile() {
		if (isSdCardMounted()) {
            // 외장메모리, 경로, temp.jpg 파일 생성
			File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_LANDSCAPE);
			try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
		}
        return null;
	}

    @SuppressWarnings( "deprecation" )
	public static Bitmap loadAutoScaledBitmapFromUri(Context context, Uri uri) throws FileNotFoundException {
        // Error check
        if (context == null || uri == null) {
            return null;
        }

        Bitmap bitmap;

        // measure device size
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int displayWidth;
        int displayHeight;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point displaySize = new Point();
            display.getSize(displaySize);
            displayWidth = displaySize.x;
            displayHeight = displaySize.y;
        } else {
            displayWidth = display.getWidth(); // deprecated
            displayHeight = display.getHeight(); // deprecated
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Config.RGB_565;

        options.inJustDecodeBounds = true;

        // bitmap 으로 반환을 받지 않아도 상관없을듯
//		bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

        // measure rescale size that nearly match the device size
        // rescale value is even for less image loss

        float widthScale = options.outWidth / displayWidth;
        float heightScale = options.outHeight / displayHeight;
        float scale = widthScale > heightScale ? widthScale : heightScale;

        if (scale >= 8) {
            options.inSampleSize = 8;
        } else if (scale >= 6) {
            options.inSampleSize = 6;
        } else if (scale >= 4) {
            options.inSampleSize = 4;
        } else if (scale >= 2) {
            options.inSampleSize = 2;
        } else {
            options.inSampleSize = 1;
        }

        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

        return bitmap;
    }

	public static void saveBitmapToUri(Context context, Uri uri, Bitmap bitmap) throws FileNotFoundException {
		try {
			OutputStream outStream = context.getContentResolver().openOutputStream(uri);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 100, context.getContentResolver().openOutputStream(uri));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            if (outStream != null) {
                outStream.close();
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

    // 2014. 01. 24에 우성이 새로 추가한 메서드
    public static void saveBitmap(Context context, int requestCode) throws FileNotFoundException {
        Bitmap bitmap;
        Uri imageUri;
        switch (requestCode) {
            case REQ_CROP_FROM_PORTRAIT:
                imageUri = getPortraitImageUri();
                break;

            case REQ_CROP_FROM_LANDSCAPE:
                imageUri = getLandscapeImageUri();
                break;

            default:
                throw new AssertionError("not expected request code");
        }
        bitmap = loadAutoScaledBitmapFromUri(context, imageUri);
        saveBitmapToUri(context, imageUri, bitmap);
        bitmap.recycle();
    }
}
