package com.yooiistudios.morningkit.panel.calendar.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by StevenKim in GoogleCalendarTestApp from Yooii Studios Co., LTD. on 2014. 4. 3.
 *
 * MNCalendarSelectDialog
 *  달력을 선택하는 다이얼로그
 */
public class MNCalendarSelectDialog {
    private static final String TAG = "MNCalendarSelectDialog";

    public interface MNCalendarSelectDialogListner {
        public void onSelectCalendars(boolean[] selectedArr);
    }

    public static AlertDialog makeDialog(Context context,
                                         final MNCalendarSelectDialogListner calendarSelectListner,
                                         boolean[] selectedCalendarArr) {
        // 모든 캘린더 id를 로딩해서 선택 여부를 결정한다

        // Calendar Ids
        ArrayList<MNCalendar> calendarModels;
        if (Build.VERSION.SDK_INT >= 14) {
            calendarModels = MNCalendarFetcher.getCalendarModel14(context);
        } else {
            calendarModels = MNCalendarFetcher.getCalendarModels(context);
        }

        // Init cursor
        ArrayList<String> calendarNameList = new ArrayList<String>();
        for (MNCalendar calendarModel : calendarModels) {
            calendarNameList.add(calendarModel.displayName);
        }

        // Arraylist to String[]
        final String[] calendarNames = calendarNameList.toArray(new String[calendarNameList.size()]);
        Log.i(TAG, "size: " + calendarNames.length);

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
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "positiveButton");
                for (int i = 0; i < selectedArr.length; i++) {
                    Log.i(TAG, calendarNames[i] + "/selected: " + selectedArr[i]);
                }
                calendarSelectListner.onSelectCalendars(selectedArr);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "negativeButton");
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.i(TAG, "cancelButton");
            }
        }).create();

        alertDialog.setTitle("Select Calendars");
        alertDialog.setCanceledOnTouchOutside(false);

        return alertDialog;
    }
}
