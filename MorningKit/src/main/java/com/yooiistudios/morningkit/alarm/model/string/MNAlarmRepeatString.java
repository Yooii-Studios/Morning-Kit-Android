package com.yooiistudios.morningkit.alarm.model.string;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.theme.MNMainColors;

import java.util.ArrayList;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 16.
 *
 * MNAlarmRepeatString
 *  반복 값이 있는 ArrrayList<Boolean> -> String
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
        }
        return repeatCheckerBuilder.toString();
    }

    // This is for MNAlarmPreferenceActivity
    public static String makeRepeatDetailString(ArrayList<Boolean> alarmRepeatList, Context context) {
        String repeatChecker = makeRepeatCheckerString(alarmRepeatList);

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

    // This is for MNAlarmListView
    public static SpannableString makeShortRepeatString(ArrayList<Boolean> alarmRepeatList, Context context) {
        String repeatChecker = makeRepeatCheckerString(alarmRepeatList);
        SpannableString shortRepeatSpannableString;

        if (repeatChecker.equals("")) {
            shortRepeatSpannableString = new SpannableString("");
        } else if (repeatChecker.equals("0123456")) {
            shortRepeatSpannableString = new SpannableString("/ " + context.getString(R.string.alarm_pref_repeat_everyday));
            shortRepeatSpannableString.setSpan(new ForegroundColorSpan(MNMainColors.getAlarmMainFontColor()), 0, shortRepeatSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (repeatChecker.equals("01234")) {
            shortRepeatSpannableString = new SpannableString("/ " + context.getString(R.string.alarm_pref_repeat_weekdays));
            shortRepeatSpannableString.setSpan(new ForegroundColorSpan(MNMainColors.getAlarmMainFontColor()), 0, shortRepeatSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (repeatChecker.equals("56")) {
            shortRepeatSpannableString = new SpannableString("/ " + context.getString(R.string.alarm_pref_repeat_weekends));
            shortRepeatSpannableString.setSpan(new ForegroundColorSpan(MNMainColors.getAlarmMainFontColor()), 0, shortRepeatSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            shortRepeatSpannableString =
                    new SpannableString("/ "
                            + context.getString(R.string.monday_short) + " "
                            + context.getString(R.string.tuesday_short) + " "
                            + context.getString(R.string.wednesday_short) + " "
                            + context.getString(R.string.thursday_short) + " "
                            + context.getString(R.string.friday_short) + " "
                            + context.getString(R.string.saturday_short) + " "
                            + context.getString(R.string.sunday_short));

            for (int i = 0; i < alarmRepeatList.size(); i++) {
                // 맨 앞의 "/ "가 2만큼 차지 -> 2, 4, 6, 8 ...
                int index = (i + 1) * 2;
                if (alarmRepeatList.get(i)) {
                    shortRepeatSpannableString.setSpan(new ForegroundColorSpan(MNMainColors.getAlarmMainFontColor()), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    shortRepeatSpannableString.setSpan(new ForegroundColorSpan(MNMainColors.getAlarmSubFontColor()), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return shortRepeatSpannableString;
    }
}
