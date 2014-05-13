package com.yooiistudios.morningkit.panel.core.selectpager.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.common.unlock.MNUnlockActivity;
import com.yooiistudios.morningkit.panel.core.MNPanelType;
import com.yooiistudios.morningkit.panel.core.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.setting.store.MNStoreActivity;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 4.
 *
 * MNPanelSelectFragment
 *  설정 - 패널 탭에서 패널을 선택할 수 있는 뷰 페이저의 프래그먼트
 */
public class MNPanelSelectPagerSecondFragment extends Fragment {

    private static final String TAG = "MNPanelSelectPagerSecondFragment";

    @Setter MNPanelSelectPagerInterface panelSelectPagerInterface;
    // MNPanelSelectPagerInterface을 셋 해줘야함 non-default constructor 를 사용하지 않기 위함
    public MNPanelSelectPagerSecondFragment() {}

    @Getter ArrayList<RelativeLayout> selectItemLayouts;
    @Getter ArrayList<TextView> textViews;
    @InjectView(R.id.panel_selector_page2_1_item_layout) RelativeLayout selectItemLayout_2_1;
    @InjectView(R.id.panel_selector_page2_2_item_layout) RelativeLayout selectItemLayout_2_2;
    @InjectView(R.id.panel_selector_page2_3_item_layout) RelativeLayout selectItemLayout_2_3;
    @InjectView(R.id.panel_selector_page2_4_item_layout) RelativeLayout selectItemLayout_2_4;
    @InjectView(R.id.panel_selector_page2_5_item_layout) RelativeLayout selectItemLayout_2_5;
    @InjectView(R.id.panel_selector_page2_6_item_layout) RelativeLayout selectItemLayout_2_6;

    @InjectView(R.id.panel_selector_page2_1_textview) TextView textView2_1;
    @InjectView(R.id.panel_selector_page2_2_textview) TextView textView2_2;
    @InjectView(R.id.panel_selector_page2_3_textview) TextView textView2_3;
    @InjectView(R.id.panel_selector_page2_4_textview) TextView textView2_4;
    @InjectView(R.id.panel_selector_page2_5_textview) TextView textView2_5;
//    @InjectView(R.id.widget_selector_page2_6_textview) TextView textView2_6; // No panel yey

    @InjectView(R.id.panel_selector_page2_2_lock_imageview) ImageView lockImageView2_2;
    @InjectView(R.id.panel_selector_page2_3_lock_imageview) ImageView lockImageView2_3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_select_pager_page2, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        initShadowLayouts();
        initTextViews();
        checkPanelLockStates();
    }

    private void initTextViews() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        textView2_1.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView2_2.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView2_3.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView2_4.setTextColor(MNSettingColors.getSubFontColor(currentThemeType)); // Store
        textView2_5.setTextColor(MNSettingColors.getStorePointedFontColor(currentThemeType)); // No panel
