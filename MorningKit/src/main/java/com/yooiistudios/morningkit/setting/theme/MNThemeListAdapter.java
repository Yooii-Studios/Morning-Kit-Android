package com.yooiistudios.morningkit.setting.theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.alarmstatusbar.MNAlarmStatusBarIcon;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrix;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 7.
 *
 * MNInfoListViewAdapter
 *  인포 프래그먼트의 리스트 어댑터
 */
public class MNThemeListAdapter extends BaseAdapter {

    private static final String TAG = "MNAlarmListAdapter";
    private Context context;
    private MNThemeListAdapter() {}
    public MNThemeListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.setting_theme_item, parent, false);

        if (convertView != null) {
            MNSettingThemeItemViewHolder viewHolder = new MNSettingThemeItemViewHolder(convertView);
            MNThemeItemType type = MNThemeItemType.valueOf(position);
            switch (type) {
                case THEME:
                    viewHolder.getTitleTextView().setText(R.string.setting_theme_color);
                    viewHolder.getDetailTextView().setText(MNThemeType.toString(MNTheme.getCurrentThemeType(context).getIndex(), context));
                    break;

                case LANGUAGE:
                    viewHolder.getTitleTextView().setText(R.string.setting_language);
                    MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(context);
                    viewHolder.getDetailTextView().setText(MNLanguageType.toTranselatedString(currentLanguageType.getIndex(), context));
                    break;

                case PANEL_MATRIX:
                    viewHolder.getTitleTextView().setText(R.string.setting_widgettable);
                    switch (MNPanelMatrix.getCurrentPanelMatrixType(context)) {
                        case PANEL_MATRIX_2X2:
                            viewHolder.getDetailTextView().setText("2 X 2");
                            break;
                        case PANEL_MATRIX_2X3:
                            viewHolder.getDetailTextView().setText("2 X 3");
                            break;
                    }
                    break;

                case ALARM_STATUS_BAR:
                    viewHolder.getTitleTextView().setText(R.string.alarm_status_bar_icon);
                    switch (MNAlarmStatusBarIcon.getCurrentAlarmStatusBarIconType(context)) {
                        case ON:
                            viewHolder.getDetailTextView().setText(R.string.setting_effect_sound_on);
                            break;
                        case OFF:
                            viewHolder.getDetailTextView().setText(R.string.setting_effect_sound_off);
                            break;
                    }
                    break;

//                case SOUND_EFFECTS:
//                    viewHolder.getTitleTextView().setText(R.string.setting_effect_sound);
//                    switch (MNSound.getCurrentSoundType(context)) {
//                        case ON:
//                            viewHolder.getDetailTextView().setText(R.string.setting_effect_sound_on);
//                            break;
//                        case OFF:
//                            viewHolder.getDetailTextView().setText(R.string.setting_effect_sound_off);
//                            break;
//                    }
//                    break;
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return MNThemeItemType.values().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * ViewHolder
     */
    static class MNSettingThemeItemViewHolder {
        @Getter @InjectView(R.id.setting_theme_item_outer_layout)       RelativeLayout outerLayout;
        @Getter @InjectView(R.id.setting_theme_item_inner_layout)       RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_theme_item_title_textview)     TextView       titleTextView;
        @Getter @InjectView(R.id.setting_theme_item_detail_textview)    TextView       detailTextView;

        public MNSettingThemeItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
