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

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarAdapter
 */
public class MNCalendarListAdapter extends BaseAdapter {
    private static final String TAG = "MNCalendarAdapter";
    private Context context;
    private MNCalendarEventList calendarEventList;

    public MNCalendarListAdapter(Context context, boolean[] selectedArr) {
        this.context = context;

        // Init cursor
        calendarEventList = MNCalendarEventUtils.getCalendarEventList(context, selectedArr);
    }

    public MNCalendarListAdapter(MNCalendarEventList calendarEventList) {
        this.calendarEventList = calendarEventList;
    }

    @Override
    public int getCount() {
        if (calendarEventList != null) {
            return calendarEventList.getSize();
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
