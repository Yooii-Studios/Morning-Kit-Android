package com.yooiistudios.morningkit.panel.newsfeed.util;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.yooiistudios.morningkit.common.locale.MNLocaleUtils;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderCountry;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLangType;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Created by Dongheyon Jeong in morning-kit from Yooii Studios Co., LTD. on 15. 5. 25.
 *
 * MNDefaultNewsFeedUrlProvider
 *  Curation List 를 파싱, 제공하는 클래스
 */
public class MNNewsFeedUrlProvider {
    /**
     * Singleton
     */
    private volatile static MNNewsFeedUrlProvider instance;

    private LinkedHashMap<String, MNNewsProviderLanguage> mNewsLanguages;
    private Context mContext;

    public static MNNewsFeedUrlProvider getInstance(Context context) {
        if (instance == null) {
            synchronized (MNNewsFeedUrlProvider.class) {
                if (instance == null) {
                    instance = new MNNewsFeedUrlProvider(context);
                }
            }
        }
        return instance;
    }

    private MNNewsFeedUrlProvider(Context context) {
        mContext = context.getApplicationContext();
        mNewsLanguages = new LinkedHashMap<String, MNNewsProviderLanguage>();
        for (int i = 0; i < MNNewsProviderLangType.values().length; i++) {
            MNNewsProviderLangType type = MNNewsProviderLangType.valueOf(i);
            MNNewsProviderLanguage newsProviderLanguage
                    = parseNewsProvidersByResource(context, type.getResourceId());
            String languageRegionCode = MNNewsFeedUtil.makeLanguageRegionCode(
                    newsProviderLanguage.languageCode,
                    newsProviderLanguage.regionCode
            );
            mNewsLanguages.put(languageRegionCode, newsProviderLanguage);
        }
    }

    public MNNewsFeedUrl getDefault() {
        Locale defaultLocale = MNLocaleUtils.loadDefaultLocale(mContext);
        String langCode = defaultLocale.getLanguage();
        String countryCode = defaultLocale.getCountry();

        String langRegionCode;
        if (langCode.equals("zh")){
            langRegionCode = MNNewsFeedUtil.makeLanguageRegionCode(langCode, countryCode);
        } else {
            langRegionCode = MNNewsFeedUtil.makeLanguageRegionCode(langCode, null);
        }
        MNNewsProviderLanguage newsProviderLanguage = mNewsLanguages.get(langRegionCode);
        MNNewsProviderCountry providerCountry;
        if (newsProviderLanguage == null) {
            providerCountry = getDefaultNewsProviderCountry();
        } else {
            providerCountry = newsProviderLanguage.newsProviderCountries.get(countryCode);
        }

        // 위 한번 더 체크를 하는 로직을 추가, 크래시가 안 생기길 바래봄. 문제 상황을 발견해 다음에 해결을 위해 로그 추가
        if (providerCountry == null) {
            Crashlytics.log("countryCode: " + countryCode);
            Crashlytics.log("langRegionCode: " + langRegionCode);
            if (newsProviderLanguage != null) {
                Crashlytics.log("newsProviderLanguage.newsProviderCountries.size(): " +
                        newsProviderLanguage.newsProviderCountries.size());
            } else {
                Crashlytics.log("newsProviderLanguage == null");
            }

            // 해결을 위한 로직
            providerCountry = getDefaultNewsProviderCountry();
        }
        return new MNNewsFeedUrl(providerCountry, MNNewsFeedUrlType.CURATION);
    }

    public MNNewsProviderCountry getDefaultNewsProviderCountry() {
        MNNewsProviderLanguage newsProviderLanguage = mNewsLanguages.get("en");
        return newsProviderLanguage.newsProviderCountries.get("US");
    }

