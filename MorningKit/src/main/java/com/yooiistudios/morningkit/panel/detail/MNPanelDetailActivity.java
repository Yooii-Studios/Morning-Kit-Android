package com.yooiistudios.morningkit.panel.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    @InjectView(R.id.panel_detail_select_pager_layout) MNPanelSelectPagerLayout panelSelectPagerLayout;

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
                MNPanelDetailFragment panelDetailFragment;
                switch (panelType) {
                    case FLICKR:
                        panelDetailFragment = new MNFlickrDetailFragment();
                        break;
                    default:
                        panelDetailFragment = new MNPanelDetailFragment();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
