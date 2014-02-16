package com.yooiistudios.morningkit.panel.selectpager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.common.shadow.factory.MNShadowLayoutFactory;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 4.
 *
 * MNPanelSelectFragment
 *  설정 - 패널 탭에서 패널을 선택할 수 있는 뷰 페이저의 프래그먼트
 */
public class MNPanelSelectPagerFirstFragment extends Fragment {

    private static final String TAG = "MNPanelSelectPagerFirstFragment";

    MNPanelSelectPagerInterface panelSelectPagerInterface;
    public MNPanelSelectPagerFirstFragment() {}
    public MNPanelSelectPagerFirstFragment(MNPanelSelectPagerInterface panelSelectPagerInterface) {
        this.panelSelectPagerInterface = panelSelectPagerInterface;
    }

    @InjectView(R.id.widget_selector_page1_line1_layout) LinearLayout line1_Layout;
    @InjectView(R.id.widget_selector_page1_line2_layout) LinearLayout line2_Layout;

    @Getter ArrayList<RoundShadowRelativeLayout> roundShadowRelativeLayouts;
    @InjectView(R.id.widget_selector_page1_1_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_1;
    @InjectView(R.id.widget_selector_page1_2_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_2;
    @InjectView(R.id.widget_selector_page1_3_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_3;
    @InjectView(R.id.widget_selector_page1_4_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_4;
    @InjectView(R.id.widget_selector_page1_5_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_5;
    @InjectView(R.id.widget_selector_page1_6_shadow_layout) RoundShadowRelativeLayout roundShadowRelativeLayout_1_6;

    @InjectView(R.id.widget_selector_page1_1_textview) TextView textView1_1;
    @InjectView(R.id.widget_selector_page1_2_textview) TextView textView1_2;
    @InjectView(R.id.widget_selector_page1_3_textview) TextView textView1_3;
    @InjectView(R.id.widget_selector_page1_4_textview) TextView textView1_4;
    @InjectView(R.id.widget_selector_page1_5_textview) TextView textView1_5;
    @InjectView(R.id.widget_selector_page1_6_textview) TextView textView1_6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_select_pager_page1, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            initShadowLayouts();
            initTextViews();
        }
        return rootView;
    }

    private void initTextViews() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        textView1_1.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        textView1_2.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        textView1_3.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        textView1_4.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        textView1_5.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
        textView1_6.setTextColor(MNSettingColors.getMainFontColor(currentThemeType));
    }

    private void initShadowLayouts() {
        // 순서대로 들어가기에 뒤부터 넣어줌
        roundShadowRelativeLayout_1_6 = setShadowLayout(roundShadowRelativeLayout_1_6, line2_Layout, 5);
        roundShadowRelativeLayout_1_5 = setShadowLayout(roundShadowRelativeLayout_1_5, line2_Layout, 4);
        roundShadowRelativeLayout_1_4 = setShadowLayout(roundShadowRelativeLayout_1_4, line2_Layout, 3);
        roundShadowRelativeLayout_1_3 = setShadowLayout(roundShadowRelativeLayout_1_3, line1_Layout, 2);
        roundShadowRelativeLayout_1_2 = setShadowLayout(roundShadowRelativeLayout_1_2, line1_Layout, 1);
        roundShadowRelativeLayout_1_1 = setShadowLayout(roundShadowRelativeLayout_1_1, line1_Layout, 0);

        if (roundShadowRelativeLayouts == null) {
            roundShadowRelativeLayouts = new ArrayList<RoundShadowRelativeLayout>();
        } else {
            roundShadowRelativeLayouts.clear();
        }
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_1);
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_2);
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_3);
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_4);
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_5);
        roundShadowRelativeLayouts.add(roundShadowRelativeLayout_1_6);
    }

    private RoundShadowRelativeLayout setShadowLayout(RoundShadowRelativeLayout roundShadowRelativeLayout, LinearLayout layout, int index) {
        // 기존 동적 생성 방식에서 색값만 변경하게 구현
//        roundShadowRelativeLayout = MNShadowLayoutFactory.changeShadowLayoutWithChildren(roundShadowRelativeLayout, layout);
        MNShadowLayoutFactory.changeThemeOfShadowLayout(roundShadowRelativeLayout, getActivity());
        roundShadowRelativeLayout.setTag(index);
        roundShadowRelativeLayout.setPressedColor(MNSettingColors.getForwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
        setShadowOnClickListener(roundShadowRelativeLayout);
        return roundShadowRelativeLayout;
    }

    private void setShadowOnClickListener(RoundShadowRelativeLayout roundShadowRelativeLayout) {
        roundShadowRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelSelectPagerInterface.onPanelSelectPagerItemClick((Integer)v.getTag());
            }
        });
    }
}
