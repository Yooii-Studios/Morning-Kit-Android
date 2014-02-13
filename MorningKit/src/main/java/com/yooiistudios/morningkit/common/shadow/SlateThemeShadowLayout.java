package com.yooiistudios.morningkit.common.shadow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 16.
 *
 * SlateThemeShadowLayout
 *  슬레이트 테마의 쉐도우 레이아웃
 */
public class SlateThemeShadowLayout extends RoundShadowRelativeLayout {
    public SlateThemeShadowLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public SlateThemeShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void initColor() {
        setBackgroundColor(Color.TRANSPARENT);

        Resources resources;
        if (getContext() != null) {
            resources = getContext().getResources();
            mRoundRectRadius = (int)resources.getDimension(R.dimen.rounded_corner_radius);
            mBlurRadius = (int)resources.getDimension(R.dimen.margin_shadow_inner);

            mSolidColor = MNSettingColors.getForwardBackgroundColor(MNThemeType.SLATE_GRAY);
            mPressedColor = MNSettingColors.getPressedBackgroundColor(MNThemeType.SLATE_GRAY);

            mShadowColor = Color.argb(140, 0, 0, 0);
        }
    }
}
