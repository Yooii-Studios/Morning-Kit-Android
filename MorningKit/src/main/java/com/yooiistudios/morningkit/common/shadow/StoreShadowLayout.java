package com.yooiistudios.morningkit.common.shadow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.R;

// 동현이 소스를 참고해 스토어용으로 만든 클래스
public class StoreShadowLayout extends RoundShadowRelativeLayout {

	public StoreShadowLayout(Context context) {
		super(context);
    }
	
	public StoreShadowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
    }

    @Override
	protected void initColor() {
		setBackgroundColor(Color.TRANSPARENT);

        Resources resources;
        if (getContext() != null) {
            resources = getContext().getResources();
            mRoundRectRadius = (int)resources.getDimension(R.dimen.rounded_corner_radius);
            mBlurRadius = (int)resources.getDimension(R.dimen.setting_store_gridview_spacing_inner);
//            mBlurRadius = DPToPixel(getContext(), 4);

            mSolidColor = resources.getColor(R.color.setting_store_grid_item_background_color);
            mPressedColor = resources.getColor(R.color.setting_store_grid_item_background_color_touched);

            mShadowColor = Color.argb(160, 30, 30, 30);
        }
	}
}
