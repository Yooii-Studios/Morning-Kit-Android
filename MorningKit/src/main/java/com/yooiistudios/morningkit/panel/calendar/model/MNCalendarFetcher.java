package com.yooiistudios.morningkit.panel.calendar.model;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.yooiistudios.morningkit.common.permission.PermissionUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.provider.CalendarContract.Events.CALENDAR_ID;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarUtils
 *  디바이스 내의 캘린더 ID 들과, 해당 ID의 캘린더 이벤트들을 얻을 수 있는 유틸리티 클래스
 *  -> 리팩토링 후
 */
public class MNCalendarFetcher {
    private MNCalendarFetcher() {
        throw new AssertionError("You MUST not create this class!");
    }

    public static ArrayList<MNCalendar> getCalendarModels(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        // Fetch a list of all calendars synced with the device, their display names and whether the
        // user has them selected for display.
        final Cursor cursor = contentResolver.query(getCalendarURI(false),
                (new String[]{"_id", "displayName", "selected"}), null, null, null);
        // For a full list of available columns see http://tinyurl.com/yfbg76w

        if (cursor != null) {
            ArrayList<MNCalendar> calendarModels = new ArrayList<>();

            while (cursor.moveToNext()) {

                MNCalendar calendarModel = new MNCalendar();

                final String _id = cursor.getString(0);
                final String displayName = cursor.getString(1);

                calendarModel.calendarId = _id;
                calendarModel.displayName = displayName;
                calendarModel.selected = true;
                calendarModels.add(calendarModel);
            }
            cursor.close();
            return calendarModels;
        }
        return null;
    }

    @TargetApi(14)
    public static ArrayList<MNCalendar> getCalendarModels14(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        // Fetch a list of all calendars synced with the device, their display names and whether the
        // user has them selected for display.
        Cursor cursor = null;
        if (PermissionUtils.hasPermission(context, Manifest.permission.READ_CALENDAR)) {
            //noinspection ResourceType
            cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, new String[]
                            { CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                    null, null, null
            ); // 캘린더 이름으로 ASC 정렬이었으나 그냥 id 순서대로 정렬로 변경
            // CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " ASC");
        }

        if (cursor != null) {
            ArrayList<MNCalendar> calendarModels = new ArrayList<>();

            while (cursor.moveToNext()) {
                MNCalendar calendarModel = new MNCalendar();

                final String _id = cursor.getString(0);
                final String displayName = cursor.getString(1);

                calendarModel.calendarId = _id;
                calendarModel.displayName = displayName;
                calendarModel.selected = true;

                calendarModels.add(calendarModel);
            }
            cursor.close();
            return calendarModels;
        }
        return null;
    }

    //for os version android below version 4(ICS)
    public static MNCalendarEventList getCalendarEvents(Context context,
                                                        ArrayList<MNCalendar> calendarModels) {
        // 오늘부터 1년 간의 이벤트를 얻기 - 나중에 현 시간부터 내일까지로 변경 필요
        // 오늘-종일, 오늘-일정 / 내일-종일, 내일-일정 총 4개의 ArrayList 가 필요
        MNCalendarEventList calendarEventList = new MNCalendarEventList();

        // 필요한 시간 기준들을 미리 준비
        DateTime todayNowDateTime = DateTime.now();
        DateTime tomorrowDateTime = DateTime.now().plusDays(1);
        DateTime todayStartDateTime = new DateTime(todayNowDateTime.getYear(), todayNowDateTime.getMonthOfYear(),
                todayNowDateTime.getDayOfMonth(), 0, 0, 0);
        DateTime todayEndDateTime = new DateTime(tomorrowDateTime.getYear(), tomorrowDateTime.getMonthOfYear(),
                tomorrowDateTime.getDayOfMonth(), 0, 0, 0);

        DateTime tomorrowStartDateTime = new DateTime(tomorrowDateTime.getYear(),
                tomorrowDateTime.getMonthOfYear(), tomorrowDateTime.getDayOfMonth(), 0, 0, 0);
        DateTime tomorrowEndDateTime = tomorrowStartDateTime.plusDays(1);

        // 지금부터 내일 0시 0분 0초 미만의 시간(그 중에서도 all-day 와 scheduled 를 분리)
        // 오늘 일정 현재로부터 한 시간 전 까지의 일정도 표시를 해줌
        calendarEventList.todayAlldayEvents = getEventsBetweenDates(context, calendarModels, true,
                todayStartDateTime, todayEndDateTime);
        calendarEventList.todayScheduledEvents = getEventsBetweenDates(context, calendarModels, false,
                todayNowDateTime.minusHours(1), todayEndDateTime);

        // 내일 0시 0분 0초 이상 모레 0시 0분 0초 미만의 일정(그 중에서도 all-day 와 scheduled 를 분리)
        calendarEventList.tomorrowAlldayEvents = getEventsBetweenDates(context, calendarModels, true,
                tomorrowStartDateTime, tomorrowEndDateTime);
        calendarEventList.tomorrowScheduledEvents = getEventsBetweenDates(context, calendarModels, false,
                tomorrowStartDateTime, tomorrowEndDateTime);

        return calendarEventList;
    }

