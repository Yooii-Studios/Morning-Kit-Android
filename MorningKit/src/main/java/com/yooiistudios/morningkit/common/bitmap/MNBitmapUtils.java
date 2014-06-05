package com.yooiistudios.morningkit.common.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 9.
 *
 * MNBitmapUtils
 *  recycle을 하기 위한 유틸리티 클래스
 */
public class MNBitmapUtils {
    private static final String TAG = "MNBitmapUtils";
    private MNBitmapUtils() { throw new AssertionError("You MUST not create this class!"); }

    public static boolean recycleImageView(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
//                    MNLog.i(TAG, "bitmap recycled");
                    imageView.setImageBitmap(null);
                    return true;
                } else {
//                    MNLog.i(TAG, "bitmap is null");
                }
            }
        } else {
//            MNLog.i(TAG, "imageView is null");
        }
        return false;
    }

    public static BitmapFactory.Options getDefaultOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;       // 메모리를 줄여주는 옵션
        options.inDither = true;          // 이미지를 깔끔하게 처리해서 보여주는 옵션
        return options;
    }
}
