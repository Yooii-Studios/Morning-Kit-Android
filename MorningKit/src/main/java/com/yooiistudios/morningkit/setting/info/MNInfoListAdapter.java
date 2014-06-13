package com.yooiistudios.morningkit.setting.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

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
                case STORE:
                    viewHolder.getTextView().setText(R.string.info_store);
                    break;
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
                case RECOMMEND:
                    viewHolder.getTextView().setText(R.string.recommend_to_friends);
                    break;
            }

            // theme
//            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);
//            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//            viewHolder.getTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//            viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

            // onClick
            viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MNSound.isSoundOn(context)) {
                        MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
                    }
                    infoItemClickListener.onItemClick(position);
                }
            });
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
        @Getter @InjectView(R.id.setting_info_item_textview)         TextView       textView;

        public MNSettingInfoItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
