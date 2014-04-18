package com.yooiistudios.morningkit.setting.theme.panelmatrix;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.MNSettingThemeDetailItemViewHolder;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNPanelMatrixListAdapter
 */
public class MNPanelMatrixListAdapter extends BaseAdapter {
    private Activity activity;

    private MNPanelMatrixListAdapter() {}
    public MNPanelMatrixListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return MNPanelMatrixType.values().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(activity).inflate(R.layout.setting_theme_detail_item, parent, false);

        if (convertView != null) {
            MNSettingThemeDetailItemViewHolder viewHolder = new MNSettingThemeDetailItemViewHolder(convertView);
            viewHolder.getLockImageView().setVisibility(View.GONE);

            MNPanelMatrixType panelMatrixType = MNPanelMatrixType.valueOf(position);
            switch (panelMatrixType) {
                case PANEL_MATRIX_2X2:
                    viewHolder.getTitleTextView().setText("2 X 2");
                    break;

                case PANEL_MATRIX_2X3:
                    viewHolder.getTitleTextView().setText("2 X 1");
                    break;
            }
            if (panelMatrixType != MNPanelMatrix.getCurrentPanelMatrixType(activity)) {
                viewHolder.getCheckImageView().setVisibility(View.GONE);
            }

            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(activity);

            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
            viewHolder.getCheckImageView().setImageResource(MNSettingResources.getCheckResourceId(currentThemeType));
            viewHolder.getLockImageView().setImageResource(MNSettingResources.getLockResourceId(currentThemeType));

            // theme - shadow
            RoundShadowRelativeLayout roundShadowRelativeLayout = (RoundShadowRelativeLayout) convertView.findViewById(viewHolder.getShadowLayout().getId());

            // 동적 생성 -> 색 변경 로직 변경
//            RoundShadowRelativeLayout newShadowRelativeLayout = MNShadowLayoutFactory.changeShadowLayout(currentThemeType, roundShadowRelativeLayout, viewHolder.getOuterLayout());
            MNShadowLayoutFactory.changeThemeOfShadowLayout(roundShadowRelativeLayout, activity);

            // onClick
            if (roundShadowRelativeLayout != null) {
                roundShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MNSound.isSoundOn(activity)) {
                            MNSoundEffectsPlayer.play(R.raw.effect_view_close, activity);
                        }
                        MNPanelMatrix.setPanelMatrixType(MNPanelMatrixType.valueOf(position), activity);
                        activity.finish();
                    }
                });
            } else {
                throw new AssertionError("shadowRelativeLayout must not be null!");
            }
        }
        return convertView;
    }
}
