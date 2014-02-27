package com.yooiistudios.morningkit.panel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

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
    public static final String PANEL_UNIQUE_ID = "PANEL_UNIQUE_ID";
    public static final String PANEL_WINDOW_INDEX = "PANEL_WINDOW_INDEX";
    public static final String PANEL_DATA_OBJECT = "PANEL_DATA_OBJECT";

    public static final int PANEL_DETAIL_ACTIVITY = 54321;
    public static final String PANEL_CHANGED = "PANEL_CHANGED"; // 패널 교체를 체크하는 키

    private static final String PANEL_SHARED_PREFERENCES = "PANEL_SHARED_PREFERENCES";
    private static final String PANEL_UNIQUE_ID_LIST_KEY = "PANEL_UNIQUE_ID_LIST_KEY";
    private static final String PANEL_DATA_LIST_KEY = "PANEL_DATA_LIST_KEY";

    private volatile static MNPanel instance;
    private List<Integer> panelUniqueIdList; // uniqueId를 저장함으로써 유연성을 가질 수 있는 구조 확립
    private List<JSONObject> panelDataList; // 각 인덱스의 패널 데이터를 저장/아카이빙

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

            String panelDataJsonString = prefs.getString(PANEL_DATA_LIST_KEY, null);
            if (panelDataJsonString != null) {
                Type type = new TypeToken<List<JSONObject>>() {}.getType();
                panelDataList = new Gson().fromJson(panelDataJsonString, type);
            } else {
                panelDataList = new ArrayList<JSONObject>();
                panelDataList.add(new JSONObject());
                panelDataList.add(new JSONObject());
                panelDataList.add(new JSONObject());
                panelDataList.add(new JSONObject());
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

    public static List<JSONObject> getPanelDataList(Context context) {
        return MNPanel.getInstance(context).panelDataList;
    }

    public static void archivePanelData(Context context, JSONObject panelData, int index) {
        // change
        MNPanel.getInstance(context).panelDataList.remove(index);
        MNPanel.getInstance(context).panelDataList.add(index, panelData);

        // Archive
        String jsonString = new Gson().toJson(MNPanel.getInstance(context).panelDataList);
        context.getSharedPreferences(PANEL_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putString(PANEL_DATA_LIST_KEY, jsonString).commit();
    }
}
