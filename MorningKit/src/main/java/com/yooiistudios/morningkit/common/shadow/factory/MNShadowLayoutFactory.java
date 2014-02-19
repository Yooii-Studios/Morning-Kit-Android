package com.yooiistudios.morningkit.common.shadow.factory;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 17.
 *
 * MNShadowLayoutFactory
 *  레이아웃과 부모 레이아웃을 넣으면 테마에 따라 쉐도우를 교체해주는 클래스
 */
public class MNShadowLayoutFactory {
    private MNShadowLayoutFactory() { throw new AssertionError("You MUST not create this class!"); }

    /**
     * RoundShadowLayout 을 교체하지 않고 테마만 바꿔주게 변경하려고 함
     */
    public static void changeThemeOfShadowLayout(RoundShadowRelativeLayout shadowLayout, Context context) {

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        Resources resources = context.getResources();

        shadowLayout.setRoundRectRadius((int)resources.getDimension(R.dimen.rounded_corner_radius));
        shadowLayout.setBlurRadius((int)resources.getDimension(R.dimen.margin_shadow_inner));
        shadowLayout.setShadowColor(Color.argb(140, 0, 0, 0));

        switch (currentThemeType) {
            case SLATE_GRAY:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.SLATE_GRAY));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.SLATE_GRAY));
                break;

            case MODERNITY_WHITE:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.MODERNITY_WHITE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.MODERNITY_WHITE));
                break;

            case CELESTIAL_SKY_BLUE:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.CELESTIAL_SKY_BLUE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.CELESTIAL_SKY_BLUE));
                break;

            default:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.MODERNITY_WHITE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.MODERNITY_WHITE));
                break;
        }
    }

    // 특정 themeType으로 변경해줌
    public static void changeThemeOfShadowLayout(RoundShadowRelativeLayout shadowLayout, Context context, MNThemeType themeType) {

        Resources resources = context.getResources();

        shadowLayout.setRoundRectRadius((int)resources.getDimension(R.dimen.rounded_corner_radius));
        shadowLayout.setBlurRadius((int)resources.getDimension(R.dimen.margin_shadow_inner));
        shadowLayout.setShadowColor(Color.argb(140, 0, 0, 0));

        switch (themeType) {
            case SLATE_GRAY:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.SLATE_GRAY));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.SLATE_GRAY));
                break;

            case MODERNITY_WHITE:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.MODERNITY_WHITE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.MODERNITY_WHITE));
                break;

            case CELESTIAL_SKY_BLUE:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.CELESTIAL_SKY_BLUE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.CELESTIAL_SKY_BLUE));
                break;

            default:
                shadowLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(MNThemeType.MODERNITY_WHITE));
                shadowLayout.setPressedColor(MNSettingColors.getPressedBackgroundColor(MNThemeType.MODERNITY_WHITE));
                break;
        }
    }

    // 아래의 동적 생성 메서드들은 더이상 사용하지 않을 예정
    /**
     * 기존에 있는 자식 뷰들도 전부 옮겨줌 - 추천 메서드
     */
    /*
    public static RoundShadowRelativeLayout changeShadowLayoutWithChildren(RoundShadowRelativeLayout originalShadowLayout,
                                                                           ViewGroup parentGroup) {
        Context context = parentGroup.getContext();
        RoundShadowRelativeLayout newShadowRelativeLayout;

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        switch (currentThemeType) {
            case SLATE_GRAY:
                newShadowRelativeLayout = new SlateThemeShadowLayout(context);
                break;

            case MODERNITY_WHITE:
                newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                break;

            case CELESTIAL_SKY_BLUE:
                newShadowRelativeLayout = new CelestialThemeShadowLayout(context);
                break;

            default:
                newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                break;
        }
        newShadowRelativeLayout.setId(originalShadowLayout.getId());
        newShadowRelativeLayout.setLayoutParams(originalShadowLayout.getLayoutParams());
        newShadowRelativeLayout.setClipChildren(true);

        // 기존에 있던 View들을 모두 옮겨줌 - 뒤쪽부터 빼서 넣어준다
        int childCount = originalShadowLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = originalShadowLayout.getChildAt(childCount - 1 - i);
//            View v = originalShadowLayout.getChildAt(0);

            if (v != null) {
                originalShadowLayout.removeView(v);
                newShadowRelativeLayout.addView(v);
            }
        }

        // 기존 ShadowLayout을 삭제하고 새 ShadowLayout으로 대입
        parentGroup.removeView(originalShadowLayout);
        parentGroup.addView(newShadowRelativeLayout, 0);

        return newShadowRelativeLayout;
    }
    */

    /**
     * 타입과 원 쉐도우레이아웃, 부모 뷰그룹(레이아웃)을 넣으면 테마에 맞게 교체하는 메서드 - 기존 메서드
     */
    /*
    public static RoundShadowRelativeLayout changeShadowLayout(MNThemeType themeType,
                                          RoundShadowRelativeLayout originalShadowLayout,
                                          ViewGroup parentGroup) {

        Context context = parentGroup.getContext();
        RoundShadowRelativeLayout newShadowRelativeLayout;

        switch (themeType) {
            case SLATE_GRAY:
                newShadowRelativeLayout = new SlateThemeShadowLayout(context);
                break;

            case MODERNITY_WHITE:
                newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                break;

            case CELESTIAL_SKY_BLUE:
                newShadowRelativeLayout = new CelestialThemeShadowLayout(context);
                break;

            default:
                newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                break;
        }
        newShadowRelativeLayout.setId(originalShadowLayout.getId());
        newShadowRelativeLayout.setLayoutParams(originalShadowLayout.getLayoutParams());
        newShadowRelativeLayout.setClipChildren(true);
        parentGroup.removeView(originalShadowLayout);
        parentGroup.addView(newShadowRelativeLayout, 0);

        return newShadowRelativeLayout;
    }
    */
}
