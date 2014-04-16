package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanel;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.core.MNPanelLayoutFactory;
import com.yooiistudios.morningkit.panel.core.MNPanelType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import lombok.Getter;

/**
 * Created by yongbinbae on 13. 10. 22..
 * MNWidgetWindowLayout
 */
public class MNPanelWindowLayout extends LinearLayout
{
    private static final String TAG = "MNWidgetWindowLayout";

    @Getter private LinearLayout panelLineLayouts[];
    @Getter private MNPanelLayout panelLayouts[];
//    @Getter private FrameLayout[][] widgetSlots;

    public MNPanelWindowLayout(Context context)
    {
        super(context);
    }

    public MNPanelWindowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MNPanelWindowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initWithWidgetMatrix() {
        this.setOrientation(VERTICAL);

        panelLineLayouts = new LinearLayout[2];
        panelLayouts = new MNPanelLayout[4];

        // 패널들이 있는 레이아웃을 추가
        for (int i = 0; i < 2; i++) {
            panelLineLayouts[i] = new LinearLayout(getContext());
            panelLineLayouts[i].setOrientation(HORIZONTAL);
            panelLineLayouts[i].setWeightSum(2);

            LayoutParams layoutParams =
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            panelLineLayouts[i].setLayoutParams(layoutParams);
            this.addView(panelLineLayouts[i]);

            // 각 패널 레이아웃을 추가
            for (int j = 0; j < 2; j++) {
                // 저장된 패널 id를 로드 - 기존 코드에서 panelDataObject를 활용하게 변경
//                List<Integer> uniquePanelIds = MNPanel.getPanelUniqueIdList(getContext());
                List<JSONObject> panelDataObjects = MNPanel.getPanelDataList(getContext());
                int uniquePanelId = -1;
                try {
                    uniquePanelId = panelDataObjects.get(i * 2 + j).getInt(MNPanel.PANEL_UNIQUE_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MNPanelType panelType = MNPanelType.valueOfUniqueId(uniquePanelId);

                // 패널 id에 맞게 패널 레이아웃 생성
                int index = i * 2 + j;
                panelLayouts[index] = MNPanelLayoutFactory.newPanelLayoutInstance(panelType, index, getContext());
                panelLineLayouts[i].addView(panelLayouts[index]);

                // 로딩 애니메이션이 onCreate시에는 제대로 생성이 안되기 때문에 뷰 로딩 이후에 리프레시
                final MNPanelLayout panelLayout = panelLayouts[index];
                MNViewSizeMeasure.setViewSizeObserver(panelLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                    @Override
                    public void onLayoutLoad() {
                        try {
                            panelLayout.refreshPanel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void applyTheme() {
        for (int i = 0; i < 4; i++) {
                MNShadowLayoutFactory.changeThemeOfShadowLayout(panelLayouts[i], getContext());
                panelLayouts[i].applyTheme();
        }
    }

    public void refreshAllPanels() {
        for (int i = 0; i < 4; i++) {
            try {
                panelLayouts[i].refreshPanel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 패널 디테일 프래그먼트의 셀렉트 페이저에서 패널을 변경할 때의 콜백 메서드
    public void replacePanel(Intent data) {
        JSONObject panelDataObject;
        try {
            panelDataObject = new JSONObject(data.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
            if (panelDataObject != null) {
                int index = panelDataObject.getInt(MNPanel.PANEL_WINDOW_INDEX);
                int uniqueId = panelDataObject.getInt(MNPanel.PANEL_UNIQUE_ID);
                if (index >= 0 && index < 4) {
                    // 패널 레이아웃 갱신
                    panelLineLayouts[index / 2].removeViewAt(index % 2);
                    panelLayouts[index] = MNPanelLayoutFactory.newPanelLayoutInstance(
                            MNPanelType.valueOfUniqueId(uniqueId), index, getContext());
                    panelLineLayouts[index / 2].addView(panelLayouts[index], index % 2);
                    panelLayouts[index].refreshPanel();
                } else {
                    throw new AssertionError("index must be > 0 and <= 4");
                }
            } else {
                throw new AssertionError("panelDataObject must not be null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshPanel(Intent data) {
        // data에서 index 추출
        JSONObject panelDataObject;
        try {
            panelDataObject = new JSONObject(data.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
            if (panelDataObject != null) {
                int index = panelDataObject.getInt(MNPanel.PANEL_WINDOW_INDEX);
                if (index >= 0 && index < 4) {
                    // 새 패널데이터 삽입 및 패널 갱신
                    panelLayouts[index].setPanelDataObject(panelDataObject);
                    panelLayouts[index].refreshPanel();
                } else {
                    throw new AssertionError("index must be > 0 and <= 4");
                }
            } else {
                throw new AssertionError("panelDataObject must not be null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
