package com.yooiistudios.morningkit.setting.panel.animate;

import android.content.Context;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 11.
 *
 * MNTwinkleAnimator
 *  반짝이는 애니메이터 팩토리
 */
public class MNTwinkleAnimator {
    private MNTwinkleAnimator() { throw new AssertionError("You MUST not create this class!"); }

    public static ValueAnimator makeTwinkleAnimation(Context context) {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        Integer colorFrom = MNSettingColors.getForwardBackgroundColor(currentThemeType);
        Integer colorTo =  MNSettingColors.getGuidedPanelColor(currentThemeType);
        return newInstance(colorFrom, colorTo);
    }

    public static ValueAnimator makeLockPanelTwinkleAnimation(Context context) {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        Integer colorFrom = MNSettingColors.getLockedBackgroundColor(currentThemeType);
        Integer colorTo =  MNSettingColors.getGuidedPanelColor(currentThemeType);
        return newInstance(colorFrom, colorTo);
    }

    private static ValueAnimator newInstance(int colorFrom, int colorTo) {
        ValueAnimator twinkleAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        twinkleAnimation.setDuration(600);
        twinkleAnimation.setRepeatMode(ValueAnimator.REVERSE);
        twinkleAnimation.setRepeatCount(1);
        return twinkleAnimation;
    }
}
