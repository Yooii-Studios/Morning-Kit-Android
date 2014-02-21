package com.yooiistudios.morningkit.panel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 6.
 *
 * MNPanelManager
 *  어느 위치에 어떤 패널이 선택되었는지 저장하는 유틸리티 클래스
 */
public class MNPanel {
    // JSONObject panelDataObject에 사용될 키워드
    public static final String PANEL_UNIQUE_ID = "panelUniqueId";
    public static final String PANEL_DATA_OBJECT = "panelDataObject";

    private static final String PANEL_SHARED_PREFERENCES = "PANEL_SHARED_PREFERENCES";
    private static final String PANEL_UNIQUE_ID_LIST_KEY = "PANEL_UNIQUE_ID_LIST_KEY";

    private volatile static MNPanel instance;
    private List<Integer> panelUniqueIdList; // uniqueId를 저장함으로써 유연성을 가질 수 있는 구조 확립

    /**
     * Singleton
     */
    private MNPanel() {}
    private MNPanel(Context context) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PANEL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String panelListJsonString = prefs.getString(PANEL_UNIQUE_ID_LIST_KEY, null);

            if (panelListJsonString != null) {
                Type type = new TypeToken<List<Integer>>(){}.getType();
                panelUniqueIdList = new Gson().fromJson(panelListJsonString, type);
            } else {
                panelUniqueIdList = new ArrayList<Integer>();
                panelUniqueIdList.add(MNPanelType.WEATHER.getUniqueId());
                panelUniqueIdList.add(MNPanelType.DATE.getUniqueId());
                panelUniqueIdList.add(MNPanelType.WORLD_CLOCK.getUniqueId());
                panelUniqueIdList.add(MNPanelType.QUOTES.getUniqueId());

                String jsonString = new Gson().toJson(panelUniqueIdList);
                prefs.edit().putString(PANEL_UNIQUE_ID_LIST_KEY, jsonString).commit();
            }
        }
    }

    public static MNPanel getInstance(Context context) {
        if (instance == null) {
            synchronized (MNPanel.class) {
                if (instance == null) {
                    instance = new MNPanel(context);
                }
            }
        }
        return instance;
    }

    /**
     * Utility Methods
     */
    public static List<Integer> getPanelUniqueIdList(Context context) {
        return MNPanel.getInstance(context).panelUniqueIdList;
    }

    public static void changePanel(Context context, int newPanalUniqueId, int index) {
        // change
        MNPanel.getInstance(context).panelUniqueIdList.remove(index);
        MNPanel.getInstance(context).panelUniqueIdList.add(index, newPanalUniqueId);

        // Archive
        String jsonString = new Gson().toJson(MNPanel.getInstance(context).panelUniqueIdList);
        context.getSharedPreferences(PANEL_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putString(PANEL_UNIQUE_ID_LIST_KEY, jsonString).commit();
    }
}
