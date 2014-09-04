package com.yooiistudios.morningkit.panel.core;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNLanguageType
 *  모닝키트의 언어를 enum으로 표현
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNPanelType {
    WEATHER(0, 0),
    DATE(1, 1),
    CALENDAR(2, 2),
    WORLD_CLOCK(3, 3),
    NEWS_FEED(4, 10),
    FLICKR(5, 5),
    EXCHANGE_RATES(6, 6),
    QUOTES(7, 4),
    MEMO(8, 7),
    DATE_COUNTDOWN(9, 8),
    PHOTO_FRAME(10, 9),
    CAT(11, 11),
    STORE(12, 9999); // 특수 타입, 패널은 아니나 상점을 위한 구분자

    @Getter private final int index; // 리스트뷰에 표시할 용도의 index
    @Getter private final int uniqueId; // SharedPreferences에 저장될 용도의 unique id

    MNPanelType(int index, int uniqueId) {
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public static MNPanelType valueOf(int index) {
        switch (index) {
            case 0: return WEATHER;
            case 1: return DATE;
            case 2: return CALENDAR;
            case 3: return WORLD_CLOCK;
            case 4: return NEWS_FEED;
            case 5: return FLICKR;
            case 6: return EXCHANGE_RATES;
            case 7: return QUOTES;
            case 8: return MEMO;
            case 9: return DATE_COUNTDOWN;
            case 10: return PHOTO_FRAME;
            case 11: return CAT;
            default: return DATE;
        }
    }

    public static MNPanelType valueOfUniqueId(int uniqueId) {
        switch (uniqueId) {
            case 0: return WEATHER;
            case 1: return DATE;
            case 2: return CALENDAR;
            case 3: return WORLD_CLOCK;
            case 4: return QUOTES;
            case 5: return FLICKR;
            case 6: return EXCHANGE_RATES;
            case 7: return MEMO;
            case 8: return DATE_COUNTDOWN;
            case 9: return PHOTO_FRAME;
            case 10: return NEWS_FEED;
            case 11: return CAT;
            default: return DATE;
        }
    }

    public static String toString(int index, Context context) {
        switch (index) {
            case 0: return context.getString(R.string.weather);
            case 1: return context.getString(R.string.calendar);
            case 2: return context.getString(R.string.reminder);
            case 3: return context.getString(R.string.world_clock);
            case 4: return context.getString(R.string.news_feed);
            case 5: return context.getString(R.string.flickr);
            case 6: return context.getString(R.string.exchange_rate);
            case 7: return context.getString(R.string.saying);
            case 8: return context.getString(R.string.memo);
            case 9: return context.getString(R.string.date_calculator);
            case 10: return context.getString(R.string.store_item_widget_photo_album);
            case 11: return context.getString(R.string.cat);
            default: return context.getString(R.string.calendar);
        }
    }
}
