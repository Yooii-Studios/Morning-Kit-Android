package com.yooiistudios.morningkit.panel.newsfeed;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.yooiistudios.morningkit.MNApplication;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.analytic.MNAnalyticsUtils;
import com.yooiistudios.morningkit.common.locale.MNLocaleUtils;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.common.sound.MNSoundEffectsPlayer;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.ui.MNNewsFeedSelectDialogFragment;
import com.yooiistudios.morningkit.setting.theme.soundeffect.MNSound;

public class MNNewsSelectActivity extends ActionBarActivity
        implements MNNewsFeedSelectDialogFragment.OnClickListener {
    private static final String TAG = MNNewsSelectActivity.class.getSimpleName();
    public static final String TAG_FEED_SELECT_DIALOG = "feed select dialog";
    public static final String INTENT_KEY_URL = "intent_key_url";

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
        // TODO: 탭 이름 변경
        actionBar.setTitle(R.string.tab_setting);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.icon_actionbar_morning);

        mFeedUrl = (MNNewsFeedUrl)getIntent().getSerializableExtra(INTENT_KEY_URL);

        MNAnalyticsUtils.startAnalytics((MNApplication) getApplication(), TAG);
    }

    /**
     * Action Bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // TODO: 텍스트, 아이콘 변경
        getMenuInflater().inflate(R.menu.news_select_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (MNSound.isSoundOn(this)) {
                MNSoundEffectsPlayer.play(R.raw.effect_view_close, this);
            }
            finish();
        } else if (id == R.id.news_select_action_custom) {
            DialogFragment newFragment =
                    MNNewsFeedSelectDialogFragment.
                            newInstance(mFeedUrl);
            newFragment.show(getSupportFragmentManager(), TAG_FEED_SELECT_DIALOG);
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
        getIntent().putExtra(INTENT_KEY_URL, url);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onCancel() {}
}
