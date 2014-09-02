package com.yooiistudios.morningkit.panel.quotes.model;

import android.content.Context;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.ArrayList;

public enum MNQuotesLanguage {
	ENGLISH("English", R.raw.quotes_english),
    JAPANESE("Japanese", R.raw.quotes_japanese),
    KOREAN("Korean", R.raw.quotes_korean),
	SIMPLIFIED_CHINESE("Simplified Chinese", R.raw.quotes_chinese_simplified),
	TRADITIONAL_CHINESE("Traditional Chinese", R.raw.quotes_chinese_traditional);

	final String name;
    final int rawDataFileId;
	
	MNQuotesLanguage(String _name, int _rawDataFileId) {
		name = _name;
        rawDataFileId = _rawDataFileId;
	}

    public static ArrayList<Boolean> initFirstQuoteLanguage(Context context) {
        // 현재 언어에 따라 첫 명언 언어 설정해주기
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(context);

        // 로직 수정: 모두 false 처리를 하고, 해당 uniqueId를 확인해 해당 언어이면, 해당 index 에 true 를 세팅
        ArrayList<Boolean> selectedLanguages = new ArrayList<Boolean>();
        for (int i = 0; i < 5; i++) {
            selectedLanguages.add(false);
        }

        int languageIndex = -1;
        for (int i = 0; i < 5; i++) {
            if (currentLanguageType.getUniqueId() == i) {
                selectedLanguages.set(currentLanguageType.getIndex(), true);
                languageIndex = i;
            }
        }

        // 언어에서 지원하지 않는 언어인 경우 영어로 선택
        if (languageIndex == -1) {
            selectedLanguages.set(0, true);
        }

        return selectedLanguages;
    }

    public static MNQuotesLanguage valueOf(int index) {
        switch (index) {
            case 1: return JAPANESE;
            case 2: return KOREAN;
            case 3: return SIMPLIFIED_CHINESE;
            case 4: return TRADITIONAL_CHINESE;
            default: return ENGLISH;
        }
    }
}
