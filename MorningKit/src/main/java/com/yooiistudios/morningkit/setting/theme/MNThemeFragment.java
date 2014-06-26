package com.yooiistudios.morningkit.setting.theme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.memory.ViewUnbindHelper;
import com.yooiistudios.morningkit.setting.store.iab.SKIabProducts;
import com.yooiistudios.morningkit.setting.theme.alarmstatusbar.MNAlarmStatusBarIconActivity;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageActivity;
import com.yooiistudios.morningkit.setting.theme.panelmatrix.MNPanelMatrixActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeDetailActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 6.
 *
 * MNThemeFragment
 *  세팅 - 테마 프래그먼트
 */
public class MNThemeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MNThemeFragment";
    public static final int REQ_THEME_DETAIL = 9385;

    @InjectView(R.id.setting_theme_listview) ListView listView;

    // Admob
    @InjectView(R.id.setting_theme_adview) AdView adView;
    private View footerView;

    // 이전에 생성된 프래그먼트를 유지
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_theme_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);
            // 애드몹 대응을 위한 FooterView, setAdapter 전에 호출 필요
            footerView = LayoutInflater.from(getActivity().getApplicationContext())
                    .inflate(R.layout.alarm_pref_list_footer_view, null, false);
            listView.addFooterView(footerView);

            listView.setAdapter(new MNThemeListAdapter(getActivity()));
            listView.setOnItemClickListener(this);
        }
        return rootView;
    }

    private void initAdView() {
        List<String> owndedSkus = SKIabProducts.loadOwnedIabProducts(getActivity().getApplicationContext());
        // 풀버전은 NO_ADS 포함
        if (owndedSkus.contains(SKIabProducts.SKU_NO_ADS)) {
            adView.setVisibility(View.GONE);
            if (footerView != null) {
                listView.removeFooterView(footerView);
            }
        } else {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // FooterView 를 넣어줘서 Wrapping 이 됨
//        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)((HeaderViewListAdapter)listView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        // 상점 탭에서 구매하고 올 경우를 대비
        initAdView();

        listView.setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
        getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(MNTheme.getCurrentThemeType(getActivity())));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MNThemeItemType themeItemType = MNThemeItemType.valueOf(i);
        switch (themeItemType) {
            case THEME:
                startActivityForResult(new Intent(getActivity(), MNThemeDetailActivity.class), REQ_THEME_DETAIL);
                break;

            case LANGUAGE:
                startActivity(new Intent(getActivity(), MNLanguageActivity.class));
                break;

            case PANEL_MATRIX:
                startActivity(new Intent(getActivity(), MNPanelMatrixActivity.class));
                break;

            case ALARM_STATUS_BAR:
                startActivity(new Intent(getActivity(), MNAlarmStatusBarIconActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_THEME_DETAIL && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        }
    }
}