//        textView2_6.setTextColor(MNSettingColors.getMainFontColor(currentThemeType)); // No panel

        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        } else {
            textViews.clear();
        }
        textViews.add(textView2_1);
        textViews.add(textView2_2);
        textViews.add(textView2_3);
        textViews.add(textView2_4);
        textViews.add(textView2_5);
    }

    private void initShadowLayouts() {
        if (selectItemLayouts == null) {
            selectItemLayouts = new ArrayList<RelativeLayout>();
        } else {
            selectItemLayouts.clear();
        }
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        selectItemLayout_2_1.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_2_2.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_2_3.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_2_4.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_2_5.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_2_6.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));

        selectItemLayouts.add(selectItemLayout_2_1);
        selectItemLayouts.add(selectItemLayout_2_2);
        selectItemLayouts.add(selectItemLayout_2_3);
        selectItemLayouts.add(selectItemLayout_2_4);
        selectItemLayouts.add(selectItemLayout_2_5);
        selectItemLayouts.add(selectItemLayout_2_6);

        int index = 6;
        for (RelativeLayout selectItemLayout : selectItemLayouts) {
            // 스토어 뒤의 아이템은 -1처리, 스토어는 -2처리, 나머지는 index + 6
            int convertedIndex = index;
            if (index > MNPanelType.STORE.getIndex()) {
                convertedIndex = -1;
            } else if (index == MNPanelType.STORE.getIndex()) {
                convertedIndex = -2;
            }
            selectItemLayout.setTag(convertedIndex);

            if (convertedIndex > 0) {
                // 보통 패널 아이템
                selectItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (panelSelectPagerInterface != null) {
                            panelSelectPagerInterface.onPanelSelectPagerItemClick((Integer) v.getTag());
                        }
                    }
                });
            } else if (convertedIndex == -2) {
                // 스토어
                selectItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (panelSelectPagerInterface != null) {
                            panelSelectPagerInterface.onPanelSelectPagerStoreItemClick((Integer) v.getTag());
                            startStoreActivity(v.getContext());
                        }
                    }
                });
            } else {
                // 빈칸
                selectItemLayout.setOnClickListener(null);
                selectItemLayout.setClickable(false);
                selectItemLayout.setFocusable(false);
            }
            index++;
        }
    }

    private void checkPanelLockStates() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());

        // 구매 확인을 하고, 구매가 되지 않았을 경우 background 색 변경, lockImageView 표시
        List<String> ownedSkus = SKIabProducts.loadOwnedIabProducts(getActivity());

        if (ownedSkus.indexOf(SKIabProducts.SKU_MEMO) == -1) {
            lockImageView2_2.setImageResource(MNSettingResources.getPanelSelectPagerLockResourceId(currentThemeType));
            lockImageView2_2.setVisibility(View.VISIBLE);
            textView2_2.setTextColor(MNSettingColors.getLockedFontColor(currentThemeType));
            selectItemLayout_2_2.setBackgroundResource(MNSettingResources.getLockItemResourcesId(currentThemeType));
//            selectItemLayout_2_2.setSolidAreaColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));
//            selectItemLayout_2_2.setPressedColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));
            selectItemLayout_2_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    panelSelectPagerInterface.onPanelSelectPagerUnlockItemClick((Integer) v.getTag());
                    startUnlockActivity(SKIabProducts.SKU_MEMO, v.getContext());
                }
            });
        } else {
            lockImageView2_2.setVisibility(View.INVISIBLE);
        }
        if (ownedSkus.indexOf(SKIabProducts.SKU_DATE_COUNTDOWN) == -1) {
            lockImageView2_3.setVisibility(View.VISIBLE);
            lockImageView2_3.setImageResource(MNSettingResources.getPanelSelectPagerLockResourceId(currentThemeType));
            textView2_3.setTextColor(MNSettingColors.getLockedFontColor(currentThemeType));
            selectItemLayout_2_3.setBackgroundResource(MNSettingResources.getLockItemResourcesId(currentThemeType));
//            selectItemLayout_2_3.setSolidAreaColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));
//            selectItemLayout_2_3.setPressedColor(MNSettingColors.getLockedBackgroundColor(currentThemeType));
            selectItemLayout_2_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    panelSelectPagerInterface.onPanelSelectPagerUnlockItemClick((Integer) v.getTag());
                    startUnlockActivity(SKIabProducts.SKU_DATE_COUNTDOWN, v.getContext());
                }
            });
        } else {
            lockImageView2_3.setVisibility(View.INVISIBLE);
        }
    }

    private void startUnlockActivity(String sku, Context context) {
        if (MNSound.isSoundOn(context)) {
            MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
        }

        Intent intent = new Intent(getActivity(), MNUnlockActivity.class);
        intent.putExtra(MNUnlockActivity.PRODUCT_SKU_KEY, sku);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);
    }

    private void startStoreActivity(Context context) {
        if (MNSound.isSoundOn(context)) {
            MNSoundEffectsPlayer.play(R.raw.effect_view_open, context);
        }

        Intent intent = new Intent(getActivity(), MNStoreActivity.class);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_modal_up, R.anim.activity_hold);
    }
}