    private static ArrayList<MNCalendarEvent> getEventsBetweenDates(Context context,
                                                                    ArrayList<MNCalendar> calendarModels,
                                                                    boolean isAllDayEvents,
                                                                    DateTime startDateTime,
                                                                    DateTime endDateTime) {
        ContentResolver cr = context.getContentResolver();

        Uri.Builder builder = getInstancesURI().buildUpon();
        ContentUris.appendId(builder, startDateTime.getMillis());
        ContentUris.appendId(builder, endDateTime.getMillis());

        String selection = null;
        // 선택된 캘린더 id를 모두 쿼리문에 삽입
        for (MNCalendar calendarModel : calendarModels) {
            // 선택된 캘린더일 경우에만 로딩해 전체 캘린더에 더하기
            if (calendarModel.selected) {
                if (selection == null) {
                    selection = ("Calendars._id = " + calendarModel.calendarId);
                } else {
                    selection += (" OR Calendars._id = " + calendarModel.calendarId);
                }
            }
        }

        // 해당 시간 안에 있는 events 만 검색할 수 있게 쿼리문에 삽입
        selection = "(" + selection + ") AND ( " +
                startDateTime.getMillis() + " <= " + "begin" + " AND " +
                "begin" + " < " + endDateTime.getMillis() + ")";

        if (isAllDayEvents) {
            selection += " AND " + "allDay = 1";
        } else {
            selection += " AND " + "allDay = 0";
        }

        Cursor eventCursor = cr.query(builder.build(),
                new String[]{"title", "begin"}, selection, null, "begin ASC");

        if (eventCursor != null) {

            ArrayList<MNCalendarEvent> calendarModelList = new ArrayList<>();

            while (eventCursor.moveToNext()) {
                MNCalendarEvent calendarEvent = new MNCalendarEvent();

                // title
                String title;
                if (eventCursor.getString(0) != null) {
                    title = eventCursor.getString(0).trim();
                    calendarEvent.title = title;
                }

                // beginDate
                calendarEvent.beginDate = new Date(eventCursor.getLong(1));

                // all day
                calendarEvent.isAllDayEvent = isAllDayEvents;
                calendarModelList.add(calendarEvent);
            }
            eventCursor.close();
            return calendarModelList;
        } else {
            return null;
        }
    }

    //for os version android version 4(ICS) AND ABOVE
    @TargetApi(14)
    public static MNCalendarEventList getCalendarEvents14(Context context,
                                                          ArrayList<MNCalendar> calendarModels) {
        // 오늘부터 1년 간의 이벤트를 얻기 - 나중에 현 시간부터 내일까지로 변경 필요
        // 오늘-종일, 오늘-일정 / 내일-종일, 내일-일정 총 4개의 ArrayList 가 필요
        MNCalendarEventList calendarEventList = new MNCalendarEventList();

        // 필요한 시간 기준들을 미리 준비
        DateTime todayNowDateTime = DateTime.now();
        DateTime tomorrowDateTime = DateTime.now().plusDays(1);
        DateTime todayStartDateTime = new DateTime(todayNowDateTime.getYear(), todayNowDateTime.getMonthOfYear(),
                todayNowDateTime.getDayOfMonth(), 0, 0, 0);
        DateTime todayEndDateTime = new DateTime(tomorrowDateTime.getYear(), tomorrowDateTime.getMonthOfYear(),
                tomorrowDateTime.getDayOfMonth(), 0, 0, 0);

        DateTime tomorrowStartDateTime = new DateTime(tomorrowDateTime.getYear(),
                tomorrowDateTime.getMonthOfYear(), tomorrowDateTime.getDayOfMonth(), 0, 0, 0);
        DateTime tomorrowEndDateTime = tomorrowStartDateTime.plusDays(1);

        // 지금부터 내일 0시 0분 0초 미만의 시간(그 중에서도 all-day 와 scheduled 를 분리)
        // 현재 시간이 오전 1시 이상이라면, 이전 1시간 까지의 일정도 표시
        calendarEventList.todayAlldayEvents = getEventsBetweenDates14(context, calendarModels, true,
                todayStartDateTime, todayEndDateTime);
        calendarEventList.todayScheduledEvents = getEventsBetweenDates14(context, calendarModels, false,
                todayNowDateTime.minusHours(1), todayEndDateTime);

        // 내일 0시 0분 0초 이상 모레 0시 0분 0초 미만의 일정(그 중에서도 all-day 와 scheduled 를 분리)
        calendarEventList.tomorrowAlldayEvents = getEventsBetweenDates14(context, calendarModels, true,
                tomorrowStartDateTime, tomorrowEndDateTime);
        calendarEventList.tomorrowScheduledEvents = getEventsBetweenDates14(context, calendarModels, false,
                tomorrowStartDateTime, tomorrowEndDateTime);

        return calendarEventList;
    }

