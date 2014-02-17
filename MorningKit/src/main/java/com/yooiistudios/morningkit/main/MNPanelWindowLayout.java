package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.MNPanelFactory;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;

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
    @Getter private MNPanelLayout[][] panelLayouts;
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
        panelLayouts = new MNPanelLayout[2][2];

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
                // 저장된 패널 id를 로드
                List<Integer> uniquePanelIds = MNPanel.getPanelUniqueIdList(getContext());
                MNPanelType panelType = MNPanelType.valueOfUniqueId(uniquePanelIds.get(i * 2 + j));

                // 패널 id에 맞게 패널 레이아웃 생성
                panelLayouts[i][j] = MNPanelFactory.newPanelLayoutInstance(panelType, getContext());
                panelLineLayouts[i].addView(panelLayouts[i][j]);

                // 로딩 애니메이션이 onCreate시에는 제대로 생성이 안되기 때문에 뷰 로딩 이후에 리프레시
                final MNPanelLayout panelLayout = panelLayouts[i][j];
                MNViewSizeMeasure.setViewSizeObserver(panelLayout, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                    @Override
                    public void onLayoutLoad() {
                        panelLayout.refreshPanel();
                    }
                });
            }
        }

        // 패널 초기화 후 전체 리프레시
//        refreshAllPanels();
    }

    public void applyTheme() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                MNShadowLayoutFactory.changeThemeOfShadowLayout(panelLayouts[i][j], getContext());
                panelLayouts[i][j].applyTheme();
            }
        }
    }

    public void refreshAllPanels() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                panelLayouts[i][j].refreshPanel();
            }
        }
    }
}
