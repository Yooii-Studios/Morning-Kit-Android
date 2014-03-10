package com.yooiistudios.morningkit.panel.quotes.model;

import com.yooiistudios.morningkit.R;

public enum MNQuotesLanguage {
	ENGLISH("English", R.raw.quotes_english),
	KOREAN("Korean", R.raw.quotes_korean),
	JAPANESE("Japanese", R.raw.quotes_japanese),
	SIMPLIFIED_CHINESE("Simplified Chinese", R.raw.quotes_chinese_simplified),
	TRADITIONAL_CHINESE("Traditional Chinese", R.raw.quotes_chinese_traditional);

	final String name;
    final int rawDataFileId;
	
	MNQuotesLanguage(String _name, int _rawDataFileId) {
		name = _name;
        rawDataFileId = _rawDataFileId;
	}

    public static MNQuotesLanguage valueOf(int index) {
        switch (index) {
            case 0: return ENGLISH;
            case 1: return KOREAN;
            case 2: return JAPANESE;
            case 3: return SIMPLIFIED_CHINESE;
            case 4: return TRADITIONAL_CHINESE;
            default: throw new AssertionError("Undefined MNQuotesLanguage index!");
        }
    }
}