    @TargetApi(14)
    private static ArrayList<MNCalendarEvent> getEventsBetweenDates14(Context context,
                                                                      ArrayList<MNCalendar> calendarModels,
                                                                      boolean isAllDayEvents,
                                                                      DateTime startDateTime,
                                                                      DateTime endDateTime) {
        // Instnaces 를 이용해서 받아보자(동기화된 이벤트 정보를 얻기 위해서)
        final String[] INSTANCE_PROJECTION = new String[]{
                CalendarContract.Instances.BEGIN,         // 0
                CalendarContract.Instances.TITLE,         // 1
        };

        Cursor eventCursor;
        ContentResolver contentResolver = context.getContentResolver();

        // The ID of the recurring event whose instances you are searching
        // for in the Instances table
        // 괄호가 중요, 괄호에 따라 제대로 된 값이 안나올 가능성이 있음
        // 아주 새로운 개념. 오늘의 일정을 표시하기 위해서, begin 과 end 가 필요한 것이 아니라,
        // begin 만을 가지고 오늘 예정된 일정이라는 것을 표시 가능! iOS도 수정 필요!
        String selection = null;
        for (MNCalendar calendarModel : calendarModels) {
            // 선택된 캘린더일 경우에만 로딩해 전체 캘린더에 더하기
            if (calendarModel.selected) {
                if (selection == null) {
                    selection = (CALENDAR_ID + " = " + calendarModel.calendarId);
                } else {
                    selection += (" OR " + CALENDAR_ID + " = " + calendarModel.calendarId);
                }
            }
        }

        selection = "(" + selection + ") AND (" +
                startDateTime.getMillis() + " <= " + CalendarContract.Instances.BEGIN + " AND " +
                CalendarContract.Instances.BEGIN + " < " + endDateTime.getMillis() + ")";

        // 종일 이벤트일 경우 쿼리문을 더 추가
        if (isAllDayEvents) {
            selection += " AND " + CalendarContract.Instances.ALL_DAY + " = 1";
        } else {
            selection += " AND " + CalendarContract.Instances.ALL_DAY + " = 0";
        }

        // Construct the query with the desired date range.
        Uri.Builder builder = getInstancesURI().buildUpon();
        ContentUris.appendId(builder, startDateTime.getMillis());
        ContentUris.appendId(builder, endDateTime.getMillis());

        // Submit the query
        eventCursor = contentResolver.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                null,
                CalendarContract.Instances.BEGIN + " ASC");

        if (eventCursor != null) {
            ArrayList<MNCalendarEvent> calendarModelList = new ArrayList<>();

            while (eventCursor.moveToNext()) {

                MNCalendarEvent calendarEvent = new MNCalendarEvent();

                // Get the field values
                long beginTimeInMillis = eventCursor.getLong(0);
                String title = eventCursor.getString(1);

                // Do something with the values.
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(beginTimeInMillis);
//                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                calendarEvent.title = title;
                calendarEvent.isAllDayEvent = isAllDayEvents;
                calendarEvent.beginDate = new Date(beginTimeInMillis);
                calendarModelList.add(calendarEvent);
            }
            eventCursor.close();
            return calendarModelList;
        } else {
            return null;
        }
    }

    private static Uri getCalendarURI(boolean eventUri) {
        Uri calendarURI;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            calendarURI = (eventUri) ? Uri.parse("content://calendar/events") : Uri.parse("content://calendar/calendars");
        } else {
            calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/events") : Uri.parse("content://com.android.calendar/calendars");
        }
        return calendarURI;
    }

    private static Uri getInstancesURI() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return Uri.parse("content://com.android.calendar/instances/when");
        } else {
            return CalendarContract.Instances.CONTENT_URI;
        }
    }
}