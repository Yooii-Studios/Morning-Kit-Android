package com.yooiistudios.morningkit.setting.info.moreinfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
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
public class MNMoreInfoListAdapter extends BaseAdapter {

    private static final String TAG = "MNMoreInfoListViewAdapter";
    private Context context;
    private MNMoreInfoItemClickListener moreInfoItemClickListener;
    private MNMoreInfoListAdapter() {}
    public MNMoreInfoListAdapter(Context context, MNMoreInfoItemClickListener moreInfoItemClickListener) {
        this.context = context;
        this.moreInfoItemClickListener = moreInfoItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);

        if (position != MNMoreInfoItemType.VERSION.getIndex()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.setting_info_item, parent, false);
            if (convertView != null) {
                MNSettingInfoItemViewHolder viewHolder = new MNSettingInfoItemViewHolder(convertView);
                MNMoreInfoItemType type = MNMoreInfoItemType.valueOf(position);
                switch (type) {
                    case YOOII_STUDIOS:
                        viewHolder.getTextView().setText(R.string.more_information_yooii_studios);
                        break;
                    case MORNING_KIT_HELP:
                        viewHolder.getTextView().setText(R.string.more_information_morning_kit_help);
                        break;
                    case LICENSE:
                        viewHolder.getTextView().setText(R.string.more_information_license);
                        break;
                }

                // theme
//                viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//                viewHolder.getTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//                viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

                // onClick
                viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreInfoItemClickListener.onItemClick(position);
                    }
                });
            }
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.setting_info_version_item, parent, false);
            if (convertView != null) {
                MNSettingInfoVersionItemViewHolder viewHolder = new MNSettingInfoVersionItemViewHolder(convertView);

                viewHolder.getTitleTextView().setText(R.string.more_information_version_info);

                PackageInfo pInfo;
                try {
                    if (context.getPackageManager() != null) {
                        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        String version = pInfo.versionName;
                        viewHolder.getDetailTextView().setText(version);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                // theme
//                viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//                viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//                viewHolder.getDetailTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//                viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return MNMoreInfoItemType.values().length;
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

    static class MNSettingInfoVersionItemViewHolder {
        @Getter @InjectView(R.id.setting_info_item_outer_layout)            RelativeLayout outerLayout;
        @Getter @InjectView(R.id.setting_info_item_inner_layout)            RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_info_version_title_textview)       TextView       titleTextView;
        @Getter @InjectView(R.id.setting_info_version_detail_textview)      TextView       detailTextView;

        public MNSettingInfoVersionItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
