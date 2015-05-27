package com.yooiistudios.morningkit.panel.newsfeed;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.locale.MNLocaleUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.panel.newsfeed.adapter.MNNewsProviderCountryAdapter;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderCountry;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

public class MNNewsSelectDetailActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener {
    private static final String TAG = MNNewsSelectDetailActivity.class.getSimpleName();

    public static final String INTENT_KEY_NEWS_PROVIDER_LANGUAGE = "intent_key_news_provider_language";

    private MNNewsProviderCountryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 회전마다 Locale 을 새로 적용해줌(언어가 바뀌어 버리는 문제 해결)
        MNLocaleUtils.updateLocale(this);

        super.onCreate(savedInstanceState);

        // OS에 의해서 kill 당할 경우 복구하지 말고 메인 액티비티를 새로 띄워줌 - panelObject 와 관련된 오류 해결
        if (savedInstanceState != null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_news_select_detail);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        // TODO: 탭 이름 변경
        actionBar.setTitle(R.string.tab_setting);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon_actionbar_morning);

        initViews();

        MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
    }

    private void initViews() {
        ListView listView = (ListView)findViewById(R.id.news_select_detail_country_listview);

        MNNewsProviderLanguage newsProviderLanguage =
                (MNNewsProviderLanguage)getIntent().getSerializableExtra(
                        INTENT_KEY_NEWS_PROVIDER_LANGUAGE);
        MNNewsFeedUrl feedUrl =
                (MNNewsFeedUrl)getIntent().getSerializableExtra(MNNewsSelectActivity.INTENT_KEY_URL);
        mAdapter = new MNNewsProviderCountryAdapter(newsProviderLanguage, feedUrl);

        listView.setAdapter(mAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                if (MNSound.isSoundOn(this)) {
                    MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        // Activity visible to user
        super.onStart();
        FlurryAgent.onStartSession(this, MNFlurry.KEY);
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // Activity no longer visible
        super.onStop();
        FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MNNewsProviderCountry newsProviderCountry = mAdapter.getNewsProviderCountryAt(position);
        // TODO: type 체크
        MNNewsFeedUrl newsFeedUrl = new MNNewsFeedUrl(newsProviderCountry, MNNewsFeedUrlType.GOOGLE);

        getIntent().putExtra(MNNewsSelectActivity.INTENT_KEY_URL, newsFeedUrl);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
