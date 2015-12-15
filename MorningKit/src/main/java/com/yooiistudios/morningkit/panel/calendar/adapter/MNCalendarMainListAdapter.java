package com.yooiistudios.morningkit.panel.calendar.adapter;

import android.annotation.SuppressLint;
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

    @SuppressWarnings("unused")
    public MNCalendarMainListAdapter(MNCalendarEventList calendarEventList) {
        super(calendarEventList);
    }

    @SuppressLint("SimpleDateFormat")
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
                        simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    }
                    timeTextView.setText(simpleDateFormat.format(calendarModel.beginDate));
                }

                // Title
                TextView titleTextView = (TextView) convertView
                        .findViewById(R.id.panel_calendar_event_item_title_textview);
                titleTextView.setText(calendarModel.title);

                View dividerView = convertView.findViewById(R.id.panel_calendar_event_item_divider);

                if (!MNPanelLayout.DEBUG_UI) {
                    itemLayout.setBackgroundColor(Color.TRANSPARENT);
                    timeTextView.setBackgroundColor(Color.TRANSPARENT);
                    titleTextView.setBackgroundColor(Color.TRANSPARENT);

                    switch (calendarEventItemInfo.calendarEventType) {
                        case TODAY_ALL_DAY:
                        case TODAY_SCHEDULED:
                            timeTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            titleTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            break;

                        case TOMORROW_ALL_DAY:
                        case TOMORROW_SCHEDULED:
                            timeTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            titleTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, context));
                            break;
                    }

                    // 제일 마지막 아이템 Divider 숨기기
                    if (position == calendarEventList.getSize() - 1) {
                        dividerView.setVisibility(View.GONE);
                    } else {
                        dividerView.setVisibility(View.VISIBLE);
                        dividerView.setBackgroundColor(
                                MNMainColors.getCalendarItemDividerColor(currentThemeType, context));
                    }
                } else {
                    itemLayout.setBackgroundColor(Color.MAGENTA);
                    timeTextView.setBackgroundColor(Color.BLUE);
                    titleTextView.setBackgroundColor(Color.YELLOW);
                    dividerView.setBackgroundColor(Color.CYAN);
                }
            }
        } else {
            if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TODAY_INDICATOR) {
                // 오늘 표시 아이템
                convertView = inflater.inflate(R.layout.panel_calendar_event_indicator_item,
                        viewGroup, false);

                if (convertView != null) {
                    RelativeLayout itemLayout = (RelativeLayout) convertView
                            .findViewById(R.id.panel_calendar_event_indicator_item_layout);

                    TextView textView = (TextView) convertView
                            .findViewById(R.id.panel_calendar_event_indicator_item_time_textview);
                    textView.setText(R.string.world_clock_today);

                    View dividerView = convertView.findViewById(R.id.panel_calendar_event_item_divider);

                    if (!MNPanelLayout.DEBUG_UI) {
                        itemLayout.setBackgroundColor(Color.TRANSPARENT);
                        textView.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, context));
                        // Divider
                        dividerView.setBackgroundColor(
                                MNMainColors.getCalendarDividerItemDividerColor(currentThemeType, context));
                    } else {
                        itemLayout.setBackgroundColor(Color.RED);
                        textView.setBackgroundColor(Color.GREEN);
                        textView.setTextColor(Color.MAGENTA);
                    }
                }
            } else if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TOMORROW_INDICATOR) {
                // 내일 표시 아이템
                convertView = inflater.inflate(R.layout.panel_calendar_event_indicator_item,
                        viewGroup, false);

                if (convertView != null) {

                    RelativeLayout itemLayout = (RelativeLayout) convertView
                            .findViewById(R.id.panel_calendar_event_indicator_item_layout);

                    TextView timeTextView = (TextView) convertView
                            .findViewById(R.id.panel_calendar_event_indicator_item_time_textview);
                    View dividerView = convertView.findViewById(R.id.panel_calendar_event_item_divider);

                    if (!MNPanelLayout.DEBUG_UI) {
                        itemLayout.setBackgroundColor(Color.TRANSPARENT);
                        timeTextView.setBackgroundColor(Color.TRANSPARENT);
                        timeTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, context));
                        // Divider
                        dividerView.setBackgroundColor(
                                MNMainColors.getCalendarDividerItemDividerColor(currentThemeType, context));
                    } else {
                        itemLayout.setBackgroundColor(Color.MAGENTA);
                        timeTextView.setBackgroundColor(Color.BLUE);
                        timeTextView.setTextColor(Color.YELLOW);
                        dividerView.setBackgroundColor(Color.CYAN);
                    }
                }
            }
        }
        return convertView;
    }
}
