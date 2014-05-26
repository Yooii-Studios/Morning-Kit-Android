package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stevenkim.photo.SKBitmapLoader;
import com.stevenkim.waterlily.SKWaterLily;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;

import java.io.FileNotFoundException;

import lombok.Setter;

/**
 * Created by Wooseong Kim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 5. 6.
 *
 * SKWaterLilyImageView
 *  사진테마, 워터릴리 테마를 활용하기 위한 이미지뷰
 */
public class SKThemeImageView extends ImageView {
    private static final String TAG = "SKThemeImageView";
    @Setter boolean isReadyForRecycle;
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
                try {
                    setImageDrawable(new BitmapDrawable(getContext().getApplicationContext().getResources(),
                            bitmap));
                } catch (OutOfMemoryError error) {
                    MNLog.i(TAG, "Water Lily Theme OOM");
                    error.printStackTrace();
                }
            }
        });
    }

    public void setPhotoThemeImage(final int orientation) throws FileNotFoundException {
        clear();

        Bitmap bitmap;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(getContext(),
                    SKBitmapLoader.getPortraitImageUri());
        } else {
            bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(getContext(),
                    SKBitmapLoader.getLandscapeImageUri());
        }
        try {
            setImageDrawable(new BitmapDrawable(getContext().getApplicationContext().getResources(),
                    bitmap));
        } catch (OutOfMemoryError error) {
            MNLog.i(TAG, "Photo Theme OOM");
            error.printStackTrace();
        }
    }

    public void clear() {
        MNBitmapUtils.recycleImageView(this);
//        Drawable drawable = getDrawable();
//        if (drawable instanceof BitmapDrawable) {
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            if (bitmap != null && !bitmap.isRecycled()) {
//                bitmap.recycle();
//                MNLog.now("photoThemeImageView recycle Bitmap");
//            }
//        }
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        MNLog.i(TAG, "onWindowVisibilityChanged");
        if (visibility == View.GONE && isReadyForRecycle) {
            clear();
        }
    }
}
