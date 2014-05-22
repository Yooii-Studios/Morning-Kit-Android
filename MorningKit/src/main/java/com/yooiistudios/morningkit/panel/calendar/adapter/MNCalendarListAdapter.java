package com.yooiistudios.morningkit.panel.calendar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEvent;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventItemInfo;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventList;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventType;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarEventUtils;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import java.text.SimpleDateFormat;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarAdapter
 *  디테일에서 사용될 어댑터. 기본 로직을 포함. 이것을 상속해서 메인에서 사용할 예정
 */
public class MNCalendarListAdapter extends BaseAdapter {
    protected static final String TAG = "MNCalendarAdapter";
    protected Context context;
    protected MNCalendarEventList calendarEventList;

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
        } else {
            return 0;
        }
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
        View convertView = initEventItem(i, calendarModel, calendarEventItemInfo, viewGroup);

        // divider를 index 위치에 따라 INVISIBLE 처리
        View dividerView = convertView.findViewById(R.id.panel_calendar_event_item_divider);
        switch (calendarEventItemInfo.calendarEventType) {
            case TODAY_ALL_DAY:
                if (calendarEventItemInfo.convertedIndex + 1 !=
                        calendarEventList.todayAlldayEvents.size()) {
                    dividerView.setVisibility(View.INVISIBLE);
                }
                break;

            case TOMORROW_ALL_DAY:
                if (calendarEventItemInfo.convertedIndex + 1 !=
                        calendarEventList.tomorrowAlldayEvents.size()) {
                    dividerView.setVisibility(View.INVISIBLE);
                }
                break;

            case TOMORROW_SCHEDULED:
                if (calendarEventItemInfo.convertedIndex + 1 ==
                        calendarEventList.tomorrowScheduledEvents.size()) {
                    dividerView.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return convertView;
    }

    protected View initEventItem(int position, MNCalendarEvent calendarModel,
                                 MNCalendarEventItemInfo calendarEventItemInfo, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
        View convertView = null;

        if (calendarModel != null) {
            convertView = inflater.inflate(R.layout.panel_calendar_detail_event_item,
                    viewGroup, false);
            if (convertView != null) {

                RelativeLayout itemLayout = (RelativeLayout) convertView
                        .findViewById(R.id.panel_calendar_detail_event_item_layout);

                TextView timeTextView = (TextView) convertView
                        .findViewById(R.id.panel_calendar_detail_event_item_time_textview);

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
                TextView titleTextView = (TextView) convertView
                        .findViewById(R.id.panel_calendar_detail_event_item_title_textview);
                titleTextView.setText(calendarModel.title);

                if (MNPanelLayout.DEBUG_UI) {
                    itemLayout.setBackgroundColor(Color.MAGENTA);
                    timeTextView.setBackgroundColor(Color.BLUE);
                    titleTextView.setBackgroundColor(Color.RED);
                    timeTextView.setTextColor(Color.YELLOW);
                    titleTextView.setTextColor(Color.GREEN);
                }
            }
        } else {
            if (calendarEventItemInfo.calendarEventType == MNCalendarEventType.TOMORROW_INDICATOR) {
                // 내일 표시 아이템
                convertView = inflater.inflate(R.layout.panel_calendar_detail_event_indicator_item,
                        viewGroup, false);

                if (convertView != null) {
                    if (MNPanelLayout.DEBUG_UI) {
                        RelativeLayout itemLayout = (RelativeLayout) convertView
                                .findViewById(R.id.panel_calendar_detail_event_indicator_item_layout);

                        TextView timeTextView = (TextView) convertView
                                .findViewById(R.id.panel_calendar_detail_event_indicator_item_time_textview);

                        itemLayout.setBackgroundColor(Color.RED);
                        timeTextView.setBackgroundColor(Color.GREEN);
                        timeTextView.setTextColor(Color.MAGENTA);
                    }
                }
            }
        }

        if (convertView != null) {
            View dividerView = convertView.findViewById(R.id.panel_calendar_event_item_divider);
            if (!MNPanelLayout.DEBUG_UI) {
                if (position == calendarEventList.getSize() - 1) {
                    dividerView.setVisibility(View.INVISIBLE);
                } else {
                    dividerView.setVisibility(View.VISIBLE);
                    dividerView.setBackgroundColor(MNMainColors.getSubFontColor(currentThemeType, context));
                }
            }
        }
        return convertView;
    }
}
