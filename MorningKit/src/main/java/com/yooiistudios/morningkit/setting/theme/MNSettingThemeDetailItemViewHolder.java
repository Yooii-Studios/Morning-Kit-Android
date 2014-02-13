package com.yooiistudios.morningkit.setting.theme;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNSettingThemeDetailItemViewHolder
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * ViewHolder
 */
public class MNSettingThemeDetailItemViewHolder {
    @Getter @InjectView(R.id.setting_theme_detail_item_outer_layout)            RelativeLayout outerLayout;
    @Getter @InjectView(R.id.setting_theme_detail_item_shadow_layout)
    RoundShadowRelativeLayout shadowLayout;
    @Getter @InjectView(R.id.setting_theme_detail_item_inner_layout)            RelativeLayout innerLayout;
    @Getter @InjectView(R.id.setting_theme_detail_item_textview)                TextView titleTextView;
    @Getter @InjectView(R.id.setting_theme_detail_item_check_imageview)         ImageView checkImageView;
    @Getter @InjectView(R.id.setting_theme_detail_item_lock_imageview)          ImageView lockImageView;

    public MNSettingThemeDetailItemViewHolder(View view) {
        ButterKnife.inject(this, view);
    }
}