package com.yooiistudios.morningkit.setting.theme.panelmatrix;

import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.common.log.MNFlurry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNPanelMatrix
 *  패널 매트릭스 설정을 관리
 */
public class MNPanelMatrix {
    private static final String PANEL_MATRIX_SHARED_PREFERENCES = "PANEL_MATRIX_SHARED_PREFERENCES";
    private static final String PANEL_MATRIX_KEY= "PANEL_MATRIX_KEY";

    private volatile static MNPanelMatrix instance;
    private MNPanelMatrixType currentPanelMatrixType;

    /**
     * Singleton
     */
    private MNPanelMatrix(){}
    private MNPanelMatrix(Context context) {
        int uniqueId = context.getSharedPreferences(PANEL_MATRIX_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getInt(PANEL_MATRIX_KEY, MNPanelMatrixType.PANEL_MATRIX_2X2.getUniqueId());

        currentPanelMatrixType = MNPanelMatrixType.valueOfUniqueId(uniqueId);
    }

    public static MNPanelMatrix getInstance(Context context) {
        if (instance == null) {
            synchronized (MNPanelMatrix.class) {
                if (instance == null) {
                    instance = new MNPanelMatrix(context);
                }
            }
        }
        return instance;
    }

    public static MNPanelMatrixType getCurrentPanelMatrixType(Context context) { return MNPanelMatrix.getInstance(context).currentPanelMatrixType; }

    public static void setPanelMatrixType(MNPanelMatrixType newPanelMatrixType, Context context) {
        MNPanelMatrix.getInstance(context).currentPanelMatrixType = newPanelMatrixType;
        context.getSharedPreferences(PANEL_MATRIX_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putInt(PANEL_MATRIX_KEY, newPanelMatrixType.getUniqueId()).commit();

        // 플러리
        Map<String, String> params = new HashMap<String, String>();
        params.put(MNFlurry.PANEL_MATRIX, newPanelMatrixType.toString());
        FlurryAgent.logEvent(MNFlurry.ON_SETTING_THEME, params);
    }
}
