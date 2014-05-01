package com.yooiistudios.morningkit.setting.theme.themedetail.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stevenkim.photo.SKBitmapLoader;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.io.FileNotFoundException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 24.
 *
 * MNThemePhotoListAdapter
 */
public class MNThemePhotoListAdapter extends BaseAdapter {
    private Activity activity;

    private MNThemePhotoListAdapter(){}
    public MNThemePhotoListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 2;
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
        convertView = LayoutInflater.from(activity).inflate(R.layout.setting_theme_detail_photo_item, parent, false);
        if (convertView != null) {
            MNThemePhotoItemViewHolder viewHolder = new MNThemePhotoItemViewHolder(convertView);

            Bitmap bitmap = null;

            // load a bitmap, adjust layoutParams to wrap/match
            if (position == 0) {
                try {
                    bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(activity, SKBitmapLoader.getPortraitImageUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                viewHolder.getTextView().setText(R.string.setting_theme_photo_portrait);
            } else {
                try {
                    bitmap = SKBitmapLoader.loadAutoScaledBitmapFromUri(activity, SKBitmapLoader.getLandscapeImageUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                viewHolder.getTextView().setText(R.string.setting_theme_photo_landscape);

            }
            viewHolder.getCropImageView().setImageBitmap(bitmap);

            // theme
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(activity);

            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            viewHolder.getTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
            viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

            // theme - shadow
//            RoundShadowRelativeLayout roundShadowRelativeLayout = (RoundShadowRelativeLayout) convertView.findViewById(viewHolder.getShadowLayout().getId());

            // 기존 동적 생성에서 색상 값만 바꾸어 주게 변경
//            RoundShadowRelativeLayout newShadowRelativeLayout = MNShadowLayoutFactory.changeShadowLayout(currentThemeType, roundShadowRelativeLayout, viewHolder.getOuterLayout());
//            MNShadowLayoutFactory.changeThemeOfShadowLayout(roundShadowRelativeLayout, activity);

            // onClick
            viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MNSound.isSoundOn(activity)) {
                        MNSoundEffectsPlayer.play(R.raw.effect_view_open, activity);
                    }
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.putExtra("outputformat", Bitmap.CompressFormat.PNG.name());
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // 사진 선택 기능으로 간다
                    // 기존에 로딩한 사진이 있다면 recycle 반드시 필요
                    if (position == 0) {
                        // Portrait
                        activity.startActivityForResult(intent, SKBitmapLoader.REQ_CODE_PICK_IMAGE_PORTRAIT);
                    } else {
                        // Landscape
                        activity.startActivityForResult(intent, SKBitmapLoader.REQ_CODE_PICK_IMAGE_LANDSCAPE);
                    }
                }
            });
        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNThemePhotoItemViewHolder {
        @Getter @InjectView(R.id.setting_theme_detail_photo_item_outer_layout)      RelativeLayout outerLayout;
        @Getter @InjectView(R.id.setting_theme_detail_photo_item_inner_layout)      RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_theme_detail_photo_item_crop_imageview)    ImageView cropImageView;
        @Getter @InjectView(R.id.setting_theme_detail_photo_item_divider_imageview) ImageView dividerImageView;
        @Getter @InjectView(R.id.setting_theme_detail_photo_item_textview)          TextView textView;

        public MNThemePhotoItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
