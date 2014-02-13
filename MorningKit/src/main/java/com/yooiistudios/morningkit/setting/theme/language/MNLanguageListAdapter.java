package com.yooiistudios.morningkit.setting.theme.language;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNLanguageListAdapter
 */
public class MNLanguageListAdapter extends BaseAdapter {
    private Activity activity;

    private MNLanguageListAdapter() {}
    public MNLanguageListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return MNLanguageType.values().length;
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
        convertView = LayoutInflater.from(activity).inflate(R.layout.setting_theme_language_item, parent, false);

        if (convertView != null) {
            MNSettingThemeLanguageItemViewHolder viewHolder = new MNSettingThemeLanguageItemViewHolder(convertView);
            viewHolder.getLockImageView().setVisibility(View.GONE);

            viewHolder.getTitleTextView().setText(MNLanguageType.toTranselatedString(position, activity));
            viewHolder.getDetailTextView().setText(MNLanguageType.toEnglishString(position, activity));

            if (MNLanguageType.valueOf(position) != MNLanguage.getCurrentLanguageType(activity)) {
                viewHolder.getCheckImageView().setVisibility(View.GONE);
            }

            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(activity);

            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
            viewHolder.getDetailTextView().setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
            viewHolder.getCheckImageView().setImageResource(MNSettingResources.getCheckResourceId(currentThemeType));
            viewHolder.getLockImageView().setImageResource(MNSettingResources.getLockResourceId(currentThemeType));

            // theme - shadow
            RoundShadowRelativeLayout roundShadowRelativeLayout = (RoundShadowRelativeLayout) convertView.findViewById(viewHolder.getShadowLayout().getId());
            RoundShadowRelativeLayout newShadowRelativeLayout = MNShadowLayoutFactory.changeShadowLayout(currentThemeType, roundShadowRelativeLayout, viewHolder.getOuterLayout());

            // onClick
            if (newShadowRelativeLayout != null) {
                newShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MNSound.isSoundOn(activity)) {
                            MNSoundEffectsPlayer.play(R.raw.effect_view_close, activity);
                        }
                        // archive selection
                        MNLanguage.setLanguageType(MNLanguageType.valueOf(position), activity);

                        // update locale
                        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(activity);
                        Locale locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
                        activity.finish();
                    }
                });
            } else {
                throw new AssertionError("shadowLayout must not be null!");
            }
        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNSettingThemeLanguageItemViewHolder {
        @Getter @InjectView(R.id.setting_theme_language_item_outer_layout)      RelativeLayout outerLayout;
        @Getter @InjectView(R.id.setting_theme_language_item_inner_layout)      RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_theme_language_item_shadow_layout)
        RoundShadowRelativeLayout shadowLayout;
        @Getter @InjectView(R.id.setting_theme_language_item_title_textview)    TextView titleTextView;
        @Getter @InjectView(R.id.setting_theme_language_item_detail_textview)   TextView detailTextView;
        @Getter @InjectView(R.id.setting_theme_language_item_check_imageview)   ImageView checkImageView;
        @Getter @InjectView(R.id.setting_theme_language_item_lock_imageview)    ImageView lockImageView;

        public MNSettingThemeLanguageItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
