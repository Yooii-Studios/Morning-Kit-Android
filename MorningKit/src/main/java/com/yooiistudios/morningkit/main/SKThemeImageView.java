package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stevenkim.photo.SKBitmapLoader;
import com.stevenkim.waterlily.SKWaterLily;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;

import java.io.FileNotFoundException;

/**
 * Created by Wooseong Kim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 5. 6.
 *
 * SKWaterLilyImageView
 *  사진테마, 워터릴리 테마를 활용하기 위한 이미지뷰
 */
public class SKThemeImageView extends ImageView {
    public SKThemeImageView(Context context) {
        super(context);

        // customizing
        RelativeLayout.LayoutParams layoutParams
                = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void setWaterLilyImage(final int orientation) {
        clear();

        MNViewSizeMeasure.setViewSizeObserver(this, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                Bitmap bitmap;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    bitmap = SKWaterLily.getPortraitBitmap(getContext(), getWidth(), getHeight());

                } else {
                    bitmap = SKWaterLily.getLandscapeBitmap(getContext(), getWidth(), getHeight());
                }
                setImageBitmap(bitmap);
            }
        });
    }

    public void setPhotoThemeImage(final int orientation, Context context) throws FileNotFoundException {
        clear();

        Bitmap bitmap;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(context,
                    SKBitmapLoader.getPortraitImageUri());
        } else {
            bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(context,
                    SKBitmapLoader.getLandscapeImageUri());
        }
        setImageBitmap(bitmap);
    }

    public void clear() {
        Drawable d = getDrawable();
        if (d instanceof BitmapDrawable) {
            Bitmap b = ((BitmapDrawable)d).getBitmap();
            b.recycle();
            MNLog.now("photoThemeImageView recycle Bitmap");
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
//        setImageDrawable(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        clear();
        super.onDetachedFromWindow();
    }
}
