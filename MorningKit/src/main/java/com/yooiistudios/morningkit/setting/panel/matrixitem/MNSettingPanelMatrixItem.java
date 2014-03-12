package com.yooiistudios.morningkit.setting.panel.matrixitem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.panel.core.MNPanelType;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 29.
 *
 * MNSettingPanelMatrixItem
 */
public class MNSettingPanelMatrixItem extends RelativeLayout {

    @Getter @Setter TextView panelNameTextView;
    @Getter @Setter ImageView panelImageView;
    @Getter @Setter RoundShadowRelativeLayout shadowLayout;
    @Getter @Setter RelativeLayout containerLayout;
    @Getter @Setter MNPanelType panelType;

    private Context context;
    private AttributeSet attrs;

    public MNSettingPanelMatrixItem(Context context) {
        super(context);
        this.context = context;
        if (!isInEditMode()) {
            init();
        }
    }

    public MNSettingPanelMatrixItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        if (!isInEditMode()) {
            init();
        }
    }

    public void init() {
        Resources resources = getResources();

        if (resources != null) {
            // shadow layout
            if (attrs != null) {
                shadowLayout = new RoundShadowRelativeLayout(context, attrs);
            } else {
                shadowLayout = new RoundShadowRelativeLayout(context);
            }
            LayoutParams shadowLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            shadowLayout.setLayoutParams(shadowLayoutParams);
            addView(shadowLayout);

            // 기존의 테마별 동적 생성이 아닌, 색만 교체하는 방식으로 로직 개선
//            shadowLayout = MNShadowLayoutFactory.changeShadowLayout(MNTheme.getCurrentThemeType(getContext()), shadowLayout, this);
            // 동적이 아닌 방식으로 처리
            MNShadowLayoutFactory.changeThemeOfShadowLayout(shadowLayout, context);
            //

            // 터치 색은 forward색과 같게 처리
            shadowLayout.setPressedColor(shadowLayout.getSolidAreaColor());

            // container layout
            if (attrs != null) {
                containerLayout = new RelativeLayout(context, attrs);
            } else {
                containerLayout = new RelativeLayout(context);
            }
            containerLayout.setLayoutParams(shadowLayoutParams);
            containerLayout.setPadding((int) resources.getDimension(R.dimen.setting_panel_item_margin),
                    (int) resources.getDimension(R.dimen.setting_panel_item_margin),
                    (int) resources.getDimension(R.dimen.setting_panel_item_margin),
                    (int) resources.getDimension(R.dimen.setting_panel_item_margin));
            addView(containerLayout);

            // image
            if (attrs != null) {
                panelImageView = new ImageView(context, attrs);
            } else {
                panelImageView = new ImageView(context);
            }
            LayoutParams imageViewlayoutParams = new LayoutParams(
                    (int)getResources().getDimension(R.dimen.setting_panel_item_image_view_size),
                    (int)getResources().getDimension(R.dimen.setting_panel_item_image_view_size));
            imageViewlayoutParams.addRule(CENTER_HORIZONTAL);
            imageViewlayoutParams.addRule(ALIGN_TOP);
            panelImageView.setLayoutParams(imageViewlayoutParams);
            containerLayout.addView(panelImageView);

            // text
            if (attrs != null) {
                panelNameTextView = new TextView(context, attrs);
            } else {
                panelNameTextView = new TextView(context);
            }
            // text stype
            panelNameTextView.setGravity(Gravity.CENTER);
            panelNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.setting_panel_item_text_font_size));
            panelNameTextView.setTypeface(panelNameTextView.getTypeface(), Typeface.BOLD);
            panelNameTextView.setSingleLine();

            // layout params
            LayoutParams textViewlayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewlayoutParams.addRule(ALIGN_PARENT_BOTTOM);
            textViewlayoutParams.bottomMargin = (int) resources.getDimension(R.dimen.margin_inner);
            panelNameTextView.setLayoutParams(textViewlayoutParams);
            containerLayout.addView(panelNameTextView);
        }
    }
}
