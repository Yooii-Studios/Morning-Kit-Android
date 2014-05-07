package com.stevenkim.waterlily;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.common.log.MNLog;

/**
 * Created by Wooseong Kim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 5. 6.
 *
 * SKWaterLilyImageView
 *  워터릴리 테마를 활용하기 위한 이미지뷰
 */
public class SKWaterLilyImageView extends ImageView {
    public SKWaterLilyImageView(Context context) {
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

        post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    bitmap = SKWaterLily.getPortraitBitmap(getContext(), getWidth(), getHeight());
                } else {
                    bitmap = SKWaterLily.getLandscapeBitmap(getContext(), getHeight(), getWidth());
                }
                setImageBitmap(bitmap);
            }
        });
    }

    public void clear() {
        setImageDrawable(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MNLog.now("onDetachedFromWindow");
//        clear();
    }


}
