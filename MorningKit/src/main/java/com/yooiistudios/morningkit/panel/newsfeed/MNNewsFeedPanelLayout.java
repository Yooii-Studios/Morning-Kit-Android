package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNRssFetchTask;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 */
public class MNNewsFeedPanelLayout extends MNPanelLayout {
    private static final String TAG = MNNewsFeedPanelLayout.class.getName();

    public static final String PREF_NEWS_FEED = "news feed preference";
    public static final String KEY_FEED_URL = "feed url";
    public static final String KEY_LOADING_FEED_URL = "loading feed url";
    public static final String KEY_RSS_FEED = "rss feed";
    public static final String KEY_RSS_ITEMS = "rss items";
    private static final int NEWS_FEED_HANDLER_DELAY = 8000;
    private static final int INVALID_NEWS_IDX = -1;

    // views
    private AutoResizeTextView newsFeedTextView;

    // object data
    private String feedUrl;
    private String loadingFeedUrl;
    private RssFeed feed;
    private int newsIdx;

    private MNRssFetchTask rssFetchTask;
    private boolean isHandlerRunning;
    private boolean needToLoad;

    public MNNewsFeedPanelLayout(Context context) {
        super(context);
    }

    public MNNewsFeedPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        newsFeedTextView = new AutoResizeTextView(getContext());
        newsFeedTextView.setGravity(Gravity.CENTER);

