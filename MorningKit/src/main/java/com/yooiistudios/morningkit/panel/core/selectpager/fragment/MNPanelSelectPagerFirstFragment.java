package com.yooiistudios.morningkit.panel.core.selectpager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.panel.core.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingResources;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.ArrayList;

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
public class MNPanelSelectPagerFirstFragment extends Fragment {

    private static final String TAG = "MNPanelSelectPagerFirstFragment";

    @Setter MNPanelSelectPagerInterface panelSelectPagerInterface;
    // MNPanelSelectPagerInterface을 셋 해줘야함 non-default constructor 를 사용하지 않기 위함
    public MNPanelSelectPagerFirstFragment() {}

    @Getter ArrayList<RelativeLayout> selectItemLayouts;
    @Getter ArrayList<TextView> textViews;
    @InjectView(R.id.panel_selector_page1_1_item_layout) RelativeLayout selectItemLayout_1_1;
    @InjectView(R.id.panel_selector_page1_2_item_layout) RelativeLayout selectItemLayout_1_2;
    @InjectView(R.id.panel_selector_page1_3_item_layout) RelativeLayout selectItemLayout_1_3;
    @InjectView(R.id.panel_selector_page1_4_item_layout) RelativeLayout selectItemLayout_1_4;
    @InjectView(R.id.panel_selector_page1_5_item_layout) RelativeLayout selectItemLayout_1_5;
    @InjectView(R.id.panel_selector_page1_6_item_layout) RelativeLayout selectItemLayout_1_6;

    @InjectView(R.id.panel_selector_page1_1_textview) TextView textView1_1;
    @InjectView(R.id.panel_selector_page1_2_textview) TextView textView1_2;
    @InjectView(R.id.panel_selector_page1_3_textview) TextView textView1_3;
    @InjectView(R.id.panel_selector_page1_4_textview) TextView textView1_4;
    @InjectView(R.id.panel_selector_page1_5_textview) TextView textView1_5;
    @InjectView(R.id.panel_selector_page1_6_textview) TextView textView1_6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_select_pager_page1, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            initSelectItemLayouts();
            initTextViews();
        }
        return rootView;
    }

    private void initTextViews() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        textView1_1.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView1_2.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView1_3.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView1_4.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView1_5.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));
        textView1_6.setTextColor(MNSettingColors.getSubFontColor(currentThemeType));

        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        } else {
            textViews.clear();
        }
        textViews.add(textView1_1);
        textViews.add(textView1_2);
        textViews.add(textView1_3);
        textViews.add(textView1_4);
        textViews.add(textView1_5);
        textViews.add(textView1_6);
    }

    private void initSelectItemLayouts() {
        if (selectItemLayouts == null) {
            selectItemLayouts = new ArrayList<RelativeLayout>();
        } else {
            selectItemLayouts.clear();
        }
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        selectItemLayout_1_1.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_1_2.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_1_3.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_1_4.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_1_5.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));
        selectItemLayout_1_6.setBackgroundResource(MNSettingResources.getNormalItemResourcesId(currentThemeType));

        selectItemLayouts.add(selectItemLayout_1_1);
        selectItemLayouts.add(selectItemLayout_1_2);
        selectItemLayouts.add(selectItemLayout_1_3);
        selectItemLayouts.add(selectItemLayout_1_4);
        selectItemLayouts.add(selectItemLayout_1_5);
        selectItemLayouts.add(selectItemLayout_1_6);

        int i = 0;
        for (RelativeLayout selectItemLayout : selectItemLayouts) {
            selectItemLayout.setTag(i);
            i++;

            selectItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (panelSelectPagerInterface != null) {
                        panelSelectPagerInterface.onPanelSelectPagerItemClick((Integer) v.getTag());
                    }
                }
            });
        }
    }
}
