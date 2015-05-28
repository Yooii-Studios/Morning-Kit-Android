package com.yooiistudios.morningkit.panel.newsfeed.model;

import java.io.Serializable;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 21.
 *
 * MNNewsFeedUrl
 *  뉴스피드의 url 을 나타내는 클래스
 */
public class MNNewsFeedUrl implements Serializable {
    public String url;
    public MNNewsFeedUrlType type;
    public String languageCode = "";
    public String regionCode = "";
    public String countryCode = "";
    public String providerName = "";

    public MNNewsFeedUrl(String url, MNNewsFeedUrlType type) {
        this.url = url;
        this.type = type;
    }

    public MNNewsFeedUrl(String url, MNNewsFeedUrlType type,
                         String languageCode, String regionCode, String countryCode,
                         String providerName) {
        this(url, type);
        this.languageCode = languageCode;
        this.regionCode = regionCode;
        this.countryCode = countryCode;
        this.providerName = providerName;
    }

    public MNNewsFeedUrl(MNNewsFeedUrl feedUrl) {
        this(feedUrl.url, feedUrl.type,
                feedUrl.languageCode, feedUrl.regionCode, feedUrl.countryCode, feedUrl.providerName);
    }

    public MNNewsFeedUrl(MNNewsProviderCountry newsProviderCountry, MNNewsFeedUrlType type) {
        this(newsProviderCountry.url, type,
                newsProviderCountry.languageCode, newsProviderCountry.regionCode,
                newsProviderCountry.countryCode, newsProviderCountry.newsProviderName);
    }
}
