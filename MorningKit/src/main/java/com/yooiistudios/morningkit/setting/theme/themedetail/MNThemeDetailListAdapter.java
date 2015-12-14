package com.yooiistudios.morningkit.setting.theme.themedetail;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.MNSettingThemeDetailItemViewHolder;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.photo.MNThemePhotoActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNLanguageListAdapter
 */
public class MNThemeDetailListAdapter extends BaseAdapter {
    private MNThemeDetailActivity activity;
    private Fragment fragment;
    private boolean hasFrontCamera = true;
    private boolean hasBackCamera = true;
    private int totalNumberOfThemes;

    @SuppressWarnings("unused")
    private MNThemeDetailListAdapter() {}
    public MNThemeDetailListAdapter(MNThemeDetailActivity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;

        // theme check(Camera)
        totalNumberOfThemes = MNThemeType.values().length - 1;
        //noinspection deprecation
        switch (Camera.getNumberOfCameras()) {
            // 0일 경우는 getNumberOfCameras 에 대응을 안하는 안드로이드 기기도 있기에(Htc 등) 다시 한번 체크를 해 주어야만 한다
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

//            viewHolder.getOuterLayout().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
//            viewHolder.getTitleTextView().setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
//            try {
//                viewHolder.getCheckImageView().setImageResource(MNSettingResources.getCheckResourceId(currentThemeType));
//                viewHolder.getLockImageView().setImageResource(MNSettingResources.getLockResourceId(currentThemeType));
//            } catch (OutOfMemoryError error) {
//                error.printStackTrace();
//            }
//            viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getItemSelectorResourcesId(currentThemeType));

            // onClick
            viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MNSound.isSoundOn(activity)) {
                        MNSoundEffectsPlayer.play(R.raw.effect_view_close, activity);
                    }

                    MNThemeType newThemeType = MNThemeType.valueOf(convertedPosition);
                    if (newThemeType == MNThemeType.TRANQUILITY_BACK_CAMERA ||
                            newThemeType == MNThemeType.REFLECTION_FRONT_CAMERA) {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                                == PackageManager.PERMISSION_GRANTED) {
                            MNTheme.setThemeType(newThemeType, activity);
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
                        } else {
                            activity.setPendingTheme(newThemeType);
                            activity.requestCameraPermission();
                        }
                    } else {
                        MNTheme.setThemeType(newThemeType, activity);

                        if (newThemeType == MNThemeType.PHOTO) {
                            Intent intent = new Intent(activity, MNThemePhotoActivity.class);
                            fragment.startActivityForResult(intent,
                                    MNThemeDetailFragment.REQ_THEME_DETAIL_PHOTO);
                        } else {
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
                        }
                    }
                }
            });

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
                    viewHolder.getInnerLayout().setBackgroundResource(MNSettingResources.getLockItemResourcesId(currentThemeType));

                    // lock onClickListener
                    viewHolder.getInnerLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MNUnlockActivity.class);
                            if (selectedThemeType == MNThemeType.CELESTIAL_SKY_BLUE) {
                                intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY, SKIabProducts.SKU_CELESTIAL);

                                // 플러리
                                Map<String, String> params = new HashMap<>();
                                params.put(MNFlurry.CALLED_FROM, "Sky Blue");
                                FlurryAgent.logEvent(MNFlurry.UNLOCK, params);

                            } else if (selectedThemeType == MNThemeType.MODERNITY_WHITE) {

                                intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY, SKIabProducts.SKU_MODERNITY);

                                // 플러리
                                Map<String, String> params = new HashMap<>();
                                params.put(MNFlurry.CALLED_FROM, "Classic White");
                                FlurryAgent.logEvent(MNFlurry.UNLOCK, params);
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
