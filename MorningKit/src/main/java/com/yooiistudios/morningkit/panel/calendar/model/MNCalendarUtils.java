package com.yooiistudios.morningkit.panel.calendar.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 4.
 *
 * MNCalendarUtils
 *  캘린더 선택 옵션을 저장하고 로드하는 클래스
 */
public class MNCalendarUtils {
    private static final String TAG = "MNCalendarUtils";
    private MNCalendarUtils() { throw new AssertionError("You MUST not create this class!"); }
    private static final String PREFS = "MNCalendarUtils_PREFS";
    private static final String PREFS_KEY = "MNCalendarUtils_PREFS_PREFS_KEY";

    public static boolean[] loadCalendarModels(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String calendarModelsJsonString = prefs.getString(PREFS_KEY, null);
        if (calendarModelsJsonString != null) {
            Type type = new TypeToken<boolean[]>(){}.getType();
            return new Gson().fromJson(calendarModelsJsonString, type);
        }
        return null;
    }

    public static void saveCalendarModels(boolean[] selectedCalendarArr, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(PREFS_KEY, new Gson().toJson(selectedCalendarArr)).apply();
    }

    public static void sort(ArrayList<MNCalendarEvent> calendarEventList) {
        Collections.sort(calendarEventList, new Comparator<MNCalendarEvent>() {
            @Override
            public int compare(MNCalendarEvent calendarEvent1, MNCalendarEvent calendarEvent2) {
                return calendarEvent1.beginDate.getTime() < calendarEvent2.beginDate.getTime() ? -1
                        : calendarEvent1.beginDate.getTime() > calendarEvent2.beginDate.getTime() ? 1 : 0;
            }
        });
    }
}
