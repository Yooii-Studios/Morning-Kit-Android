package com.yooiistudios.morningkit.setting.theme.panelmatrix;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.MNSettingThemeDetailItemViewHolder;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.List;

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

            MNPanelMatrixType panelMatrixType = MNPanelMatrixType.valueOf(position);
            switch (panelMatrixType) {
                case PANEL_MATRIX_2X2:
                    viewHolder.getTitleTextView().setText("2 X 2");
                    break;

                case PANEL_MATRIX_2X3:
                    viewHolder.getTitleTextView().setText("2 X 3");
                    break;
            }
            if (panelMatrixType != MNPanelMatrix.getCurrentPanelMatrixType(activity)) {
                viewHolder.getCheckImageView().setVisibility(View.GONE);
            }

            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(activity);

//            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//            viewHolder.getCheckImageView().setImageResource(MNSettingResources.getCheckResourceId(currentThemeType));
//            viewHolder.getLockImageView().setImageResource(MNSettingResources.getLockResourceId(currentThemeType));
//            viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

            // onClick
            viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MNSound.isSoundOn(activity)) {
                        MNSoundEffectsPlayer.play(R.raw.effect_view_close, activity);
                    }
                    MNPanelMatrix.setPanelMatrixType(MNPanelMatrixType.valueOf(position), activity);
                    activity.finish();
                }
            });

            // lock
            if (panelMatrixType == MNPanelMatrixType.PANEL_MATRIX_2X2) {
                viewHolder.getLockImageView().setVisibility(View.GONE);
            } else {
                List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(activity);
                if (ownedSkus.contains(SKIabProducts.SKU_PANEL_MATRIX_2X3)) {
                    // 아이템 구매완료
                    viewHolder.getLockImageView().setVisibility(View.GONE);
                } else {
                    // 아이템 잠김
                    viewHolder.getInnerLayout().setBackgroundResource(
                            MNSettingResources.getLockItemResourcesId(currentThemeType));

                    // lock onClickListener
                    viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MNUnlockActivity.class);
                            intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY,
                                    SKIabProducts.SKU_PANEL_MATRIX_2X3);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);
                        }
                    });
                }
            }
        }
        return convertView;
    }
}
