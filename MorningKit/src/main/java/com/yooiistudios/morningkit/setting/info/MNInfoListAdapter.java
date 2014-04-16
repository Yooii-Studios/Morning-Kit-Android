package com.yooiistudios.morningkit.setting.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
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
public class MNInfoListAdapter extends BaseAdapter {

    private static final String TAG = "MNAlarmListAdapter";
    private Context context;
    private MNInfoItemClickListener infoItemClickListener;
    private MNInfoListAdapter() {}
    public MNInfoListAdapter(Context context, MNInfoItemClickListener infoItemClickListener) {
        this.context = context;
        this.infoItemClickListener = infoItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.setting_info_item, parent, false);
        if (convertView != null) {
            MNSettingInfoItemViewHolder viewHolder = new MNSettingInfoItemViewHolder(convertView);
            MNInfoItemType type = MNInfoItemType.valueOf(position);
            switch (type) {
                case MORNING_KIT_INFO:
                    viewHolder.getTextView().setText(R.string.more_information);
                    break;
                case RATE_MORNING_KIT:
                    viewHolder.getTextView().setText(R.string.rate_morning_kit);
                    break;
                case LIKE_US_ON_FACEBOOK:
                    viewHolder.getTextView().setText("Like us on Facebook");
                    break;
                case CREDITS:
                    viewHolder.getTextView().setText(R.string.info_credit);
                    break;
            }
            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);

            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            viewHolder.getTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));

            // theme - shadow
            RoundShadowRelativeLayout roundShadowRelativeLayout = (RoundShadowRelativeLayout) convertView.findViewById(viewHolder.getShadowLayout().getId());

            // 동적 생성 -> 색 변경 로직 변경
//            RoundShadowRelativeLayout newShadowRelativeLayout = MNShadowLayoutFactory.changeShadowLayout(currentThemeType, roundShadowRelativeLayout, viewHolder.getOuterLayout());
            MNShadowLayoutFactory.changeThemeOfShadowLayout(roundShadowRelativeLayout, context);

            // onClick
            if (roundShadowRelativeLayout != null) {
                roundShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MNSound.isSoundOn(context)) {
                            MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
                        }
                        infoItemClickListener.onItemClick(position);
                    }
                });
            } else {
                throw new AssertionError("shadowRelativeLayout must not be null!");
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return MNInfoItemType.values().length;
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
    static class MNSettingInfoItemViewHolder {
        @Getter @InjectView(R.id.setting_info_item_outer_layout)     RelativeLayout outerLayout;
        @Getter @InjectView(R.id.setting_info_item_inner_layout)     RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_info_item_shadow_layout)    RelativeLayout shadowLayout;
        @Getter @InjectView(R.id.setting_info_item_textview)         TextView       textView;

        public MNSettingInfoItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