    public LinkedHashMap<String, MNNewsProviderLanguage> getUrlsSortedByLocale() {
        Locale locale = MNLocaleUtils.loadDefaultLocale(mContext);
        LinkedHashMap<String, MNNewsProviderLanguage> clonedLanguageProviders
                = new LinkedHashMap<String, MNNewsProviderLanguage>(mNewsLanguages);
        LinkedHashMap<String, MNNewsProviderLanguage> sortedLanguageProviders
                = new LinkedHashMap<String, MNNewsProviderLanguage>();

        String languageCode = locale.getLanguage();
        String countryCode = locale.getCountry();
        if (languageCode.equals("fr")) {
            // French English German Dutch Spanish Portuguese [French Spanish Portuguese 공통] 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nl");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "es");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "pt");

            putFrenchSpanishPortugueseNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders);
        } else if (languageCode.equals("es")) {
            // Spanish English Portuguese French German	Dutch [French Spanish Portuguese 공통] 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "es");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "pt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nl");

            putFrenchSpanishPortugueseNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders);
        } else if (languageCode.equals("pt")) {
            // Portuguese English Spanish French German	Dutch [French Spanish Portuguese 공통] 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "pt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "es");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nl");

            putFrenchSpanishPortugueseNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders);
        } else if (languageCode.equals("hi")) {
            // Hindi 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "hi");
        } else if (languageCode.equals("zh") && countryCode.equals("CN")) {
            // S-Chinese T-Chinese English Japanese	Korean 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "cn");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "tw");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ja");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ko");
        } else if (languageCode.equals("zh") && countryCode.equals("TW")) {
            // T-Chinese S-Chinese English Japanese	Korean 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "tw");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "cn");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ja");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ko");
        } else if (languageCode.equals("ja")) {
            // Japanese English S-Chinese T-Chinese Korean 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ja");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "cn");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "tw");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ko");
        } else if (languageCode.equals("ko")) {
            // Korean English Japanese S-Chinese T-Chinese 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ko");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ja");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "cn");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "tw");
        } else if (languageCode.equals("vi")) {
            // Vietnamese English S-Chinese T-Chinese Japanese Korean Thai 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "vi");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "cn");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "zh", "tw");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ja");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ko");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "th");
        } else if (languageCode.equals("ar")) {
            // Arabic Persian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ar");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fa");
        } else if (languageCode.equals("fa")) {
            // Persian Arabic 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fa");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ar");
        } else if (languageCode.equals("ru")) {
            // Russian English Kazakh Ukrainian Estonian Lithuanian Latvian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "kk");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "uk");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ee");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lv");
        } else if (languageCode.equals("de")) {
            // German 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
        } else if (languageCode.equals("nl")) {
            // Dutch English German 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nl");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
        } else if (languageCode.equals("it")) {
            // Italian English French Spanish Portuguese German 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "it");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "es");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "pt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "de");
        } else if (languageCode.equals("nb")) {
            // Norwegian English Swedish Finnish Danish 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nb");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sv");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fi");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "da");
        } else if (languageCode.equals("sv")) {
            // Swedish English Norwegian Finnish Danish 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sv");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nb");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fi");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "da");
        } else if (languageCode.equals("fi")) {
            // Finnish English Swedish Norwegian Danish 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fi");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sv");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nb");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "da");
        } else if (languageCode.equals("da")) {
            // Danish English Norwegian Swedish Finnish 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "da");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "nb");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sv");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fi");
        } else if (languageCode.equals("tr")) {
            // Turkish English Italian Spanish Portuguese French 우선정렬
            // Turkish English Greek 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "tr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "el");
        } else if (languageCode.equals("el")) {
            // Greek English Turkish 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "el");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "tr");
        } else if (languageCode.equals("cs")) {
            // Czech English Slovak 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "cs");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sk");
        } else if (languageCode.equals("sk")) {
            // Slovak English Czech 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sk");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "cs");
        } else if (languageCode.equals("ee")) {
            // Estonian English Lithuanian Latvian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ee");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lv");
        } else if (languageCode.equals("lt")) {
            // Lithuanian English Estonian Latvian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lt");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ee");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lv");
        } else if (languageCode.equals("lv")) {
            // Latvian English Estonian Lithuanian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lv");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ee");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "lt");
        } else if (languageCode.equals("pl")) {
            // Polish English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "pl");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("ro")) {
            // Romanian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ro");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("hr")) {
            // Croatian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "hr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("kk")) {
            // Kazakh English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "kk");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("uk")) {
            // Ukrainian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "uk");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("hu")) {
            // Hungarian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "hu");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("bg")) {
            // Bulgarian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "bg");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("be")) {
            // Belarusian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "be");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("sr")) {
            // Serbian English Russian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "sr");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ru");
        } else if (languageCode.equals("il")) {
            // Hebrew English Arabic Persian 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "il");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ar");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "fa");
        } else if (languageCode.equals("th")) {
            // Thai English Vietnamese Malay 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "th");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "vi");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ms");
        } else if (languageCode.equals("ms")) {
            // Malay English Thai Vietnamese 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ms");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "th");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "vi");
        } else if (languageCode.equals("in")) {
            // Indonesia English Malay 우선정렬
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "in");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "en");
            putNewsProviderLanguage(clonedLanguageProviders, sortedLanguageProviders, "ms");
        }

        if (sortedLanguageProviders.size() == 0) {
            sortedLanguageProviders = clonedLanguageProviders;
        } else {
            LinkedHashSet<String> keySet = new LinkedHashSet<String>(clonedLanguageProviders.keySet());

            for (String clonedKey : keySet) {
                sortedLanguageProviders.put(clonedKey, clonedLanguageProviders.remove(clonedKey));
            }
        }

        return sortedLanguageProviders;
    }

    private static void putFrenchSpanishPortugueseNewsProviderLanguage(
            LinkedHashMap<String, MNNewsProviderLanguage> clonedNewsProviders,
            LinkedHashMap<String, MNNewsProviderLanguage> sortedNewsProviders) {
        // Italian Norwegian Swedish Finnish Danish	Turkish	Greek Russian Czech Slovak Estonian
        // Lithuanian Latvian Polish Romanian Croatian Kazakh Ukrainian Hungarian Bulgarian
        // Belarusian Serbian Hebrew
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "it");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "nb");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "sv");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "fi");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "da");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "tr");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "el");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "ru");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "cs");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "sk");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "ee");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "lt");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "lv");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "pl");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "ro");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "hr");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "kk");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "uk");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "hu");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "bg");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "be");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "sr");
        putNewsProviderLanguage(clonedNewsProviders, sortedNewsProviders, "il");
    }

    private static void putNewsProviderLanguage(LinkedHashMap<String, MNNewsProviderLanguage> cloned,
                                                LinkedHashMap<String, MNNewsProviderLanguage> sorted,
                                                String languageCode) {
        putNewsProviderLanguage(cloned, sorted, languageCode, "");
    }

    private static void putNewsProviderLanguage(LinkedHashMap<String, MNNewsProviderLanguage> cloned,
                                                LinkedHashMap<String, MNNewsProviderLanguage> sorted,
                                                String languageCode, String regionCode) {
        String languageRegionCode = MNNewsFeedUtil.makeLanguageRegionCode(languageCode, regionCode);
        sorted.put(languageRegionCode, cloned.remove(languageRegionCode));
    }

    private static MNNewsProviderLanguage parseNewsProvidersByResource(Context context, int resourceId) {
        // raw id 에서 json 스트링을 만들고 JSONObject 로 변환
        try {
            InputStream file;
            file = context.getResources().openRawResource(resourceId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            char[] buffer = new char[file.available()];
            reader.read(buffer);

            // 변환한 JSONObject 를 NewsProviders 로 변환
            JSONObject newsData = new JSONObject(new String(buffer));

            // NewsProviderLanguages
            MNNewsProviderLanguage newsProviderLanguage = new MNNewsProviderLanguage();
            newsProviderLanguage.englishLanguageName = newsData.getString("lang_name_english");
            newsProviderLanguage.regionalLanguageName = newsData.getString("lang_name_regional");
            newsProviderLanguage.languageCode = newsData.getString("lang_code");
            String regionCode = newsData.getString("region_code");
            if (!regionCode.equals("")) {
                newsProviderLanguage.regionCode = regionCode;
            }

            // NewsProviderCountries
            JSONArray newsProviderCountryArray = newsData.getJSONArray("countries");
            newsProviderLanguage.newsProviderCountries = parseNewsProviderCountries(
                    newsProviderCountryArray, newsProviderLanguage);

            return newsProviderLanguage;
        } catch (IOException ignored) {
        } catch (JSONException ignored) {
        }
        return null;
    }

    private static LinkedHashMap<String, MNNewsProviderCountry> parseNewsProviderCountries(
            JSONArray newsProviderCountryArray, MNNewsProviderLanguage newsProviderLanguage) throws JSONException {

        LinkedHashMap<String, MNNewsProviderCountry> newsProviderCountries = new LinkedHashMap<String, MNNewsProviderCountry>();

        for (int i = 0; i < newsProviderCountryArray.length(); i++) {
            JSONObject newsProviderCountryObject = newsProviderCountryArray.getJSONObject(i);
            MNNewsProviderCountry newsProviderCountry = new MNNewsProviderCountry();
            newsProviderCountry.languageCode = newsProviderLanguage.languageCode;
            newsProviderCountry.regionCode = newsProviderLanguage.regionCode;
            newsProviderCountry.countryLocalName = newsProviderCountryObject.getString("country_name");
            newsProviderCountry.countryCode = newsProviderCountryObject.getString("country_code");
            newsProviderCountry.newsProviderName = newsProviderCountryObject.getString("provider_name");
            newsProviderCountry.url = newsProviderCountryObject.getString("url");

            newsProviderCountries.put(newsProviderCountry.countryCode, newsProviderCountry);
        }
        return newsProviderCountries;
    }
}
