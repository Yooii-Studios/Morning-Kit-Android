package com.yooiistudios.morningkit.panel.calendar.model;

import java.util.ArrayList;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 4.
 *
 * MNCalendarEventList
 *  앱에 맞게 캘린더의 이벤트가 정렬된 리스트
 */
public class MNCalendarEventList {
    public ArrayList<MNCalendarEvent> todayAlldayEvents;
    public ArrayList<MNCalendarEvent> todayScheduledEvents;

    public ArrayList<MNCalendarEvent> tomorrowAlldayEvents;
    public ArrayList<MNCalendarEvent> tomorrowScheduledEvents;

    public MNCalendarEventList() {
        todayAlldayEvents = new ArrayList<MNCalendarEvent>();
        todayScheduledEvents = new ArrayList<MNCalendarEvent>();

        tomorrowAlldayEvents = new ArrayList<MNCalendarEvent>();
        tomorrowScheduledEvents = new ArrayList<MNCalendarEvent>();
    }

    public int getSize() {
        int size = todayAlldayEvents.size() + todayScheduledEvents.size() +
                tomorrowAlldayEvents.size() + tomorrowScheduledEvents.size();

        if (tomorrowAlldayEvents.size() > 0 || tomorrowScheduledEvents.size() > 0) {
            return size + 1; // 내일 아이템 표시용
        } else {
            return size;
        }
    }

    public MNCalendarEventItemInfo getCalendarEventItemInfo(int index) {
        MNCalendarEventItemInfo calendarEventItemInfo = new MNCalendarEventItemInfo();

        // 인덱스에 맞게 해당 캘린더 이벤트를 가져옴
        if (tomorrowAlldayEvents.size() == 0 &&
                tomorrowScheduledEvents.size() == 0) {
            if (index < todayAlldayEvents.size()) {
                // 오늘 종일 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                calendarEventItemInfo.convertedIndex = index;
            } else {
                // 오늘 스케쥴 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                calendarEventItemInfo.convertedIndex = index - todayAlldayEvents.size();
            }
        } else {
            if (todayAlldayEvents.size() + todayScheduledEvents.size() > index) {
                // 오늘 이벤트
                if (index < todayAlldayEvents.size()) {
                    // 오늘 종일 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = index;
                } else {
                    // 오늘 스케쥴 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = index - todayAlldayEvents.size();
                }
            } else if (todayAlldayEvents.size() + todayScheduledEvents.size() < index) {
                int todayEventsSize = todayAlldayEvents.size() + todayScheduledEvents.size() + 1; // 1은 내일 표시 아이템
                // 내일 이벤트
                if (index - todayEventsSize < tomorrowAlldayEvents.size()) {
                    // 내일 종일 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = index - todayEventsSize;
                } else {
                    // 내일 스케쥴 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = index - todayEventsSize - tomorrowAlldayEvents.size();
                }
            } else {
                // 내일 표시 아이템
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_INDICATOR;
            }
        }
        return calendarEventItemInfo;
    }
}
