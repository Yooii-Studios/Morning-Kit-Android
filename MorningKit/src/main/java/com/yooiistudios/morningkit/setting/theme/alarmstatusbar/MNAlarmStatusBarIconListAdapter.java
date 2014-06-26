package com.yooiistudios.morningkit.setting.theme.alarmstatusbar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.MNSettingThemeDetailItemViewHolder;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSoundType;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNSoundEffectListAdapter
 */
public class MNAlarmStatusBarIconListAdapter extends BaseAdapter {
    private Activity activity;

    private MNAlarmStatusBarIconListAdapter() {}
    public MNAlarmStatusBarIconListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return MNSoundType.values().length;
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

            MNAlarmStatusBarIconType alarmStatusBarIconType = MNAlarmStatusBarIconType.valueOf(position);
            switch (alarmStatusBarIconType) {
                case ON:
                    viewHolder.getTitleTextView().setText(R.string.setting_effect_sound_on);
                    break;

                case OFF:
                    viewHolder.getTitleTextView().setText(R.string.setting_effect_sound_off);
                    break;
            }
            if (alarmStatusBarIconType != MNAlarmStatusBarIcon.getCurrentAlarmStatusBarIconType(activity)) {
                viewHolder.getCheckImageView().setVisibility(View.GONE);
            }

            // theme
//            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(activity);
//
//            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//            viewHolder.getCheckImageView().setImageResource(MNSettingResources.getCheckResourceId(currentThemeType));
//            viewHolder.getLockImageView().setImageResource(MNSettingResources.getLockResourceId(currentThemeType));
//            viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

            // onClick
            viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MNAlarmStatusBarIcon.setAlarmStatusBarIconType(MNAlarmStatusBarIconType.valueOf(position), activity);
                    activity.finish();
                }
            });
        }
        return convertView;
    }
}
