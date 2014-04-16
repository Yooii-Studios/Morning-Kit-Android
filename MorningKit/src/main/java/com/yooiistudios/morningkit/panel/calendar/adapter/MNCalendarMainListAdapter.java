package com.yooiistudios.morningkit.panel.calendar.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEvent;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventItemInfo;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventList;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventType;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventUtils;

import java.text.SimpleDateFormat;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarMainListAdapter
 *  메인 패널에서 사용되는 어댑터
 */
public class MNCalendarMainListAdapter extends MNCalendarListAdapter {

    public MNCalendarMainListAdapter(Context context, boolean[] selectedArr) {
        super(context, selectedArr);
    }

    public MNCalendarMainListAdapter(MNCalendarEventList calendarEventList) {
        super(calendarEventList);
    }

    @Override
    protected View initEventItem(MNCalendarEvent calendarModel,
                                 MNCalendarEventItemInfo calendarEventItemInfo, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (calendarModel != null) {
            View convertView = inflater.inflate(R.layout.panel_calendar_event_item, viewGroup, false);
            if (convertView != null) {

                TextView timeTextView = (TextView) convertView.findViewById(R.id.panel_calendar_event_item_time_textview);

                // Time
                if (calendarModel.isAllDayEvent) {
                    if (calendarEventItemInfo.convertedIndex == 0) {
                        timeTextView.setText(R.string.reminder_all_day);
                    } else {
                        timeTextView.setText("");
                    }
                } else {
                    SimpleDateFormat simpleDateFormat;
                    if (DateFormat.is24HourFormat(context)) {
                        simpleDateFormat = new SimpleDateFormat("HH:mm");
                    } else {
                        simpleDateFormat = new SimpleDateFormat("a hh:mm");
                    }
                    timeTextView.setText(simpleDateFormat.format(calendarModel.beginDate));
                }

                // Title
                TextView titleTextView = (TextView) convertView.findViewById(R.id.panel_calendar_event_item_title_textview);
                titleTextView.setText(calendarModel.title);
                return convertView;
            }
        } else {
            if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TOMORROW_INDICATOR) {

                // 내일 표시 아이템
                View tomorrowGuideView = inflater.inflate(R.layout.panel_calendar_event_indicator_item,
                        viewGroup, false);
                if (tomorrowGuideView != null) {
                    return tomorrowGuideView;
                }
                return null;
            }
        }
        return null;
    }
}
