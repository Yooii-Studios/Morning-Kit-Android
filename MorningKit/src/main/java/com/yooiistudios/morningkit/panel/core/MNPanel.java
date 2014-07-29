package com.yooiistudios.morningkit.panel.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
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
    private static final String PANEL_DATA_LIST_KEY = "PANEL_DATA_LIST_KEY";

    private volatile static MNPanel instance;
    private List<JSONObject> panelDataList; // 각 인덱스의 패널 데이터를 저장/아카이빙

    /**
     * Singleton
     */
    private MNPanel() {}
    private MNPanel(Context context) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PANEL_SHARED_PREFERENCES, Context.MODE_PRIVATE);

            // 기존에 저장한 패널 데이터 리스트가 있는지 확인
            String panelDataJsonString = prefs.getString(PANEL_DATA_LIST_KEY, null);
            if (panelDataJsonString != null) {
                Type type = new TypeToken<List<JSONObject>>() {}.getType();
                panelDataList = new Gson().fromJson(panelDataJsonString, type);
            } else {
                panelDataList = new ArrayList<JSONObject>();
                try {
                    panelDataList.add(makeDefaultJSONObject(0, MNPanelType.WEATHER.getUniqueId()));
                    panelDataList.add(makeDefaultJSONObject(1, MNPanelType.DATE.getUniqueId()));
                    panelDataList.add(makeDefaultJSONObject(2, MNPanelType.WORLD_CLOCK.getUniqueId()));
                    panelDataList.add(makeDefaultJSONObject(3, MNPanelType.NEWS_FEED.getUniqueId()));
                    panelDataList.add(makeDefaultJSONObject(4, MNPanelType.CALENDAR.getUniqueId()));
                    panelDataList.add(makeDefaultJSONObject(5, MNPanelType.FLICKR.getUniqueId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static JSONObject makeDefaultJSONObject(int panelWindowIndex, int panelUniqueId) throws JSONException {
        JSONObject newJSONObject = new JSONObject();
        newJSONObject.put(MNPanel.PANEL_UNIQUE_ID, panelUniqueId);
        newJSONObject.put(MNPanel.PANEL_WINDOW_INDEX, panelWindowIndex);
        return newJSONObject;
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
    // settingPanelFragment에서 사용
    public static void changeToEmptyDataPanel(Context context, int newPanalUniqueId, int index) {

        // clear panelDataList and set default option
        JSONObject newJSONObject = null;
        try {
            newJSONObject = makeDefaultJSONObject(index, newPanalUniqueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        archivePanelData(context, newJSONObject, index);
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
