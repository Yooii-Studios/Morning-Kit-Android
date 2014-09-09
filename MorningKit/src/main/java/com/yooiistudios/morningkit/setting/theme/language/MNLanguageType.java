package com.yooiistudios.morningkit.setting.theme.language;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNLanguageType
 *  모닝키트의 언어를 enum 으로 표현
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNLanguageType {
    ENGLISH(0, 0, "en", ""),
    JAPANESE(1, 2, "ja", ""),
    KOREAN(2, 1, "ko", ""),
    SIMPLIFIED_CHINESE(3, 3, "zh", "CN"),
    TRADITIONAL_CHINESE(4, 4, "zh", "TW"),
    SPANISH(5, 6, "es", ""),
    FRENCH(6, 7, "fr", ""),
    GERMAN(7, 8, "de", ""),
    RUSSIAN(8, 5, "ru", "");

    @Getter private final int index; // 리스트뷰에 표시할 용도의 index
    @Getter private final int uniqueId; // SharedPreferences 에 저장될 용도의 unique id
    @Getter private final String code;
    @Getter private final String region;

    MNLanguageType(int index, int uniqueId, String code, String region) {
        this.index = index;
        this.uniqueId = uniqueId;
        this.code = code;
        this.region = region;
    }

    public static MNLanguageType valueOf(int index) {

        switch (index) {
            case 0: return ENGLISH;
            case 1: return JAPANESE;
            case 2: return KOREAN;
            case 3: return SIMPLIFIED_CHINESE;
            case 4: return TRADITIONAL_CHINESE;
            case 5: return SPANISH;
            case 6: return FRENCH;
            case 7: return GERMAN;
            case 8: return RUSSIAN;
            default: return ENGLISH;
        }
    }

    public static MNLanguageType valueOfUniqueId(int uniqueId) {

        switch (uniqueId) {
            case 0: return ENGLISH;
            case 1: return KOREAN;
            case 2: return JAPANESE;
            case 3: return SIMPLIFIED_CHINESE;
            case 4: return TRADITIONAL_CHINESE;
            case 5: return RUSSIAN;
            case 6: return SPANISH;
            case 7: return FRENCH;
            case 8: return GERMAN;
            default: return ENGLISH;
        }
    }

    public static String toTranselatedString(int position, Context context) {
        switch (position) {
            case 0: return context.getString(R.string.setting_language_english);
            case 1: return context.getString(R.string.setting_language_japanese);
            case 2: return context.getString(R.string.setting_language_korean);
            case 3: return context.getString(R.string.setting_language_simplified_chinese);
            case 4: return context.getString(R.string.setting_language_traditional_chinese);
            case 5: return context.getString(R.string.setting_language_spanish);
            case 6: return context.getString(R.string.setting_language_french);
            case 7: return context.getString(R.string.setting_language_german);
            case 8: return context.getString(R.string.setting_language_russian);
            default: return context.getString(R.string.setting_language_english);
        }
    }

    public static String toEnglishString(int position) {
        switch (position) {
            case 0: return "English";
            case 1: return "Japanese";
            case 2: return "Korean";
            case 3: return "Chinese (Simplified)";
            case 4: return "Chinese (Traditional)";
            case 5: return "Spanish";
            case 6: return "French";
            case 7: return "German";
            case 8: return "Russian";
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static MNLanguageType valueOfCodeAndRegion(String code, String region) {
        if (code.equals("ko")) {
            return KOREAN;
        } else if (code.equals("ja")) {
            return JAPANESE;
        } else if (code.equals("zh") && region.equals("CN")) {
            return SIMPLIFIED_CHINESE;
        } else if (code.equals("zh") && region.equals("TW")) {
            return TRADITIONAL_CHINESE;
        } else if (code.equals("ru")) {
            return RUSSIAN;
        } else if (code.equals("es")) {
            return SPANISH;
        } else if (code.equals("fr")) {
            return FRENCH;
        } else if (code.equals("de")) {
            return GERMAN;
        } else {
            // Default
            return ENGLISH;
        }
    }
}
