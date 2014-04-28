package com.yooiistudios.morningkit.common.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 28.
 *
 * MNBitmapColorProcessor
 *  흰색 비트맵을 특정 색을 지닌 비트맵으로 변환해준다.
 */
public class MNBitmapColorProcessor {
    private MNBitmapColorProcessor() { throw new AssertionError("You MUST not create this class!"); }

    public static void getBitmapWithColor(Bitmap bitmap, int color) {
        int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < bitmap.getHeight() * bitmap.getWidth(); i++) {
            if (pixels[i] == Color.WHITE) {
                pixels[i] = color;
            }
        }

        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }
}
