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
}
