package com.yooiistudios.morningkit.panel.newsfeed.model;

import android.util.SparseArray;

import com.yooiistudios.morningkit.R;

/**
 * Created by Wooseong Kim in News-Android-L from Yooii Studios Co., LTD. on 2014. 9. 25.
 *
 * NewsProviderLangType
 *  뉴스 선택화면 - 탭 순서를 정하기 위한 enum
 */
public enum MNNewsProviderLangType {
    ENGLISH(0, R.raw.news_data_en, "English"),
    FRENCH(1, R.raw.news_data_fr, "Français"),
    SPANISH(2, R.raw.news_data_es, "Español"),
    PORTUGUESE(3, R.raw.news_data_pt, "Português"),
    HINDI(4, R.raw.news_data_hi, "हिन्दी"),
    CHINESE_CN(5, R.raw.news_data_zh_cn, "简体中文"),
    CHINESE_TW(6, R.raw.news_data_zh_tw, "繁體中文"),
    JAPANESE(7, R.raw.news_data_ja, "日本語"),
    KOREAN(8, R.raw.news_data_ko, "한국어"),
    VIETNAMESE(9, R.raw.news_data_vi, "Tiếng Việt"),
    ARABIC(10, R.raw.news_data_ar, "العربية"),
    PERSIAN(11, R.raw.news_data_fa, "فارسی"),
    RUSSIAN(12, R.raw.news_data_ru, "Pусский"),
    GERMAN(13, R.raw.news_data_de, "Deutsch"),
    DUTCH(14, R.raw.news_data_nl, "Nederlands"),
    ITALIAN(15, R.raw.news_data_it, "Italiano"),
    NORWEGIAN(16, R.raw.news_data_nb, "Norsk Bokmål"),
    SWEDISH(17, R.raw.news_data_sv, "Svenska"),
    FINNISH(18, R.raw.news_data_fi, "Suomi"),
    DANISH(19, R.raw.news_data_da, "Dansk"),
    TURKISH(20, R.raw.news_data_tr, "Türk"),
    GREEK(21, R.raw.news_data_el, "Ελληνικά"),
    CZECH(22, R.raw.news_data_cs, "Čeština"),
    SLOVAK(23, R.raw.news_data_sk, "Slovenčina"),
    ESTONIAN(24, R.raw.news_data_ee, "eesti"),
    LITHUANIAN(25, R.raw.news_data_lt, "Lietuvos"),
    LATVIAN(26, R.raw.news_data_lv, "Latvijas"),
    POLISH(27, R.raw.news_data_pl, "Język polski"),
    ROMANIAN(28, R.raw.news_data_ro, "Român"),
    CROATIAN(29, R.raw.news_data_hr, "Hrvatski"),
    KAZAKH(30, R.raw.news_data_kk, "Қазақ"),
    UKRAINIAN(31, R.raw.news_data_uk, "Украї́нська"),
    HUNGARIAN(32, R.raw.news_data_hu, "Magyar"),
    BULGARIAN(33, R.raw.news_data_bg, "Български"),
    BELARUSIAN(34, R.raw.news_data_be, "беларускі"),
    SERBIAN(35, R.raw.news_data_sr, "Cрпски"),
    HEBREW(36, R.raw.news_data_il, "עברית"),
    THAI(37, R.raw.news_data_th, "ไทย"),
    MALAY(38, R.raw.news_data_ms, "Malay"),
    INDONESIAN(39, R.raw.news_data_in, "Indonesia");

    // index 에 해당하는 NewsProviderLangType 를 찾기 위해 매번 switch, if-else, for 등으로
    // 체크하지 않고 초기 클래스가 로드될 경우 한번만 전체 타입을 검사, 맵으로 만들어 둠으로써
    // 가독성 및 퍼포먼스 향상
    private static final SparseArray<MNNewsProviderLangType> TYPES;

    private int mIndex;
    private int mResourceId;
    private String mTitle;

    static {
        TYPES = new SparseArray<MNNewsProviderLangType>();

        for (MNNewsProviderLangType type : MNNewsProviderLangType.values()) {
            TYPES.put(type.getIndex(), type);
        }
    }

    MNNewsProviderLangType(int index, int resourceId, String title) {
        mIndex = index;
        mResourceId = resourceId;
        mTitle = title;
    }

    public static MNNewsProviderLangType valueOf(int index) {
        return TYPES.get(index, ENGLISH);
    }

    public int getIndex() { return mIndex; }
    public int getResourceId() { return mResourceId; }
    public String getTitle() { return mTitle; }
}
