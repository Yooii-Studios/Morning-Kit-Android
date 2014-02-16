package com.yooiistudios.morningkit.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 16.
 *
 * PanelLayout
 *  패널의 근간이 되는 레이아웃
 */
public class MNPanelLayout extends RoundShadowRelativeLayout {

    @Getter @Setter MNPanelType panelType;
    @Getter @Setter RelativeLayout contentLayout;
    @Getter @Setter boolean isUsingNetwork;

    public MNPanelLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        setClipChildren(false);

        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.panel_height));
        layoutParams.weight = 1;

        setLayoutParams(layoutParams);

        // content layout 추가
        contentLayout = new RelativeLayout(getContext());

        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        int padding = (int) getResources().getDimension(R.dimen.margin_inner);
        contentLayout.setPadding(padding, padding, padding, padding);
        contentLayout.setLayoutParams(contentLayoutParams);

        addView(contentLayout);
    }

    public void refreshPanel() {
        if (isUsingNetwork) {
            // 네트워크 체크
            boolean isReachable = true;
            if (isReachable) {
                // 로딩 중 애니메이션
                // processLoading

                // 로딩이 끝나고 애니메이션 해제 후
                // updateUI
            } else {
                // 네트워크 불가 이미지
            }
        } else {
            // processLoading
            // updateUI
        }
    }

    public void applyTheme() {

    }
}
