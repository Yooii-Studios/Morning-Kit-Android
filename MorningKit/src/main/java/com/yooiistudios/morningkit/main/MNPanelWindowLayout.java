package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanel;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.core.MNPanelLayoutFactory;
import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrix;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixType;

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
//    private static final String TAG = "MNWidgetWindowLayout";

    // 현재 지원하는 최대의 매트릭스 row과 갯수
    private static final int PANEL_ROWS = 3;
    private static final int NUMBER_OF_PANELS = 6;

    private Context context;
    @Getter private LinearLayout panelLineLayouts[];
    @Getter private MNPanelLayout panelLayouts[];
//    @Getter private FrameLayout[][] widgetSlots;

    public MNPanelWindowLayout(Context context) {
        super(context);
        this.context = context;
    }

    public MNPanelWindowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initWithPanelMatrix() {
        this.setOrientation(VERTICAL);

        panelLineLayouts = new LinearLayout[PANEL_ROWS];
        panelLayouts = new MNPanelLayout[NUMBER_OF_PANELS];

        // 패널들이 있는 레이아웃을 추가
        for (int i = 0; i < PANEL_ROWS; i++) {
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
        for (MNPanelLayout panelLayout : panelLayouts) {
            MNShadowLayoutFactory.changeThemeOfShadowLayout(panelLayout, getContext());
            panelLayout.applyTheme();
        }
    }

    // 방향과 무관
    public void refreshAllPanels() {
        for (MNPanelLayout panelLayout : panelLayouts) {
            try {
                panelLayout.refreshPanel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 패널 디테일 프래그먼트의 셀렉트 페이저에서 패널을 변경해서 메인으로 나올 때의 콜백 메서드
    public void replacePanel(Intent data) {
        JSONObject panelDataObject;
        try {
            panelDataObject = new JSONObject(data.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
            if (panelDataObject != null) {
                int index = panelDataObject.getInt(MNPanel.PANEL_WINDOW_INDEX);
                int uniqueId = panelDataObject.getInt(MNPanel.PANEL_UNIQUE_ID);
                if (index >= 0 && index < panelLayouts.length) {
                    // 기존의 위치에 새 패널을 대입
                    panelLayouts[index] = MNPanelLayoutFactory.newPanelLayoutInstance(
                            MNPanelType.valueOfUniqueId(uniqueId), index, getContext());
                    panelLayouts[index].refreshPanel();

                    // 기존 위치에 새 패널을 대입
                    if (MNPanelMatrix.getCurrentPanelMatrixType(getContext()) ==
                            MNPanelMatrixType.PANEL_MATRIX_2X2) {
                        // 2X2는 크게 영향이 없으므로 기존 코드 사용(배열 변경 X)
                        panelLineLayouts[index / 2].removeViewAt(index % 2);
                        panelLineLayouts[index / 2].addView(panelLayouts[index], index % 2);
                    } else {
                        // 방향을 알아내어 그에 따라 배열을 변경
                        switch (getResources().getConfiguration().orientation) {
                            // 2X3
                            case Configuration.ORIENTATION_PORTRAIT:
                                panelLineLayouts[index / 2].removeViewAt(index % 2);
                                panelLineLayouts[index / 2].addView(panelLayouts[index], index % 2);
                                break;

                            // 3X2
                            case Configuration.ORIENTATION_LANDSCAPE:
                                panelLineLayouts[index / 3].removeViewAt(index % 3);
                                panelLineLayouts[index / 3].addView(panelLayouts[index], index % 3);
                                break;
                        }
                    }
                } else {
                    throw new AssertionError("index must be > 0 and <= panelLayouts.length");
                }
            } else {
                throw new AssertionError("panelDataObject must not be null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshPanel(Intent data) {
        // data에서 index 추출, 회전과 무관
        JSONObject panelDataObject;
        try {
            panelDataObject = new JSONObject(data.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
            if (panelDataObject != null) {
                int index = panelDataObject.getInt(MNPanel.PANEL_WINDOW_INDEX);
                if (index >= 0 && index < panelLayouts.length) {
                    // 새 패널데이터 삽입 및 패널 갱신
                    panelLayouts[index].setPanelDataObject(panelDataObject);
                    panelLayouts[index].refreshPanel();
                } else {
                    throw new AssertionError("index must be > 0 and <= panelLayouts.length");
                }
            } else {
                throw new AssertionError("panelDataObject must not be null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 방향에 따라 배열, 높이 변경
     */
    public void adjustPanelLayoutMatrixAtOrientation(final int orientation) {
        if (MNPanelMatrix.getCurrentPanelMatrixType(getContext()) ==
                MNPanelMatrixType.PANEL_MATRIX_2X3) {
            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:        // 2X3이 되게 새로 정렬
                    // 1 2
                    // 3 4
                    // 5 6
                    // 가로모드에서 세로모드로 전환할 때에는 3X2 -> 2X3
                    for (LinearLayout panelLineLayout : panelLineLayouts) {
                        panelLineLayout.removeAllViews();       // 모든 뷰를 먼저 삭제
                    }
                    panelLineLayouts[0].setWeightSum(2);        // 웨이트 정렬
                    panelLineLayouts[1].setWeightSum(2);
                    panelLineLayouts[2].setVisibility(View.VISIBLE);
                    for (int i = 0; i < panelLayouts.length; i++) {
                        panelLineLayouts[i / 2].addView(panelLayouts[i], i % 2);
                    }
                    break;

                case Configuration.ORIENTATION_LANDSCAPE:       // 3X2가 되게 새로 정렬
                    // 1 2 3
                    // 4 5 6
                    for (LinearLayout panelLineLayout : panelLineLayouts) {
                        panelLineLayout.removeAllViews();       // 모든 뷰를 먼저 삭제
                    }
                    panelLineLayouts[0].setWeightSum(3);        // 웨이트 정렬
                    panelLineLayouts[1].setWeightSum(3);
                    panelLineLayouts[2].setVisibility(View.GONE);
                    for (int i = 0; i < panelLayouts.length; i++) {
                        panelLineLayouts[i / 3].addView(panelLayouts[i], i % 3);
                    }
                    break;
            }
        } else if (MNPanelMatrix.getCurrentPanelMatrixType(getContext()) ==
                MNPanelMatrixType.PANEL_MATRIX_2X2) {
            // 1 2
            // 3 4
            if (panelLineLayouts.length == 3) {
                for (LinearLayout panelLineLayout : panelLineLayouts) {
                    panelLineLayout.removeAllViews();           // 모든 뷰를 먼저 삭제
                }
                panelLineLayouts[0].setWeightSum(2);            // 웨이트 정렬
                panelLineLayouts[1].setWeightSum(2);
                panelLineLayouts[2].setVisibility(View.GONE);
                for (int i = 0; i < panelLayouts.length; i++) {
                    panelLineLayouts[i / 2].addView(panelLayouts[i], i % 2);
                }
            }
        }
    }

    /**
     * 방향에 따라 각 패널의 높이를 조절
     */
    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                for (MNPanelLayout panelLayout : panelLayouts) {
                    LinearLayout.LayoutParams layoutParams = (LayoutParams) panelLayout.getLayoutParams();
                    layoutParams.height = getResources().getDimensionPixelSize(R.dimen.panel_height);
                }
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                for (MNPanelLayout panelLayout : panelLayouts) {
                    LinearLayout.LayoutParams layoutParams = (LayoutParams) panelLayout.getLayoutParams();
                    layoutParams.height = getHeight() / 2;  // 패널윈도우 높이의 절반씩 사용
                }
                break;
        }
    }
}
