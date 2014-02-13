package com.yooiistudios.morningkit.common.shadow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 17.
 *
 * ModernityThemeShadowLayout
 *  모더니티(화이트) 테마의 쉐도우 레이아웃
 */
public class ModernityThemeShadowLayout extends RoundShadowRelativeLayout {
    public ModernityThemeShadowLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ModernityThemeShadowLayout(Context context, AttributeSet attrs) {
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

            mSolidColor = MNSettingColors.getForwardBackgroundColor(MNThemeType.MODERNITY_WHITE);
            mPressedColor = MNSettingColors.getPressedBackgroundColor(MNThemeType.MODERNITY_WHITE);

            mShadowColor = Color.argb(140, 0, 0, 0);
        }
    }
}
