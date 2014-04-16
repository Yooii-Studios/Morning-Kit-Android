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

        MNCalendarEvent calendarModel = null;
        MNCalendarEventItemInfo calendarEventItemInfo = calendarEventList.getCalendarEventItemInfo(i);
        switch (calendarEventItemInfo.calendarEventType) {
            case TODAY_ALL_DAY:
                calendarModel = calendarEventList.todayAlldayEvents.get(calendarEventItemInfo.convertedIndex);
                break;

            case TODAY_SCHEDULED:
                calendarModel = calendarEventList.todayScheduledEvents.get(calendarEventItemInfo.convertedIndex);
                break;

            case TOMORROW_ALL_DAY:
                calendarModel = calendarEventList.tomorrowAlldayEvents.get(calendarEventItemInfo.convertedIndex);
                break;

            case TOMORROW_SCHEDULED:
                calendarModel = calendarEventList.tomorrowScheduledEvents.get(calendarEventItemInfo.convertedIndex);
                break;

            case TOMORROW_INDICATOR:
                break;
        }
        return initEventItem(calendarModel, calendarEventItemInfo, viewGroup);
    }

    protected View initEventItem(MNCalendarEvent calendarModel,
                                 MNCalendarEventItemInfo calendarEventItemInfo, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

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
        } else {
            if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TOMORROW_INDICATOR) {

                // 내일 표시 아이템
                View tomorrowGuideView = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                if (tomorrowGuideView != null) {
                    TextView textView = (TextView) tomorrowGuideView.findViewById(android.R.id.text1);
                    textView.setText("내일");
                }
                return tomorrowGuideView;
            }
        }
        return null;
    }
}
