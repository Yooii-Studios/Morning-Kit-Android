package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.common.log.MNFlurry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNTheme (유틸리티 클래스)
 *  테마와 관련된 모든 처리를 관장
 */
public class MNTheme {
    private static final String THEME_SHARED_PREFERENCES = "THEME_SHARED_PREFERENCES";
    private static final String THEME_KEY= "THEME_KEY";

    private volatile static MNTheme instance;
    private MNThemeType currentThemeType;

    /**
     * Singleton
     */
    private MNTheme() {}
    private MNTheme(Context context) {
        if (context != null) {
            int uniqueId = context.getSharedPreferences(THEME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    .getInt(THEME_KEY, MNThemeType.PASTEL_GREEN.getUniqueId());

            currentThemeType = MNThemeType.valueOfUniqueId(uniqueId);
        }
    }
    public static MNTheme getInstance(Context context) {
        if (instance == null) {
            synchronized (MNTheme.class) {
                if (instance == null) {
                    instance = new MNTheme(context);
                }
            }
        }
        return instance;
    }

    /**
     * Utility Methods
     */
    public static MNThemeType getCurrentThemeType(Context context) {
        return MNTheme.getInstance(context).currentThemeType;
    }

    public static void setThemeType(MNThemeType newThemeType, Context context) {
        MNTheme.getInstance(context).currentThemeType = newThemeType;
        context.getSharedPreferences(THEME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(THEME_KEY, newThemeType.getUniqueId()).commit();

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.THEME, newThemeType.toString());
        FlurryAgent.logEvent(MNFlurry.ON_SETTING_THEME, params);
    }
}
