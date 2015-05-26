package com.yooiistudios.morningkit.panel.newsfeed.model;

import java.io.Serializable;

/**
 * Created by Wooseong Kim in News-Android-L from Yooii Studios Co., LTD. on 15. 2. 17.
 *
 * NewsProviderCountry
 *  해당 국가의 이름과 코드, 언론사들을 가지는 자료구조
 */
public class MNNewsProviderCountry implements Serializable {
    // identifiers
    public String languageCode;
    public String regionCode;

    // data
    public String countryLocalName;
    public String countryCode;

    public String url;
}
