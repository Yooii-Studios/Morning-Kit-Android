package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.MNSettingThemeDetailItemViewHolder;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.photo.MNThemePhotoActivity;

import java.util.List;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNLanguageListAdapter
 */
public class MNThemeDetailListAdapter extends BaseAdapter {
    private Activity activity;
    private boolean hasFrontCamera = true;
    private boolean hasBackCamera = true;
    private int totalNumberOfThemes;

    private MNThemeDetailListAdapter() {}
    public MNThemeDetailListAdapter(Activity activity) {
        this.activity = activity;

        // theme check(Camera)
        totalNumberOfThemes = MNThemeType.values().length;
        switch (Camera.getNumberOfCameras()) {
            // 0일 경우는 getNumberOfCameras에 대응을 안하는 안드로이드 기기도 있기에(Htc 등) 다시 한번 체크를 해 주어야만 한다
            case 0:
                if (activity.getPackageManager() != null) {
                    if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                            activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                            hasFrontCamera = false;
                            totalNumberOfThemes -= 1;
                        }
                        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                            hasBackCamera = false;
                            totalNumberOfThemes -= 1;
                        }
                    } else {
                        hasFrontCamera = false;
                        hasBackCamera = false;
                        totalNumberOfThemes -= 2;
                    }
                }
                break;

            case 1:
                hasFrontCamera = false;
                hasBackCamera = false;
                if (activity.getPackageManager() != null) {
                    if (activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                        hasFrontCamera = true;
                    } else {
                        hasBackCamera = true;
                    }
                }
                totalNumberOfThemes -= 1;
                break;

            case 2:
                hasFrontCamera = true;
                hasBackCamera = true;
                break;
        }

        // test
//        hasBackCamera = false;
//        hasFrontCamera = true;
//        return totallNumberOfThemes - 1;
    }

    @Override
    public int getCount() {
        return totalNumberOfThemes;
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

        int offset;

        // 첫번째 워터릴리는 그대로 둠
        if (position == 0) {
            offset = 0;
        } else {
            if (hasBackCamera && hasFrontCamera) {
                offset = 0;
            } else if (!hasBackCamera && !hasFrontCamera) {
                offset = 2;
            } else {
                if (hasBackCamera) {
                    offset = MNThemeType.TRANQUILITY_BACK_CAMERA.getIndex()-1;
                } else {
                    offset = MNThemeType.REFLECTION_FRONT_CAMERA.getIndex()-1;
                }
            }
        }

        final int convertedPosition = position + offset;

        convertView = LayoutInflater.from(activity).inflate(R.layout.setting_theme_detail_item, parent, false);

        if (convertView != null) {
            MNSettingThemeDetailItemViewHolder viewHolder = new MNSettingThemeDetailItemViewHolder(convertView);

            viewHolder.getTitleTextView().setText(MNThemeType.toString(convertedPosition, activity));

            final MNThemeType selectedThemeType = MNThemeType.valueOf(convertedPosition);

            // check
            if (selectedThemeType != MNTheme.getCurrentThemeType(activity)) {
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
                        MNTheme.setThemeType(MNThemeType.valueOf(convertedPosition), activity);

                        if (MNTheme.getCurrentThemeType(activity) == MNThemeType.PHOTO) {
                            activity.startActivity(new Intent(activity, MNThemePhotoActivity.class));
                        } else {
                            activity.finish();
                        }
                    }
                });
            } else {
                throw new AssertionError("shadowRelativeLayout must not be null!");
            }

            // lock
            if (selectedThemeType != MNThemeType.CELESTIAL_SKY_BLUE && selectedThemeType != MNThemeType.MODERNITY_WHITE) {
                viewHolder.getLockImageView().setVisibility(View.GONE);
            } else {
                List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(activity);
                if ((selectedThemeType == MNThemeType.MODERNITY_WHITE && ownedSkus.contains(SKIabProducts.SKU_MODERNITY)) ||
                        (selectedThemeType == MNThemeType.CELESTIAL_SKY_BLUE && ownedSkus.contains(SKIabProducts.SKU_CELESTIAL))) {
                    // 아이템 구매완료
                    viewHolder.getLockImageView().setVisibility(View.GONE);
                } else {
                    // 아이템 잠김
                    roundShadowRelativeLayout.setSolidAreaColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));
                    roundShadowRelativeLayout.setPressedColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));

                    // lock onClickListener
                    roundShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MNUnlockActivity.class);
                            if (selectedThemeType == MNThemeType.CELESTIAL_SKY_BLUE) {
                                intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY, SKIabProducts.SKU_CELESTIAL);
                            } else if (selectedThemeType == MNThemeType.MODERNITY_WHITE) {
                                intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY, SKIabProducts.SKU_MODERNITY);
                            } else {
                                throw new AssertionError("ProductId is not included");
                            }
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
