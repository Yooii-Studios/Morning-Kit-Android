package com.yooiistudios.morningkit.panel.newsfeed.util;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 3.
 */
public class MNNewsFeedUtil {

    public static String getDefaultFeedUrl(Context context) {
        MNLanguageType type = MNLanguage.getCurrentLanguageType(context);

        String feedUrl;

        //TODO 언어별 기본 RSS피드 주소 입력해야함.
        switch(type) {
            case ENGLISH:
                feedUrl = "";
                break;
            case KOREAN:
                feedUrl = "";
                break;
            case JAPANESE:
                feedUrl = "";
                break;
            case TRADITIONAL_CHINESE:
                feedUrl = "";
                break;
            case SIMPLIFIED_CHINESE:
                feedUrl = "";
                break;
            case RUSSIAN:
                feedUrl = "";
                break;
            default:
                feedUrl = "";
                break;
        }
//        feedUrl = "http://sweetpjy.tistory.com/rss";
        feedUrl = "http://www.cnet.com/rss/iphone-update/";


        return feedUrl;
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
}
