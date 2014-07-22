package com.yooiistudios.morningkit.panel.newsfeed.model;

import java.io.Serializable;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 21.
 */
public class MNNewsFeedUrl implements Serializable {
    private String mUrl;
    private MNNewsFeedUrlType mType;

    public MNNewsFeedUrl(String url, MNNewsFeedUrlType type) {
        mUrl = url;
        mType = type;
    }
    public MNNewsFeedUrl(MNNewsFeedUrl feedUrl) {
        this.mUrl = feedUrl.getUrl();
        this.mType = feedUrl.getType();
    }

    public String getUrl() {
        return mUrl;
    }
    public MNNewsFeedUrlType getType() {
        return mType;
    }
}
