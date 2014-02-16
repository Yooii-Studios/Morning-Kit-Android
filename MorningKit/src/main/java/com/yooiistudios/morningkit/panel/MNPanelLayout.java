package com.yooiistudios.morningkit.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
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
        // content layout 추가
        RelativeLayout contentLayout = new RelativeLayout(getContext());

        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        int padding = (int) getResources().getDimension(R.dimen.margin_inner);
        contentLayout.setPadding(padding, padding, padding, padding);
        contentLayout.setLayoutParams(contentLayoutParams);

        addView(contentLayout);
    }
}
