package com.yooiistudios.morningkit.panel.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.MNPanel;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.flickr.detail.MNFlickrDetailFragment;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerInterface;
import com.yooiistudios.morningkit.panel.selectpager.MNPanelSelectPagerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MNPanelDetailActivity extends ActionBarActivity implements MNPanelSelectPagerInterface {

    @InjectView(R.id.panel_detail_select_pager_layout)
    MNPanelSelectPagerLayout panelSelectPagerLayout;
    MNPanelDetailFragment panelDetailFragment;
    Menu actionBarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_detail);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            // 인텐트에서 패널 데이터를 가져오기
            Intent intent = getIntent();
            JSONObject panelDataObject;
            try {
                panelDataObject = new JSONObject(intent.getStringExtra(MNPanel.PANEL_DATA_OBJECT));
                MNPanelType panelType = MNPanelType.valueOfUniqueId(panelDataObject.getInt(MNPanel.PANEL_UNIQUE_ID));
                MNLog.now(panelDataObject.toString());
                MNLog.now("panelType: " + panelType);

                // 패널 타입을 확인해 프래그먼트 생성
                switch (panelType) {
                    case FLICKR:
                        panelDetailFragment = new MNFlickrDetailFragment();
                        break;
                    default:
                        panelDetailFragment = new MNFlickrDetailFragment();
                        break;
                }

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

    }

    @Override
    public void onPanelSelectPagerUnlockItemClick(int position) {

    }

    @Override
    public void onPanelSelectPagerStoreItemClick(int position) {

    }
}
