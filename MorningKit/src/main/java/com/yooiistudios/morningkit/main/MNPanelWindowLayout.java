package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
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

    public void initWithWidgetMatrix()
    {
        this.setOrientation(VERTICAL);

        panelLineLayouts = new LinearLayout[2];
        panelLayouts = new MNPanelLayout[2][2];
//        widgetSlots = new FrameLayout[2][2];

//        int padding = DipToPixel.getPixel(getContext(), 3);

//        this.setPadding(padding, padding, padding, padding);

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

//                widgetSlots[i][j] = new FrameLayout(getContext());

//                if (getContext() != null) {
//                    Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                display.getSize(size);
                // deprecated 되서 동현이 말 듣고 수정
//                int width = display.getWidth();
//                int height = display.getHeight();
//                    int width = size.x;
//                    int height = size.y;

//                    Log.i(TAG, "" + width + " " + height);

//                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width/2,
//                            DipToPixel.getPixel(getContext(), MN.widget.WIDGET_HEIGHT_PHONE_DP));
//                    widgetSlots[i][j].setLayoutParams(params);
//                    widgetSlots[i][j].setPadding(3, 3, 3, 3);
//
//                    widgetSlots[i][j].setBackgroundColor(Color.BLUE); // test
//                    panelLayouts[i].addView(widgetSlots[i][j]);
//                }
            }
        }
    }

    public void applyTheme() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                MNShadowLayoutFactory.changeThemeOfShadowLayout(panelLayouts[i][j], getContext());
            }
        }
    }
}
