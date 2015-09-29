package com.yooiistudios.morningkit.panel.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
import com.yooiistudios.morningkit.panel.newsfeed.adapter.MNNewsProviderLanguageAdapter;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderCountry;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;
import com.yooiistudios.morningkit.panel.newsfeed.ui.MNNewsFeedSelectDialogFragment;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUrlProvider;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MNNewsSelectActivity extends ActionBarActivity
        implements MNNewsFeedSelectDialogFragment.OnClickListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = MNNewsSelectActivity.class.getSimpleName();
    private static final int RC_NEWS_SELECT_DETAIL = 1001;

    public static final String TAG_FEED_SELECT_DIALOG = "feed select dialog";
    public static final String INTENT_KEY_URL = "intent_key_url";

    private MNNewsProviderLanguageAdapter mAdapter;

    private MNNewsFeedUrl mFeedUrl;

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

        setContentView(R.layout.activity_news_select);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.select_newsfeed_title);
        actionBar.setIcon(R.drawable.icon_actionbar_morning);

        initData();
        initViews();

        MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
    }

    private void initData() {
        mFeedUrl = (MNNewsFeedUrl)getIntent().getSerializableExtra(INTENT_KEY_URL);
    }

    private void initViews() {
        ListView listView = (ListView)findViewById(R.id.news_select_language_listview);

        ArrayList<MNNewsProviderLanguage> newsProviderLanguages =
                new ArrayList<MNNewsProviderLanguage>(
                        MNNewsFeedUrlProvider.getInstance(this).getUrlsSortedByLocale().values()
                );
        mAdapter = new MNNewsProviderLanguageAdapter(newsProviderLanguages, mFeedUrl);

        listView.setAdapter(mAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
    }

    private void finishWithNewsFeedUrl(MNNewsFeedUrl url) {
        getIntent().putExtra(INTENT_KEY_URL, url);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.news_select_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.news_select_action_custom:
                DialogFragment newFragment =
                        mFeedUrl.type.equals(MNNewsFeedUrlType.CUSTOM)
                                ? MNNewsFeedSelectDialogFragment.newInstance(mFeedUrl)
                                : MNNewsFeedSelectDialogFragment.newInstance();
                newFragment.show(getSupportFragmentManager(), TAG_FEED_SELECT_DIALOG);
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
    public void onConfirm(MNNewsFeedUrl url) {
        finishWithNewsFeedUrl(url);
    }

    @Override
    public void onCancel() {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MNNewsProviderLanguage newsProviderLanguage = mAdapter.getNewsProviderLanguageAt(position);

        LinkedHashMap<String, MNNewsProviderCountry> countries =
                newsProviderLanguage.newsProviderCountries;

        boolean isLanguageSimplifiedChineseAndClickEnglish = false;
        if (MNLanguage.getCurrentLanguageType(this) == MNLanguageType.SIMPLIFIED_CHINESE &&
                newsProviderLanguage.languageCode.equals("en")) {
            isLanguageSimplifiedChineseAndClickEnglish = true;
        }

        if (countries.size() == 1 || isLanguageSimplifiedChineseAndClickEnglish) {
            MNNewsProviderCountry newsProviderCountry =
                    new ArrayList<MNNewsProviderCountry>(countries.values()).get(0);

            // 예외 추가: 앱 언어가 중국이이며 영어를 클릭할 경우는 뉴욕타임즈 기사를 대신 보여줄 것. 구글은 중국에서 블럭됨
            if (isLanguageSimplifiedChineseAndClickEnglish) {
                newsProviderCountry.url = "http://www.nytimes.com/services/xml/rss/nyt/HomePage.xml";
                newsProviderCountry.newsProviderName = "New York Times";
            }
            MNNewsFeedUrl newsFeedUrl = new MNNewsFeedUrl(newsProviderCountry, MNNewsFeedUrlType.CURATION);
            finishWithNewsFeedUrl(newsFeedUrl);
        } else {
            Intent intent = new Intent(this, MNNewsSelectDetailActivity.class);
            intent.putExtra(MNNewsSelectDetailActivity.INTENT_KEY_NEWS_PROVIDER_LANGUAGE, newsProviderLanguage);
            intent.putExtra(MNNewsSelectActivity.INTENT_KEY_URL, mFeedUrl);
            startActivityForResult(intent, RC_NEWS_SELECT_DETAIL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_NEWS_SELECT_DETAIL) {
                MNNewsFeedUrl feedUrl = (MNNewsFeedUrl)data.getSerializableExtra(
                        MNNewsSelectActivity.INTENT_KEY_URL);
                finishWithNewsFeedUrl(feedUrl);
            }
        }
    }
}
