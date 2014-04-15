package com.yooiistudios.morningkit.panel.calendar.model;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarAdapter
 */
public class MNCalendarListAdapter extends BaseAdapter {
    private static final String TAG = "MNCalendarAdapter";
    private Context context;
    private ArrayList<MNCalendar> calendarModels;
    private ArrayList<MNCalendarEvent> calendarEvents;
    private MNCalendarEventList calendarEventList;

    public MNCalendarListAdapter(Context context) {
        this.context = context;

        initCalendarModels();
        initEventsCursor();
    }

    public MNCalendarListAdapter(Context context, boolean[] selectedArr) {
        this.context = context;

        initCalendarModels();
        for (int i = 0; i < calendarModels.size(); i++) {
            MNCalendar calendarModel = calendarModels.get(i);
            // 저장된 정보와 캘린더 숫자가 변할 가능성을 염두에 두고 방어적 코드 삽입
            if (selectedArr != null && i < selectedArr.length) {
                calendarModel.selected = selectedArr[i];
            }
        }
        initEventsCursor();
    }

    private void initCalendarModels() {
        // Calendar Ids
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            calendarModels = MNCalendarFetcher.getCalendarModel14(context);
        } else {
            calendarModels = MNCalendarFetcher.getCalendarModels(context);
        }
    }

    private void initEventsCursor() {
        // Init cursor
        calendarEventList = new MNCalendarEventList();
        for (MNCalendar calendarModel : calendarModels) {
            // 선택된 캘린더일 경우에만 로딩해 전체 캘린더에 더하기
            if (calendarModel.selected) {
                MNCalendarEventList selectedCalendarEventList;
                if (android.os.Build.VERSION.SDK_INT >= 14) {
                    selectedCalendarEventList = MNCalendarFetcher.getCalendarEvents14(context, calendarModel.calendarId);
                } else {
                    selectedCalendarEventList = MNCalendarFetcher.getCalendarEvents(context, calendarModel.calendarId);
                }
                if (selectedCalendarEventList != null) {
                    calendarEventList.todayAlldayEvents.addAll(
                            selectedCalendarEventList.todayAlldayEvents);
                    calendarEventList.todayScheduledEvents.addAll(
                            selectedCalendarEventList.todayScheduledEvents);
                    calendarEventList.tomorrowAlldayEvents.addAll(
                            selectedCalendarEventList.tomorrowAlldayEvents);
                    calendarEventList.tomorrowScheduledEvents.addAll(
                            selectedCalendarEventList.tomorrowScheduledEvents);
                }
            }
        }
        // 마지막으로 소팅(비종일 일정만)
        MNCalendarUtils.sort(calendarEventList.todayScheduledEvents);
        MNCalendarUtils.sort(calendarEventList.tomorrowScheduledEvents);
    }

    @Override
    public int getCount() {
        if (calendarEventList != null) {
            int size = calendarEventList.todayAlldayEvents.size() +
                    calendarEventList.todayScheduledEvents.size() +
                    calendarEventList.tomorrowAlldayEvents.size() +
                    calendarEventList.tomorrowScheduledEvents.size();

            if (calendarEventList.tomorrowAlldayEvents.size() > 0 ||
                    calendarEventList.tomorrowScheduledEvents.size() > 0) {
                return size + 1; // 내일 아이템 표시용
            } else {
                return size;
            }
        }
        return 0;
//        return calendarEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        MNCalendarEvent calendarModel;

        // 인덱스에 맞게 해당 캘린더 이벤트를 가져옴
        if (calendarEventList.tomorrowAlldayEvents.size() == 0 &&
                calendarEventList.tomorrowScheduledEvents.size() == 0) {
            if (i < calendarEventList.todayAlldayEvents.size()) {
                // 오늘 종일 이벤트
                calendarModel = calendarEventList.todayAlldayEvents.get(i);
            } else {
                // 오늘 스케쥴 이벤트
                calendarModel = calendarEventList.todayScheduledEvents.get(i);
            }
        } else {
            if (calendarEventList.todayAlldayEvents.size() +
                    calendarEventList.todayScheduledEvents.size() > i) {
                // 오늘 이벤트
                if (i < calendarEventList.todayAlldayEvents.size()) {
                    // 오늘 종일 이벤트
                    calendarModel = calendarEventList.todayAlldayEvents.get(i);
                } else {
                    // 오늘 스케쥴 이벤트
                    calendarModel = calendarEventList.todayScheduledEvents.get(i - calendarEventList.todayAlldayEvents.size());
                }
            } else if (calendarEventList.todayAlldayEvents.size() +
                    calendarEventList.todayScheduledEvents.size() < i) {
                // 내일 이벤트
                int todayEventsSize = calendarEventList.todayAlldayEvents.size() +
                        calendarEventList.todayScheduledEvents.size() + 1; // 1은 내일 표시 아이템 갯수
                if (i - todayEventsSize < calendarEventList.tomorrowAlldayEvents.size()) {
                    // 내일 종일 이벤트
                    calendarModel = calendarEventList.tomorrowAlldayEvents.get(i - todayEventsSize);
                } else {
                    // 내일 스케쥴 이벤트
                    calendarModel = calendarEventList.tomorrowScheduledEvents.get(i - todayEventsSize - calendarEventList.tomorrowAlldayEvents.size());
                }
            } else {
                // 내일 표시 아이템
                View tomorrowGuideView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                if (tomorrowGuideView != null) {
                    TextView textView = (TextView) tomorrowGuideView.findViewById(android.R.id.text1);
                    textView.setText("내일");
                }
                return tomorrowGuideView;
            }
        }

        if (calendarModel != null) {
            View convertView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            if (convertView != null) {

                SimpleDateFormat sdfrr;
                if (DateFormat.is24HourFormat(context)) {
                    sdfrr = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                } else {
                    sdfrr = new SimpleDateFormat("yyyy/MM/dd a hh:mm");
                }

                String stimesr = sdfrr.format(calendarModel.beginDate);

                TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
                textView.setText(stimesr + "   " + calendarModel.title);

                return convertView;
            }
        }
        return null;
    }
}
