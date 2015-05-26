package com.yooiistudios.morningkit.common.locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.Locale;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 8. 22.
 *
 * MNLocaleUtils
 *  필요할 때 언어가 원래대로 돌아가는 문제를 해결하기 위해 로케일을 새로 적용해 주는 유틸 클래스
 */
public class MNLocaleUtils {
    private static final String LOCALE_SHARED_PREFERENCES = "locale_shared_preferences";
    private static final String COUNTRY_KEY = "country_key";
    private static final String LANGUAGE_KEY = "language_key";

    private MNLocaleUtils() { throw new AssertionError("You MUST not create this class!"); }

    public static void updateLocale(Context context) {
        // 회전마다 Locale 을 새로 적용해줌(언어가 바뀌어 버리는 문제 해결)
        Configuration config = context.getApplicationContext().getResources().getConfiguration();
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(context);
        Locale locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
        Locale.setDefault(locale);
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config,
                context.getApplicationContext().getResources().getDisplayMetrics());
    }

    public static void saveDefaultLocaleIfNecessary(Context context, Locale locale) {
        SharedPreferences prefs =
                context.getSharedPreferences(LOCALE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if (!prefs.contains(LANGUAGE_KEY)) {
            saveDefaultLocale(context, locale);
        }
    }

    public static void saveDefaultLocale(Context context, Locale locale) {
        SharedPreferences prefs =
                context.getSharedPreferences(LOCALE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        MNLog.now("COUNTRY_KEY: " + locale.getCountry());
        MNLog.now("LANGUAGE_KEY: " + locale.getLanguage());
        prefs.edit().putString(COUNTRY_KEY, locale.getCountry()).apply();
        prefs.edit().putString(LANGUAGE_KEY, locale.getLanguage()).apply();
    }

    public static Locale loadDefaultLocale(Context context) {
        // 제대로 읽히지 않을경우 미국-영어로 저장
        SharedPreferences prefs =
                context.getSharedPreferences(LOCALE_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String country = prefs.getString(COUNTRY_KEY, "US");
        String language = prefs.getString(LANGUAGE_KEY, "en");
        return new Locale(language, country);
    }
}
