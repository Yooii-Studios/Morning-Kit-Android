package com.yooiistudios.morningkit.panel.quotes.model;

import android.content.Context;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.ArrayList;

// 순서는 절대 바꾸면 안됨. 순서가 곧 uniqueId 가 됨.
public enum MNQuotesLanguage {
	ENGLISH(0, "English", R.raw.quotes_english),
    JAPANESE(2, "Japanese", R.raw.quotes_japanese),
    KOREAN(1, "Korean", R.raw.quotes_korean),
	SIMPLIFIED_CHINESE(3, "Simplified Chinese", R.raw.quotes_chinese_simplified),
	TRADITIONAL_CHINESE(4, "Traditional Chinese", R.raw.quotes_chinese_traditional),
    SPANISH(6, "Spanish", R.raw.quotes_spanish),
    FRENCH(7, "French", R.raw.quotes_french);

    final int uniqueId;
	final String name;
    final int rawDataFileId;

	MNQuotesLanguage(int _uniqueId, String _name, int _rawDataFileId) {
        uniqueId = _uniqueId;
		name = _name;
        rawDataFileId = _rawDataFileId;
	}

    public static ArrayList<Boolean> initFirstQuoteLanguage(Context context) {
        // 현재 언어에 따라 첫 명언 언어 설정해주기
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(context);

        // 현재 언어의 uniqueId와 비교해 true 를 세팅
        ArrayList<Boolean> selectedLanguages = new ArrayList<Boolean>();
        int languageIndex = -1;
        for (int i = 0; i < MNQuotesLanguage.values().length; i++) {
            if (currentLanguageType.getUniqueId() == MNQuotesLanguage.values()[i].uniqueId) {
                selectedLanguages.add(true);
                languageIndex = i;
            } else {
                selectedLanguages.add(false);
            }
        }

        // 언어에서 지원하지 않는 언어인 경우 영어로 선택
        if (languageIndex == -1) {
            selectedLanguages.set(0, true);
        }

        return selectedLanguages;
    }
}
