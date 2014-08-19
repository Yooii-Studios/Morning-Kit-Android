package com.yooiistudios.morningkit.theme.font;

import android.content.Context;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 5. 7.
 *
 * MNTranslucentFont
 *  투명 테마에 대응하기 위한 폰트 색을 관리
 */
public class MNTranslucentFont {
    private static final String FONT_SHARED_PREFERENCES = "FONT_SHARED_PREFERENCES";
    private static final String FONT_KEY = "FONT_KEY";

    private volatile static MNTranslucentFont instance;
    private MNTranslucentFontType currentFontType;

    /**
     * Singleton
     */
    private MNTranslucentFont() {}
    private MNTranslucentFont(Context context) {
        if (context != null) {
            int uniqueId = context.getSharedPreferences(FONT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    .getInt(FONT_KEY, MNTranslucentFontType.BLACK.getUniqueId());

            currentFontType = MNTranslucentFontType.valueOfUniqueId(uniqueId);
        }
    }
    public static MNTranslucentFont getInstance(Context context) {
        if (instance == null) {
            synchronized (MNTranslucentFont.class) {
                if (instance == null) {
                    instance = new MNTranslucentFont(context);
                }
            }
        }
        return instance;
    }

    /**
     * Utility Methods
     */
    public static MNTranslucentFontType getCurrentFontType(Context context) {
        return MNTranslucentFont.getInstance(context).currentFontType;
    }

    public static void toggleFontType(Context context) {
        if (MNTranslucentFont.getInstance(context).currentFontType == MNTranslucentFontType.BLACK) {
            setThemeType(MNTranslucentFontType.WHITE, context);
        } else {
            setThemeType(MNTranslucentFontType.BLACK, context);
        }
    }

    public static void setThemeType(MNTranslucentFontType newFontType, Context context) {
        MNTranslucentFont.getInstance(context).currentFontType = newFontType;
        context.getSharedPreferences(FONT_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(FONT_KEY, newFontType.getUniqueId()).apply();
    }
}
