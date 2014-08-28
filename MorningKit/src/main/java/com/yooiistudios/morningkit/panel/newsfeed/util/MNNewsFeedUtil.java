package com.yooiistudios.morningkit.panel.newsfeed.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.PREF_NEWS_FEED;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 3.
 */
public class MNNewsFeedUtil {
    private static final String KEY_HISTORY = "url hisotry";
    private static final int MAX_HISTORY_SIZE = 10;

    private static final String HISTORY_DELIM = "|";
    private static final String NEWS_PROVIDER_YAHOO_JAPAN = "Yahoo!ニュース";

    public static MNNewsFeedUrl getDefaultFeedUrl(Context context) {
        MNLanguageType type = MNLanguage.getCurrentLanguageType(context);

        String feedUrl;
        MNNewsFeedUrlType urlType;

        switch(type) {
            case ENGLISH:
                feedUrl = "http://news.google.com/news?cf=all&ned=us&hl=en&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case KOREAN:
                feedUrl = "http://news.google.com/news?cf=all&ned=kr&hl=ko&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case JAPANESE:
                feedUrl = "http://rss.dailynews.yahoo.co.jp/fc/rss.xml";
                urlType = MNNewsFeedUrlType.YAHOO;
                break;
            case TRADITIONAL_CHINESE:
                feedUrl = "http://news.google.com/news?cf=all&ned=tw&hl=zh-TW&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case SIMPLIFIED_CHINESE:
                feedUrl = "http://news.google.com/news?cf=all&ned=cn&hl=zh-CN&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case RUSSIAN:
                feedUrl = "http://news.google.com/news?cf=all&ned=ru_ru&hl=ru&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case FRENCH:
                feedUrl = "http://news.google.com/news?cf=all&ned=fr&hl=fr&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            case SPANISH:
                feedUrl = "http://news.google.com/news?cf=all&ned=es&hl=es&topic=w&output=rss";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
            default:
                feedUrl = "";
                urlType = MNNewsFeedUrlType.GOOGLE;
                break;
        }
//        feedUrl = "http://sweetpjy.tistory.com/rss";
//        feedUrl = "http://www.cnet.com/rss/iphone-update/";


        return new MNNewsFeedUrl(feedUrl, urlType);
    }

    public static String getRssFeedJsonString(RssFeed feed) {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy
                () {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return (clazz == RssItem.class);
                    }
                }).serializeNulls().create().toJson(feed);
    }
    public static String getRssItemArrayListString(ArrayList<RssItem>
                                                           itemList) {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy
                () {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return (clazz == RssFeed.class);
            }
        }).serializeNulls().create().toJson(itemList);
    }

    public static void addUrlToHistory(Context context, String url) {
        ArrayList<String> urlList = getUrlHistory(context);

        // if list contains url, remove and add it at 0th index.
        if (urlList.contains(url)) {
            urlList.remove(url);
        }
        // put recent url at 0th index.
        urlList.add(0, url);

        // remove last history if no room.
        if (urlList.size() > MAX_HISTORY_SIZE) {
            urlList.remove(urlList.size()-1);
        }

        SharedPreferences prefs = context.getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);

        prefs.edit().putString(KEY_HISTORY, new Gson().toJson(urlList)).apply();
    }

    public static ArrayList<String> getUrlHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);
        String historyJsonStr = prefs.getString(KEY_HISTORY, null);

        if (historyJsonStr != null) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            return new Gson().fromJson(historyJsonStr, type);
        }
        else {
            return new ArrayList<String>();
        }
    }

    /**
     *
     * @param news
     * @param type
     * @return retval
     * retval[0] : title.
     * retval[1] : publisher or null if there's no publisher info.
     *
     */
    public static String[] getTitleAndPublisherName(RssItem news,
                                          MNNewsFeedUrlType type) {
        String title = news.getTitle();
        String newTitle;
        String publisher;
        switch (type) {
            case GOOGLE:
                final String delim = " - ";
                int idx = title.lastIndexOf(delim);

                int titleStartIdx = 0;
                int titleEndIdx = idx;
                int pubStartIdx = idx+delim.length();
                int pubEndIdx = title.length();

                if (idx >= 0 &&
                        titleEndIdx >= titleStartIdx &&
                        pubEndIdx >= pubStartIdx) {
                // title.length() >= delim.length()
                    newTitle = title.substring(titleStartIdx, titleEndIdx);
                    publisher = "- " + title.substring(pubStartIdx, pubEndIdx);
                }
                else {
                    newTitle = title;
                    publisher = null;
                }

                break;
            case YAHOO:
                newTitle = title;
                publisher = NEWS_PROVIDER_YAHOO_JAPAN;
                break;
            case CUSTOM:
            default:
                newTitle = title;
                publisher = null;
                break;
        }

        return new String[]{newTitle, publisher};
    }

    public static String getFeedTitle(Context context) {
        MNLanguageType currentLanguage = MNLanguage.getCurrentLanguageType(context);

        String provider;

        if (currentLanguage.equals(MNLanguageType.JAPANESE)) {
            provider = NEWS_PROVIDER_YAHOO_JAPAN;
        }
        else {
            provider = null;
        }

        return provider;
    }
}
