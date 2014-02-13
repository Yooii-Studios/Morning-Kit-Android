package com.yooiistudios.morningkit.setting.theme.language;

import android.content.Context;

import java.util.Locale;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNLanguage
 *  언어 설정을 관리
 */
public class MNLanguage {
    private static final String LANGUAGE_SHARED_PREFERENCES = "LANGUAGE_SHARED_PREFERENCES";
    private static final String LANGUAGE_MATRIX_KEY= "LANGUAGE_MATRIX_KEY";

    private volatile static MNLanguage instance;
    private MNLanguageType currentLanguageType;

    /**
     * Singleton
     */
    private MNLanguage(){}
    private MNLanguage(Context context) {
        int uniqueId = context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getInt(LANGUAGE_MATRIX_KEY, -1);

        // 최초 설치시 디바이스의 언어와 비교해 앱이 지원하는 언어면 해당 언어로 설정, 아닐 경우 영어로 첫 언어 설정
        if (uniqueId == -1) {
            Locale locale = Locale.getDefault();
            currentLanguageType = MNLanguageType.valueOfCodeAndRegion(locale.getLanguage(), locale.getCountry());
            // 아카이브
            context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    .edit().putInt(LANGUAGE_MATRIX_KEY, currentLanguageType.getUniqueId()).commit();
        } else {
            currentLanguageType = MNLanguageType.valueOfUniqueId(uniqueId);
        }
    }

    public static MNLanguage getInstance(Context context) {
        if (instance == null) {
            synchronized (MNLanguage.class) {
                if (instance == null) {
                    instance = new MNLanguage(context);
                }
            }
        }
        return instance;
    }

    public static MNLanguageType getCurrentLanguageType(Context context) { return MNLanguage.getInstance(context).currentLanguageType; }

    public static void setLanguageType(MNLanguageType newNewLanguage, Context context) {
        MNLanguage.getInstance(context).currentLanguageType = newNewLanguage;
        context.getSharedPreferences(LANGUAGE_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(LANGUAGE_MATRIX_KEY, newNewLanguage.getUniqueId()).commit();
    }
}
