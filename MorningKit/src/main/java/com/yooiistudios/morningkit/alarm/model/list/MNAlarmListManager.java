package com.yooiistudios.morningkit.alarm.model.list;

import android.content.Context;
import android.content.SharedPreferences;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.alarm.model.MNAlarm;
import com.yooiistudios.morningkit.alarm.model.factory.MNAlarmMaker;
import com.yooiistudios.morningkit.common.serialize.ObjectSerializer;
import com.yooiistudios.morningkit.common.sharedpreferences.MNSharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by StevenKim on 2013. 11. 11..
 *
 * MNAlarmListManager
 *  알람 리스트에 관한 모든 것을 관장하는 유틸리티 클래스,
 *  싱글턴으로 알람 리스트를 가지고 있어서 접근 가능
 */
public class MNAlarmListManager {
    private static final String TAG = "MNAlarmListManager";

    /**
     * Singleton
     */
    private volatile static MNAlarmListManager instance;
    private volatile ArrayList<MNAlarm> alarmList;
    private MNAlarmListManager() {}
    public static MNAlarmListManager getInstance() {
        if (instance == null) {
            synchronized (MNAlarmListManager.class) {
                if (instance == null) {
                    instance = new MNAlarmListManager();
                }
            }
        }
        return instance;
    }

    /**
     * get Singleton alarmList
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>
     */
    public static ArrayList<MNAlarm> getAlarmList(Context context) {
        if (MNAlarmListManager.getInstance().alarmList == null) {
            MNAlarmListManager.getInstance().alarmList = loadAlarmList(context);
        }
        return MNAlarmListManager.getInstance().alarmList;
    }
    public static void removeAlarmList(Context context) throws IOException {
        MNAlarmListManager.getInstance().alarmList = null;
        MNAlarmListManager.getInstance().alarmList = newDefaultAlarmList(context);
        saveAlarmList(context);
    }

    /**
     * load alarmList(ArrayList<MNAlarm>) from SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>, and if it's first load, two alarms will be added automatically.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<MNAlarm> loadAlarmList(Context context) {
        try {
            String alarmListDataString = MNSharedPreferences.getAlarmSharedPrefs(context).getString(MN.alarm.ALARM_LIST, null);
            if (alarmListDataString != null) {
                MNAlarmListManager.getInstance().alarmList = (ArrayList<MNAlarm>) ObjectSerializer.deserialize(alarmListDataString);
                if (MNAlarmListManager.getInstance().alarmList == null) {
                    throw new NullPointerException("AlarmList should not be null");
                }
            } else {
                MNAlarmListManager.getInstance().alarmList = newDefaultAlarmList(context);
                if (MNAlarmListManager.getInstance().alarmList != null) {
                    saveAlarmList(context);
                } else {
                    throw new NullPointerException("AlarmList should not be null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MNAlarmListManager.getInstance().alarmList;
    }

    /**
     * make new defulat AlarmList
     * @param context used to get SharedPreferences
     * @return ArrayList<MNAlarm>
     */
    public static ArrayList<MNAlarm> newDefaultAlarmList(Context context) {
        ArrayList<MNAlarm> defaultAlarmList = new ArrayList<MNAlarm>();

        MNAlarm firstAlarm = MNAlarmMaker.makeAlarmWithTime(context, 6, 30);
        MNAlarm secondAlarm = MNAlarmMaker.makeAlarmWithTime(context, 7, 0);

        defaultAlarmList.add(firstAlarm);
        defaultAlarmList.add(secondAlarm);

        return defaultAlarmList;
    }

    /**
     * save alarmList(ArrayList<MNAlarm>) to SharedPreferences using ObjectSerializer.
     * @param context used to get SharedPreferences
     * @throws IOException
     */
    public static void saveAlarmList(Context context) throws IOException {
        SharedPreferences.Editor editor = MNSharedPreferences.getAlarmSharedPrefs(context).edit();
        if (editor != null) {
            if (MNAlarmListManager.getInstance().alarmList != null) {
                editor.putString(MN.alarm.ALARM_LIST, ObjectSerializer.serialize(MNAlarmListManager.getInstance().alarmList));
            } else {
                editor.remove(MN.alarm.ALARM_LIST);
            }
            editor.commit();
        }
    }

