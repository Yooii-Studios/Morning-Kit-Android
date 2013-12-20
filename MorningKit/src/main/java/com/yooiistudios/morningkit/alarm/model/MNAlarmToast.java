package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.widget.Toast;

import com.yooiistudios.morningkit.R;

import java.util.Calendar;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 20.
 *
 * MNAlarmToast
 *  언제 알람이 울리는지 알려주는 Toast
 */
public class MNAlarmToast {
    private MNAlarmToast() { throw new AssertionError("You MUST NOT create this class"); }

    public static void show(Context context, Calendar calendar) {
        Calendar toastCalendar = (Calendar) calendar.clone();
        toastCalendar.add(Calendar.SECOND, 3);

        Calendar toastNowCalendar = Calendar.getInstance();
        toastNowCalendar.add(Calendar.SECOND, 3);

        long wholeTimeGap = toastCalendar.getTimeInMillis() - toastNowCalendar.getTimeInMillis();
        long dayGap = wholeTimeGap / 1000 / 60 / 60 / 24;
        long hourGap = wholeTimeGap / 1000 / 60 / 60 % 24;
        long minuteGap = wholeTimeGap / 1000 / 60 % 60;

        String toastString = context.getString(R.string.alarm_set_toast_part1);
        if (dayGap != 0) {
            if (dayGap == 1) {
                toastString = toastString + dayGap + context.getString(R.string.alarm_day);
            }else{
                toastString = toastString + dayGap + context.getString(R.string.alarm_days);
            }
        }
        if (hourGap != 0) {
            if (hourGap == 1) {
                toastString = toastString + hourGap + context.getString(R.string.alarm_hour);
            }else{
                toastString = toastString + hourGap + context.getString(R.string.alarm_hours);
            }
        }
        if (minuteGap != 0) {
//            if (GeneralSetting.getLanguageName().equals("English") ) {
//                // 앞에 뭐가 있으면 and 붙임
//                if (dayGap != 0 || hourGap != 0) {
//                    toastString += "and ";
//                }
//            }

            // 분만 있으면 and 안붙임
            if (minuteGap == 1) {
                toastString = toastString + minuteGap + context.getString(R.string.alarm_minute);
            }else{
                toastString = toastString + minuteGap + context.getString(R.string.alarm_minute);
            }
            toastString += context.getString(R.string.alarm_set_toast_part2);
        }else{
            toastString = context.getString(R.string.alarm_set_for_less_than_1_minute);
        }
        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
    }
}
