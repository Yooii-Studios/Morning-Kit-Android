package com.yooiistudios.morningkit.alarm.model;

import android.content.Context;
import android.util.Log;

import com.yooiistudios.morningkit.R;

import java.util.ArrayList;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmRepeatString
 */
public class MNAlarmRepeatString {
    private static final String TAG = "MNAlarmRepeatString";
    private MNAlarmRepeatString() { throw new AssertionError("You MUST NOT create this class!"); }

    public static String makeRepeatCheckerString(ArrayList<Boolean> repeatList) {
        StringBuilder repeatCheckerBuilder = new StringBuilder();

        for (int i = 0; i < repeatList.size(); i++) {
            boolean repeat = repeatList.get(i);
            if (repeat) {
                repeatCheckerBuilder.append(i);
            }
//            if (repeatList.get(i) == Boolean.FALSE) {
//                Log.i(TAG, "repeatList.get(" + i + ") is Boolean.FALSE");
//            }
        }
        return repeatCheckerBuilder.toString();
    }

    public static String makeRepeatDetailString(ArrayList<Boolean> alarmRepeatList, Context context) {
        String repeatChecker = makeRepeatCheckerString(alarmRepeatList);

        Log.i(TAG, repeatChecker);

        if (repeatChecker.equals("")) {
            return context.getString(R.string.alarm_pref_repeat_never);
        } else if (repeatChecker.equals("0123456")) {
            return context.getString(R.string.alarm_pref_repeat_everyday);
        } else if (repeatChecker.equals("01234")) {
            return context.getString(R.string.alarm_pref_repeat_weekdays);
        } else if (repeatChecker.equals("56")) {
            return context.getString(R.string.alarm_pref_repeat_weekends);
        } else {
            StringBuilder repeatDetailStringBuilder = new StringBuilder();

            for (int j = 0; j < repeatChecker.length(); j++) {
                char dayOfWeek = repeatChecker.charAt(j);
                switch (dayOfWeek) {
                    case '0':
                        repeatDetailStringBuilder.append(context.getString(R.string.monday));
                        break;

                    case '1':
                        repeatDetailStringBuilder.append(context.getString(R.string.tuesday));
                        break;

                    case '2':
                        repeatDetailStringBuilder.append(context.getString(R.string.wednesday));
                        break;

                    case '3':
                        repeatDetailStringBuilder.append(context.getString(R.string.thursday));
                        break;

                    case '4':
                        repeatDetailStringBuilder.append(context.getString(R.string.friday));
                        break;

                    case '5':
                        repeatDetailStringBuilder.append(context.getString(R.string.saturday));
                        break;

                    case '6':
                        repeatDetailStringBuilder.append(context.getString(R.string.sunday));
                        break;

                }
                if (j != repeatChecker.length() - 1) {
                    repeatDetailStringBuilder.append(" ");
                }
            }
            return repeatDetailStringBuilder.toString();
        }
    }
}