    /**
     * add alarm to alarmList(ArrayList<MNAlarm>)
     * @param targetAlarm will be saved to alarmList
     * @param context used to get SharedPreferences
     */
    public static void addAlarmToAlarmList(MNAlarm targetAlarm, Context context) {
        if (targetAlarm == null) {
            throw new IllegalArgumentException("MNAlarm must not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        MNAlarmListManager.getAlarmList(context).add(targetAlarm);
    }

    /**
     * find the same alarm with targetAlarm's alarmId in alarmList, and replace it
     * @param targetAlarm will be used to replace the alarm
     * @param context used to get SharedPreferences
     */
    public static void replaceAlarmToAlarmList(MNAlarm targetAlarm, Context context) {
        if (targetAlarm != null && context != null) {
            int indexOfAlarm = MNAlarmListManager.findIndexOfAlarmById(targetAlarm.getAlarmId(), context);
            if (indexOfAlarm != -1 && indexOfAlarm < MNAlarmListManager.getAlarmList(context).size()) {
                MNAlarmListManager.getAlarmList(context).set(indexOfAlarm, targetAlarm);
            }else{
                throw new IndexOutOfBoundsException("Can't get proper index");
            }
        } else if (targetAlarm == null) {
            throw new IllegalArgumentException("MNAlarm must not be null");
        } else {
            throw new IllegalArgumentException("Context must not be null");
        }
    }

    /**
     * remove the alarm with alarmId from alarmList;
     * @param alarmId will be uesed to find the alarm
     * @param context used to get SharedPreferences
     */
    public static void removeAlarmFromAlarmList(int alarmId, Context context) {
        MNAlarm targetAlarm = MNAlarmListManager.findAlarmById(alarmId, context);
        MNAlarmListManager.getAlarmList(context).remove(targetAlarm);
    }

    /**
     * sort alarmList(ArrayList<MNAlarm>) by time string as ascending
     * @param context used to get SharedPreferences
     */
    public static void sortAlarmList(Context context) {
        ArrayList<MNAlarm> originalAlarmList = MNAlarmListManager.getAlarmList(context);
        ArrayList<MNAlarm> sortedAlarmList = new ArrayList<MNAlarm>(originalAlarmList.size());

        for (MNAlarm alarm : originalAlarmList) {
            sortedAlarmList.add(alarm);
        }

        Comparator<MNAlarm> alarmComparator = new Comparator<MNAlarm>() {
            @Override
            public int compare(MNAlarm lhs, MNAlarm rhs) {
                return MNAlarmComparator.makeComparator(lhs) > MNAlarmComparator.makeComparator(rhs) ? 1 : -1;
            }
        };
        Collections.sort(sortedAlarmList, alarmComparator);
        MNAlarmListManager.getInstance().alarmList = sortedAlarmList;
    }

    /**
     * find alarm by specific alarmId in alarmList
     * @param targetAlarmId alarmId to be searched
     * @param context used to get SharedPreferences
     * @return MNAlarm
     */
    public static MNAlarm findAlarmById(int targetAlarmId, Context context) {
        if (targetAlarmId != -1) {
            MNAlarm targetAlarm = null;
            for (MNAlarm alarm : MNAlarmListManager.getAlarmList(context)) {
                if (alarm.getAlarmId() == targetAlarmId) {
                    targetAlarm = alarm;
                }
            }
            return targetAlarm;
        } else {
            return null;
        }
    }

    /**
     * find index of alarm by specific alarmId in alarmList
     * @param targetAlarmId alarmId to be searched
     * @param context used to get SharedPreferences
     * @return int or -1 if not found
     */
    public static int findIndexOfAlarmById(int targetAlarmId, Context context) {
        if (targetAlarmId != -1) {
            ArrayList<MNAlarm> alarmList = MNAlarmListManager.getAlarmList(context);
            for (int i=0; i<alarmList.size(); i++) {
                MNAlarm alarm = alarmList.get(i);
                if (alarm.getAlarmId() == targetAlarmId) {
                    return i;
                }
            }
        } else {
            throw new IllegalArgumentException("targetAlarmId can't be -1");
        }
        return -1;
    }
}
