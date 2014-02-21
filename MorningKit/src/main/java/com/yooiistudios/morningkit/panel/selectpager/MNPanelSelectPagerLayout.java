package com.yooiistudios.morningkit.panel.selectpager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 2. 4.
 *
 * MNPanelSelectPagerLayout
 *  iOS의 위젯 셀렉터에 대응하는 ViewGroup - 재활용을 위해 리팩토링함
 */
public class MNPanelSelectPagerLayout extends RelativeLayout {

    @Getter @InjectView(R.id.panel_select_view_pager) ViewPager panelSelectPager;
    @Getter @InjectView(R.id.panel_select_page_indicator)
    CirclePageIndicator pageIndicator;

    public MNPanelSelectPagerLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNPanelSelectPagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNPanelSelectPagerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        if (getContext() != null) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.panel_select_pager_layout, this, true);
            if (v != null) {
                ButterKnife.inject(this, v);
            }
        }
    }

    public void loadPanelSelectPager(FragmentManager fragmentManager, MNPanelSelectPagerInterface panelSelectPagerInterface) {
        // getChildFragmentManager() 가 아니면 이 프래그먼트가 다시 보일때 panelSelectPager가 보이지 않음
        // http://stackoverflow.com/questions/6672066/fragment-inside-fragment
        panelSelectPager.setAdapter(new MNPanelSelectPagerAdapter(fragmentManager, panelSelectPagerInterface));

        // page indicator
        pageIndicator.setViewPager(panelSelectPager);
    }

    public void applyTheme() {
        panelSelectPager.getAdapter().notifyDataSetChanged(); // 이렇게 하면 ViewPager의 live fragment를 얻을 수 없다

        pageIndicator.setFillColor(MNSettingColors.getCurrentPanelSelectIndicatorColor(MNTheme.getCurrentThemeType(getContext())));
        pageIndicator.setPageColor(MNSettingColors.getDefaultPanelSelectIndicatorColor(MNTheme.getCurrentThemeType(getContext())));
    }
}
