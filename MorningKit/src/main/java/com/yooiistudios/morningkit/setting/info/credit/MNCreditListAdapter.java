package com.yooiistudios.morningkit.setting.info.credit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.CelestialThemeShadowLayout;
import com.yooiistudios.morningkit.common.shadow.ModernityThemeShadowLayout;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.SlateThemeShadowLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 8.
 *
 * MNCreditListAdapter
 */
public class MNCreditListAdapter extends BaseAdapter {
    private Context context;
    private MNCreditListAdapter() {}
    public MNCreditListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return 13;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.setting_info_credit_item, parent, false);
        if (convertView != null) {
            final MNSettingInfoCreditItemViewHolder viewHolder = new MNSettingInfoCreditItemViewHolder(convertView);

            switch (position) {
                case 0:
                    viewHolder.getTitleTextView().setText("Executive Producer");
                    viewHolder.getNameTextView().setText("Robert Song");
                    break;
                case 1:
                    viewHolder.getTitleTextView().setText("Lead Software Engineer");
                    viewHolder.getNameTextView().setText("Steven Kim");
                    break;
                case 2:
                    viewHolder.getTitleTextView().setText("Art Director");
                    viewHolder.getNameTextView().setText("Ted");
                    break;
                case 3:
                    viewHolder.getTitleTextView().setText("Main Artist");
                    viewHolder.getNameTextView().setText("Ga Hyeon Park");
                    break;
                case 4:
                    viewHolder.getTitleTextView().setText("Artist");
                    viewHolder.getNameTextView().setText("Jeong Eun Sil");
                    break;
                case 5:
                    viewHolder.getTitleTextView().setText("Artist");
                    viewHolder.getNameTextView().setText("Kylie Oh");
                    break;
                case 6:
                    viewHolder.getTitleTextView().setText("Creative Manager");
                    viewHolder.getNameTextView().setText("Jasmine Jeongmin Oh");
                    break;
                case 7:
                    viewHolder.getTitleTextView().setText("Development Manager");
                    viewHolder.getNameTextView().setText("Jeff Jeong");
                    break;
                case 8:
                    viewHolder.getTitleTextView().setText("Sound Director");
                    viewHolder.getNameTextView().setText("Sean Lee");
                    break;
                case 9:
                    viewHolder.getTitleTextView().setText("QA");
                    viewHolder.getNameTextView().setText("Yooii Studios Members");
                    break;
                case 10:
                    viewHolder.getTitleTextView().setText("Development Consulting by");
                    viewHolder.getNameTextView().setText("PlayFluent");
                    break;
                case 11:
                    viewHolder.getTitleTextView().setText("Localization");
                    viewHolder.getNameTextView().setText("Akira Yamada\n" +
                            "Angela Choi\n" +
                            "Brad Tsao\n" +
                            "Chez Kuo\n" +
                            "Jasmine Jeongmin Oh\n" +
                            "Jason Piros\n" +
                            "Lena Zaverukha\n" +
                            "Matt Wang\n" +
                            "Taft Love\n" +
                            "Yu Wang");
                    break;
                case 12:
                    viewHolder.getTitleTextView().setText("Special Thanks to");
                    viewHolder.getNameTextView().setText("Andrew Ryu\n" +
                            "HyoSang Lim\n" +
                            "JongHwa Kim\n" +
                            "Kevin Cho\n" +
                            "KwanSoo Choi\n" +
                            "Lou Hsin\n" +
                            "Osamu Takahashi\n" +
                            "SangWon Ko\n" +
                            "SungMoon Cho\n" +
                            "The Great Frog Party");
                    break;
            }
            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(context);

            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
            viewHolder.getNameTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));

            // theme - shadow / 일반적으로는 MNShadowLayoutFactory에서 사용하지만 여기는 커스터마이징이 필요해서 직접 코딩함
            RoundShadowRelativeLayout roundShadowRelativeLayout = (RoundShadowRelativeLayout) convertView.findViewById(viewHolder.getShadowLayout().getId());
            RelativeLayout innerLayout = (RelativeLayout) roundShadowRelativeLayout.findViewById(viewHolder.getInnerLayout().getId());
            RoundShadowRelativeLayout newShadowRelativeLayout;

            switch (currentThemeType) {
                case SLATE_GRAY:
                    newShadowRelativeLayout = new SlateThemeShadowLayout(context);
                    break;

                case MODERNITY_WHITE:
                    newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                    break;

                case CELESTIAL_SKY_BLUE:
                    newShadowRelativeLayout = new CelestialThemeShadowLayout(context);
                    break;

                default:
                    newShadowRelativeLayout = new ModernityThemeShadowLayout(context);
                    break;
            }
            newShadowRelativeLayout.setId(viewHolder.getShadowLayout().getId());
            newShadowRelativeLayout.setLayoutParams(viewHolder.getShadowLayout().getLayoutParams());

            roundShadowRelativeLayout.removeView(innerLayout);
            newShadowRelativeLayout.addView(innerLayout);

            viewHolder.getOuterLayout().removeView(roundShadowRelativeLayout);
            viewHolder.getOuterLayout().addView(newShadowRelativeLayout, 0);

            // onClick
            newShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNSettingInfoCreditItemViewHolder {
        @Getter @InjectView(R.id.setting_info_credit_item_outer_layout)     RelativeLayout  outerLayout;
        @Getter @InjectView(R.id.setting_info_credit_item_inner_layout)     RelativeLayout  innerLayout;
        @Getter @InjectView(R.id.setting_info_credit_item_shadow_layout)
        RoundShadowRelativeLayout shadowLayout;
        @Getter @InjectView(R.id.setting_info_credit_item_title_textview)   TextView        titleTextView;
        @Getter @InjectView(R.id.setting_info_credit_item_name_textview)    TextView        nameTextView;

        public MNSettingInfoCreditItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
