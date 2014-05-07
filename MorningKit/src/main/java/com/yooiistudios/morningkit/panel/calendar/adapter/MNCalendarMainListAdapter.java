package com.yooiistudios.morningkit.panel.calendar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEvent;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventItemInfo;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventList;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventType;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

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
    protected View initEventItem(int position, MNCalendarEvent calendarModel,
                                 MNCalendarEventItemInfo calendarEventItemInfo, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        View convertView = null;

        if (calendarModel != null) {
            convertView = inflater.inflate(R.layout.panel_calendar_event_item, viewGroup, false);
            if (convertView != null) {

                RelativeLayout itemLayout = (RelativeLayout) convertView
                        .findViewById(R.id.panel_calendar_event_item_layout);

                // Time
                TextView timeTextView = (TextView) convertView
                        .findViewById(R.id.panel_calendar_event_item_time_textview);
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
                TextView titleTextView = (TextView) convertView
                        .findViewById(R.id.panel_calendar_event_item_title_textview);
                titleTextView.setText(calendarModel.title);

                if (!MNPanelLayout.DEBUG_UI) {
                    itemLayout.setBackgroundColor(Color.TRANSPARENT);
                    timeTextView.setBackgroundColor(Color.TRANSPARENT);
                    titleTextView.setBackgroundColor(Color.TRANSPARENT);

                    switch (calendarEventItemInfo.calendarEventType) {
                        case TODAY_ALL_DAY:
                        case TODAY_SCHEDULED:
                            timeTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, context));
                            titleTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, context));
                            break;

                        case TOMORROW_ALL_DAY:
                        case TOMORROW_SCHEDULED:
                            timeTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            titleTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            break;
                    }
                }
            }
        } else {
            if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TOMORROW_INDICATOR) {

                // 내일 표시 아이템
                convertView = inflater.inflate(R.layout.panel_calendar_event_indicator_item,
                        viewGroup, false);

                if (convertView != null) {
                    if (!MNPanelLayout.DEBUG_UI) {
                        RelativeLayout itemLayout = (RelativeLayout) convertView
                                .findViewById(R.id.panel_calendar_event_indicator_item_layout);

                        TextView timeTextView = (TextView) convertView
                                .findViewById(R.id.panel_calendar_event_indicator_item_time_textview);

                        itemLayout.setBackgroundColor(Color.TRANSPARENT);
                        timeTextView.setBackgroundColor(Color.TRANSPARENT);
                        timeTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                    }
                }
            }
        }
        // Divider
        View dividerView = (View) convertView.
                findViewById(R.id.panel_calendar_event_item_divider);

        if (!MNPanelLayout.DEBUG_UI) {
            if (position == calendarEventList.getSize() - 1) {
                dividerView.setVisibility(View.INVISIBLE);
            } else {
                dividerView.setVisibility(View.VISIBLE);
                dividerView.setBackgroundColor(MNMainColors.getSubFontColor(currentThemeType, context));
            }
        }
        return convertView;
    }
}
