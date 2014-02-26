package com.yooiistudios.morningkit.panel.detail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerAdapter;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerLayout;
import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerFirstFragment;
import com.yooiistudios.morningkit.panel.selectpager.fragment.MNPanelSelectPagerSecondFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNPanelDetailActivity extends ActionBarActivity implements MNPanelSelectPagerInterface {

    private static final String TAG = "MNPanelDetailActivity";

    @InjectView(R.id.panel_detail_select_pager_layout)
    MNPanelSelectPagerLayout panelSelectPagerLayout;
    MNPanelDetailFragment panelDetailFragment;
    int panelIndex;
    Menu actionBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_detail);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            // 인텐트에서 패널 데이터를 가져오기
            Intent intent = getIntent();
            panelIndex = intent.getIntExtra(MNPanel.PANEL_INDEX, 0);
            JSONObject panelDataObject;
            try {
                panelDataObject = new JSONObject(intent.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
                MNPanelType panelType = MNPanelType.valueOfUniqueId(panelDataObject.getInt(MNPanel.PANEL_UNIQUE_ID));

                initActionBar(panelType);

                // 패널 타입을 확인해 프래그먼트 생성
                panelDetailFragment = MNPanelDetailFragment.newInstance(panelType, panelIndex);

                // 데이터를 프래그먼트에 넣어주기
                panelDetailFragment.setPanelDataObject(panelDataObject);

                // 패널셀렉트페이저 로딩
                panelSelectPagerLayout.loadPanelSelectPager(getSupportFragmentManager(), this);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.panel_detail_fragment_container, panelDetailFragment)
                        .commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initActionBarTitle(MNPanelType panelType) {
        String panelTypeName = MNPanelType.toString(panelType.getIndex(), this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setTitle(panelTypeName);
            }
        }else{
            getSupportActionBar().setTitle(panelTypeName); // 추후 구현 다시 하자
        }
    }

    private void initActionBar(MNPanelType panelType) {
        initActionBarTitle(panelType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setDisplayShowHomeEnabled(false); // 추후 구현 다시 하자
            }
        }else{
            getSupportActionBar().setDisplayShowHomeEnabled(false); // 추후 구현 다시 하자
        }
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.pref_actions, menu);
        actionBarMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.pref_action_ok:
                // 변경사항 저장
                try {
                    panelDetailFragment.archivePanelData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 메인 액티비티에 리프레시 요청
                Intent intent = new Intent();
                intent.putExtra(MNPanel.PANEL_DATA_OBJECT,
                        panelDetailFragment.getPanelDataObject().toString());
                setResult(RESULT_OK, intent);
                finish();
                return true;

            case R.id.pref_action_cancel:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Panel Select Pager
     */
    @Override
    public void onPanelSelectPagerItemClick(int position) {

        // 선택한 패널 타입 체크
        int panelUniqueId = -1;
        try {
            if (panelDetailFragment.getPanelDataObject().has(MNPanel.PANEL_UNIQUE_ID)) {
                panelUniqueId = panelDetailFragment.getPanelDataObject().getInt(MNPanel.PANEL_UNIQUE_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (panelUniqueId != -1) {
            int previousPanelTypeIndex = MNPanelType.valueOfUniqueId(panelUniqueId).getIndex();

            if (previousPanelTypeIndex != position) {
                MNLog.i(TAG, "panel should be changed");

                // title
                initActionBarTitle(MNPanelType.valueOf(position));

                // panelSelectPager forward 색상 변경
                setColorOfPanelSelectPager(position, previousPanelTypeIndex);

                // fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                panelDetailFragment = MNPanelDetailFragment.newInstance(MNPanelType.valueOf(position), panelIndex);
                transaction.replace(R.id.panel_detail_fragment_container, panelDetailFragment);
                transaction.commit();
            } else {
                MNLog.i(TAG, "panel type is same, not changed");
            }
        }
    }

    private void setColorOfPanelSelectPager(int position, int previousPanelTypeIndex) {
        ViewPager panelSelectPager = panelSelectPagerLayout.getPanelSelectPager();
        MNPanelSelectPagerAdapter panelSelectPagerAdapter
                = (MNPanelSelectPagerAdapter) panelSelectPagerLayout.getPanelSelectPager().getAdapter();

        MNPanelSelectPagerFirstFragment firstFragment
                = (MNPanelSelectPagerFirstFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 0);
        MNPanelSelectPagerSecondFragment secondFragment
                = (MNPanelSelectPagerSecondFragment) panelSelectPagerAdapter.getActiveFragment(panelSelectPager, 1);

        // 새로 선택된 레이아웃
        RoundShadowRelativeLayout selectedShadowRelativeLayout
                = getShadowRoundLayout(position, firstFragment, secondFragment);

        if (selectedShadowRelativeLayout != null) {
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(this);
            selectedShadowRelativeLayout.setSolidAreaColor(MNSettingColors.getGuidedPanelColor(currentThemeType));
        }

        // 기존의 레이아웃
        RoundShadowRelativeLayout previouslySelectedShadowRelativeLayout
                = getShadowRoundLayout(previousPanelTypeIndex, firstFragment, secondFragment);

        if (previouslySelectedShadowRelativeLayout != null) {
            MNThemeType currentThemeType = MNTheme.getCurrentThemeType(this);
            previouslySelectedShadowRelativeLayout.setSolidAreaColor(MNSettingColors.getForwardBackgroundColor(currentThemeType));
        }
    }

    private RoundShadowRelativeLayout getShadowRoundLayout(int position,
                                                           MNPanelSelectPagerFirstFragment firstFragment,
                                                           MNPanelSelectPagerSecondFragment secondFragment) {
        RoundShadowRelativeLayout shadowRelativeLayout;
        if (position >= 0 && position < 6) {
            // 페이지 1
            return firstFragment.getRoundShadowRelativeLayouts().get(position);
        } else {
            // 페이지 2
            int offset = firstFragment.getRoundShadowRelativeLayouts().size();
            return secondFragment.getRoundShadowRelativeLayouts().get(position - offset);
        }
    }

    @Override
    public void onPanelSelectPagerUnlockItemClick(int position) {
    }

    @Override
    public void onPanelSelectPagerStoreItemClick(int position) {
    }
}
