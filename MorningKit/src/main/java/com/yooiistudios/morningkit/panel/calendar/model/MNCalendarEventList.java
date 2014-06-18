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

    public int getSize(boolean isLoadedFromMain) {
        int size = todayAlldayEvents.size() + todayScheduledEvents.size() +
                tomorrowAlldayEvents.size() + tomorrowScheduledEvents.size();

        // 오늘 일정과 내일 일정이 있다면 indicator 때문에 갯수를 하나씩 추가해줌
        if (todayAlldayEvents.size() > 0 || todayScheduledEvents.size() > 0) {
            size++;
        }
        if (tomorrowAlldayEvents.size() > 0 || tomorrowScheduledEvents.size() > 0) {
            size++;
        }
//        MNLog.now("MNCalendarEventList.size: " + size);
        return size;

        /*
        if (isLoadedFromMain) {
            return size;
        } else {
            if (size == 0) {
                return 0;
            } else {
                return size + 1;
            }
        }
        */

        /*
        if (tomorrowAlldayEvents.size() > 0 || tomorrowScheduledEvents.size() > 0) {
            return size + 1; // 내일 아이템 표시용
        } else {
            if (isLoadedFromMain) {
                return size;
            } else {
                if (size == 0) {
                    return 0;
                } else {
                    return size + 1;
                }
            }
        }
        */
    }

    public MNCalendarEventItemInfo getCalendarEventItemInfo(int index, boolean isLoadedFromMain) {
        MNCalendarEventItemInfo calendarEventItemInfo = new MNCalendarEventItemInfo();

        // 디테일 이벤트리스트인 경우 최상단을 인디케이터로 사용
        /*
        int convertedItemIndex = index;
        if (!isLoadedFromMain) {
            convertedItemIndex -= 1;

            if (index == 0) {
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_INDICATOR;
                calendarEventItemInfo.convertedIndex = 0;
                return calendarEventItemInfo;
            }
        }
        */
        int convertedItemIndex = index;

        // 인덱스에 맞게 해당 캘린더 이벤트를 가져옴
        int todayAllDayEventSize = todayAlldayEvents.size();
        int todayScheduledEventSize = todayScheduledEvents.size();
        int tomorrowAllDayEventSize = tomorrowAlldayEvents.size();
        int tomorrowScheduledEventSize = tomorrowScheduledEvents.size();

        if (tomorrowAllDayEventSize == 0 && tomorrowScheduledEventSize == 0 &&
                todayAllDayEventSize + todayScheduledEventSize > 0) {
            if (index == 0) {
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_INDICATOR;
            } else if (index < todayAllDayEventSize + 1) {
                // 오늘 종일 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                calendarEventItemInfo.convertedIndex = index - 1;
            } else {
                // 오늘 스케쥴 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                calendarEventItemInfo.convertedIndex = index - todayAllDayEventSize - 1;
            }

            /*
            // 내일 일정이 없고 오늘 일정만 있다면 오늘 인디케이터만 빼면 됨
            if (convertedItemIndex < todayAlldayEventSize) {
                // 오늘 종일 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                calendarEventItemInfo.convertedIndex = convertedItemIndex;
            } else {
                // 오늘 스케쥴 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                calendarEventItemInfo.convertedIndex = convertedItemIndex - todayAlldayEventSize;
            }
            */
        } else if (todayAllDayEventSize == 0 && todayScheduledEventSize == 0 &&
                tomorrowAllDayEventSize + tomorrowScheduledEventSize > 0) {
            // 오늘 일정이 없고 내일 일정만 있는 경우
            if (index == 0) {
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_INDICATOR;
            } else if (index < tomorrowAllDayEventSize + 1) {
                // 내일 종일 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_ALL_DAY;
                calendarEventItemInfo.convertedIndex = index - 1;
            } else {
                // 내일 스케쥴 이벤트
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_SCHEDULED;
                calendarEventItemInfo.convertedIndex = index - tomorrowAllDayEventSize - 1;
            }
        } else {
            // 오늘 일정과 내일 일정이 모두 포함된 경우(제일 계산이 어려움)
            // 오늘/내일 일정이 둘 다 없을 순 없다.(0을 반환해서 이 메서드가 호출될 일이 없음. 따라서 하나씩은 있다고 가정
            // 전체 사이즈: 인디케이터 + 오늘 모든 일정 + 인디케이터 + 내일 모든 일정
            int allTodayEventItems = todayAllDayEventSize + todayScheduledEventSize + 1;
            if (convertedItemIndex < allTodayEventItems) {
                // 오늘 아이템들
                if (convertedItemIndex == 0) {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_INDICATOR;
//                    calendarEventItemInfo.convertedIndex = 0;
                } else if (convertedItemIndex < todayAllDayEventSize + 1) {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - 1;
                } else {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - todayAllDayEventSize - 1;
                }
            } else {
                // 내일 아이템들
                // 오늘 아이템들 갯수를 빼주자
                convertedItemIndex -= allTodayEventItems;
                if (convertedItemIndex == 0) {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_INDICATOR;
//                    calendarEventItemInfo.convertedIndex = 0;
                } else if (convertedItemIndex < tomorrowAllDayEventSize + 1) {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - 1;
                } else {
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - tomorrowAllDayEventSize - 1;
                }
            }

            /*
            if (todayAlldayEvents.size() + todayScheduledEvents.size() > convertedItemIndex) {
                // 오늘 이벤트
                if (convertedItemIndex < todayAlldayEvents.size()) {
                    // 오늘 종일 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex;
                } else {
                    // 오늘 스케쥴 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TODAY_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - todayAlldayEvents.size();
                }
            } else if (todayAlldayEvents.size() + todayScheduledEvents.size() < convertedItemIndex) {
                int todayEventsSize = todayAlldayEvents.size() + todayScheduledEvents.size() + 1; // 1은 내일 표시 아이템
                // 내일 이벤트
                if (convertedItemIndex - todayEventsSize < tomorrowAlldayEvents.size()) {
                    // 내일 종일 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_ALL_DAY;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - todayEventsSize;
                } else {
                    // 내일 스케쥴 이벤트
                    calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_SCHEDULED;
                    calendarEventItemInfo.convertedIndex = convertedItemIndex - todayEventsSize - tomorrowAlldayEvents.size();
                }
            } else {
                // 내일 표시 아이템
                calendarEventItemInfo.calendarEventType = MNCalendarEventType.TOMORROW_INDICATOR;
            }
            */
        }
        return calendarEventItemInfo;
    }
}
