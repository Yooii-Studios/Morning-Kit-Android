package com.yooiistudios.morningkit.setting.theme.alarmstatusbar;

import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNPanelMatrix
 *  패널 매트릭스 설정을 관리
 */
public class MNAlarmStatusBarIcon {
    private static final String ALARM_STATUS_BAR_ICON_SHARED_PREFERENCES = "ALARM_STATUS_BAR_ICON_SHARED_PREFERENCES";
    private static final String ALARM_STATUS_BAR_ICON_KEY = "ALARM_STATUS_BAR_ICON_KEY";

    private volatile static MNAlarmStatusBarIcon instance;
    private MNAlarmStatusBarIconType currentAlarmStatusBarIconType;

    /**
     * Singleton
     */
    private MNAlarmStatusBarIcon(){}
    private MNAlarmStatusBarIcon(Context context) {
        int uniqueId = context.getSharedPreferences(ALARM_STATUS_BAR_ICON_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getInt(ALARM_STATUS_BAR_ICON_KEY, MNAlarmStatusBarIconType.ON.getUniqueId());

        currentAlarmStatusBarIconType = MNAlarmStatusBarIconType.valueOfUniqueId(uniqueId);
    }

    public static MNAlarmStatusBarIcon getInstance(Context context) {
        if (instance == null) {
            synchronized (MNAlarmStatusBarIcon.class) {
                if (instance == null) {
                    instance = new MNAlarmStatusBarIcon(context);
                }
            }
        }
        return instance;
    }

    public static MNAlarmStatusBarIconType getCurrentAlarmStatusBarIconType(Context context) { return MNAlarmStatusBarIcon.getInstance(context).currentAlarmStatusBarIconType; }

    public static void setAlarmStatusBarIconType(MNAlarmStatusBarIconType newAlarmStatusBarIconType, Context context) {
        MNAlarmStatusBarIcon.getInstance(context).currentAlarmStatusBarIconType = newAlarmStatusBarIconType;
        context.getSharedPreferences(ALARM_STATUS_BAR_ICON_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(ALARM_STATUS_BAR_ICON_KEY, newAlarmStatusBarIconType.getUniqueId()).commit();

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.ALARM_STATUS_BAR_ICON_TYPE, newAlarmStatusBarIconType.toString());
        FlurryAgent.logEvent(MNFlurry.ON_SETTING_THEME, params);
    }
}
