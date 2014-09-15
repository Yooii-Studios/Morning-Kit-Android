package com.yooiistudios.morningkit.panel.calendar.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import com.yooiistudios.morningkit.R;

import java.util.ArrayList;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarSelectDialog
 *  달력을 선택하는 다이얼로그
 */
public class MNCalendarSelectDialog {
//    private static final String TAG = "MNCalendarSelectDialog";

    public interface MNCalendarSelectDialogListener {
        public void onSelectCalendars(boolean[] selectedArr);
    }

    public static AlertDialog makeDialog(Context context,
                                         final MNCalendarSelectDialogListener calendarSelectListener,
                                         boolean[] selectedCalendarArr) {
        // 모든 캘린더 id를 로딩해서 선택 여부를 결정한다

        // Calendar Ids
        ArrayList<MNCalendar> calendarModels;
        if (Build.VERSION.SDK_INT >= 14) {
            calendarModels = MNCalendarFetcher.getCalendarModels14(context);
        } else {
            calendarModels = MNCalendarFetcher.getCalendarModels(context);
        }

        // Init cursor
        ArrayList<String> calendarNameList = new ArrayList<String>();
        if (calendarModels != null) {
            for (MNCalendar calendarModel : calendarModels) {
                calendarNameList.add(calendarModel.displayName);
            }
        }

        // ArrayList to String[]
        final String[] calendarNames = calendarNameList.toArray(new String[calendarNameList.size()]);

        final boolean[] selectedArr = new boolean[calendarNames.length];

        if (selectedCalendarArr != null && selectedCalendarArr.length == calendarNameList.size()) {
            System.arraycopy(selectedCalendarArr, 0, selectedArr, 0, selectedCalendarArr.length);
        } else {
            for (int i = 0; i < selectedArr.length; i++) {
                selectedArr[i] = true;
            }
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        AlertDialog alertDialog = builder.setMultiChoiceItems(calendarNames, selectedArr, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedArr[which] = isChecked;
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calendarSelectListener.onSelectCalendars(selectedArr);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();

        alertDialog.setTitle(R.string.calendar_select_calendars);
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }
}