        // 방향에 따라 최초 사이즈를 약간 다르게 주기
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            newsFeedTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.panel_quotes_default_font_size_port));
        } else {
            newsFeedTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.panel_quotes_default_font_size_land));
        }
        newsFeedTextView.setOnResizeListener(new AutoResizeTextView.OnTextResizeListener() {
            @Override public void onTextResize(TextView textView, float oldSize,
                                               float newSize) {}

            @Override
            public void onEllipsisAdded(TextView textView) {
                //TODO apply theme when ellipsis added.
//                MNThemeType currentThemeType = MNTheme.getCurrentThemeType(
//                        getContext().getApplicationContext());
//                int textColor = MNMainColors.getQuoteContentTextColor
//                        (currentThemeType,
//                                getContext().getApplicationContext());
//
//                newsFeedTextView.setTextColor(textColor);
            }
        });

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.panel_quotes_padding);
        lp.setMargins(margin, margin, margin, margin);

        getContentLayout().addView(newsFeedTextView, lp);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);

        if (getPanelDataObject().has(KEY_LOADING_FEED_URL)) {
            loadingFeedUrl = getPanelDataObject().getString(KEY_LOADING_FEED_URL);
        }
        else {
            loadingFeedUrl = null;
        }
        if (loadingFeedUrl != null) {
            loadNewsFeed(loadingFeedUrl);
        }
        else {
            if (getPanelDataObject().has(KEY_FEED_URL)) {
                feedUrl = getPanelDataObject().getString(KEY_FEED_URL);
            } else {
                feedUrl = prefs.getString(KEY_FEED_URL,
                        MNNewsFeedUtil.getDefaultFeedUrl(getContext()));
                getPanelDataObject().put(KEY_FEED_URL, feedUrl);
            }
            //메인에서 이전 피드 캐싱해서 보여주던 루틴 없엠.(피드 url이 바뀐 경우 의미 없음)
            if (getPanelDataObject().has(KEY_RSS_FEED)
                    && getPanelDataObject().has(KEY_RSS_ITEMS)) {
                String feedStr = getPanelDataObject().getString(KEY_RSS_FEED);
                String newsListStr = getPanelDataObject().getString(KEY_RSS_ITEMS);

                if (feedStr != null && newsListStr != null) {
                    Type type = new TypeToken<RssFeed>() {
                    }.getType();
                    RssFeed rssFeed = new Gson().fromJson(feedStr, type);
                    type = new TypeToken<ArrayList<RssItem>>() {
                    }.getType();
                    rssFeed.setRssItems(
                            (ArrayList<RssItem>) new Gson().fromJson(newsListStr, type));

                    setNewRssFeed(feedUrl, rssFeed);
                } else {
                    feed = null;
                }
            } else {
                feed = null;
            }

            loadNewsFeed(feedUrl);
        }
    }
    private void loadNewsFeed(String url) {
        startLoadingAnimation();
        stopHandler();
        loadingFeedUrl = url;
        rssFetchTask = new MNRssFetchTask(new MNRssFetchTask.OnFetchListener() {
            @Override
            public void onFetch(RssFeed rssFeed) {
                stopHandler();
                setNewRssFeed(loadingFeedUrl, rssFeed);
                try {
                    archivePanelData_();
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                updateUI();
            }

            @Override
            public void onCancel() {
                Log.i("RSS Reader", "task cancelled.");

                loadingFeedUrl = null;
                updateUI();
            }

            @Override
            public void onError() {
                Log.i("RSS Reader", "error occurred while fetching rss feed.");

                loadingFeedUrl = null;
                updateUI();
            }
        });
        rssFetchTask.execute(url);
        startLoadingAnimation();
    }

    private void setNewRssFeed(String url, RssFeed feed) {
        this.feedUrl = url;
        this.loadingFeedUrl = null;
        this.feed = feed;
        newsIdx = 0;
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        showCurrentFeed();
    }
    private void showCurrentFeed() {
        if (feed != null && feed.getRssItems().size() > 0) {
            if (!isHandlerRunning) {
                // if handler is not running, news won't be shown. So show
                // next news forcefully.
                showNextNews();
            }
            startHandler();
        }
        else {
            newsFeedTextView.setText("blah...news feed unavailable.");
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        super.archivePanelData();

        MNLog.i(TAG, "archivePanelData");
    }

    private void archivePanelData_() throws JSONException {
        super.archivePanelData();

        if (loadingFeedUrl == null) {
            getPanelDataObject().remove(KEY_LOADING_FEED_URL);
        }
        getPanelDataObject().put(KEY_RSS_FEED,
                MNNewsFeedUtil.getRssFeedJsonString(feed));
        getPanelDataObject().put(KEY_RSS_ITEMS,
                MNNewsFeedUtil.getRssItemArrayListString(feed.getRssItems()));
    }

    @Override
    public void applyTheme() {
        super.applyTheme();

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(
                getContext().getApplicationContext());
        int subFontColor = MNMainColors.getSubFontColor(currentThemeType,
                getContext().getApplicationContext());

        newsFeedTextView.setTextColor(subFontColor);
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();

//        showCurrentFeed();
        if (needToLoad) {
            try {
                processLoading();
                needToLoad = false;
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityPause() {
        super.onActivityPause();
//        stopHandler();

        if (getPanelDataObject().has(KEY_LOADING_FEED_URL)) {
            needToLoad = true;
        }
        if (rssFetchTask != null) {
            rssFetchTask.cancel(false);
        }
    }

    private void startHandler() {
        if (isHandlerRunning) {
            return;
        }
        isHandlerRunning = true;
        newsHandler.sendEmptyMessageDelayed(0, NEWS_FEED_HANDLER_DELAY);
    }

    private void stopHandler() {
        if (!isHandlerRunning) {
            return;
        }
        isHandlerRunning = false;
        newsHandler.removeMessages(0);
    }
    private void showNextNews() {
        if (newsIdx >= feed.getRssItems().size()) {
            newsIdx = 0;
        }
        newsFeedTextView.setText(
                feed.getRssItems().get(newsIdx++).getTitle());
    }

    private MNQuotesHandler newsHandler = new MNQuotesHandler();
    private class MNQuotesHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (MNTutorialManager.isTutorialShown(getContext().getApplicationContext())) {
                Animation hideAnimation = AnimationUtils.loadAnimation(
                        getContext().getApplicationContext(), R.anim.quotes_hide);
                if (hideAnimation != null && newsFeedTextView != null) {
                    hideAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // 뉴스 갱신
                            showNextNews();
                            // 다시 보여주기
                            Animation showAnimation = AnimationUtils.loadAnimation(
                                    getContext().getApplicationContext(), R.anim.quotes_show);

                            if (showAnimation != null) {
                                newsFeedTextView.startAnimation(showAnimation);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    newsFeedTextView.startAnimation(hideAnimation);
                }

                // tick의 동작 시간을 계산해서 정확히 1초마다 UI 갱신을 요청할 수 있게 구현
                newsHandler.sendEmptyMessageDelayed(0, NEWS_FEED_HANDLER_DELAY);
            }
        }
    }
}
