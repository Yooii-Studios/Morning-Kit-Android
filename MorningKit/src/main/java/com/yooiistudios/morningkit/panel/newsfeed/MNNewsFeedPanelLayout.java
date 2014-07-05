package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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

import nl.matshofman.saxrssreader.RssFeed;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 */
public class MNNewsFeedPanelLayout extends MNPanelLayout {
    private static final String TAG = MNNewsFeedPanelLayout.class.getName();

    public static final String PREF_NEWS_FEED = "news feed preference";
    public static final String KEY_FEED_URL = "feed url";
    public static final String KEY_RSS_FEED = "rss feed";
    public static final String KEY_RSS_ITEMS = "rss items";
    private static final int NEWS_FEED_HANDLER_DELAY = 8000;
    private static final int INVALID_NEWS_IDX = -1;

    // views
    private AutoResizeTextView newsFeedTextView;

    // object data
    private String feedUrl;
    private RssFeed feed;
    private int newsIdx;

    private MNRssFetchTask rssFetchTask;
    private boolean isHandlerRunning;

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
//        LayoutParams lp = new LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        int margin = getResources().getDimensionPixelSize(R.dimen.panel_quotes_padding);
//        lp.setMargins(margin, margin, margin, margin);
//        newsFeedTextView.setLayoutParams(quoteTextViewLayoutParams);
        newsFeedTextView.setGravity(Gravity.CENTER);
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

        getContentLayout().addView(newsFeedTextView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        SharedPreferences prefs = getContext().getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);

        if (getPanelDataObject().has(KEY_FEED_URL)) {
            feedUrl = getPanelDataObject().getString(KEY_FEED_URL);
        }
        else {
            feedUrl = prefs.getString(KEY_FEED_URL,
                    MNNewsFeedUtil.getDefaultFeedUrl(getContext()));
            getPanelDataObject().put(KEY_FEED_URL, feedUrl);
        }
        //메인에서 이전 피드 캐싱해서 보여주던 루틴 없엠.(피드 url이 바뀐 경우 의미 없음)
//        if (getPanelDataObject().has(KEY_RSS_FEED)
//                && getPanelDataObject().has(KEY_RSS_ITEMS)) {
//            String feedStr = getPanelDataObject().getString(KEY_RSS_FEED);
//            String newsListStr = getPanelDataObject().getString(KEY_RSS_ITEMS);
//
//            if (feedStr != null && newsListStr != null) {
//                Type type = new TypeToken<RssFeed>() {}.getType();
//                RssFeed rssFeed = new Gson().fromJson(feedStr, type);
//                type = new TypeToken<ArrayList<RssItem>>() {}.getType();
//                rssFeed.setRssItems(
//                        (ArrayList<RssItem>)new Gson().fromJson(newsListStr, type));
//
//                setNewRssFeed(rssFeed);
//            }
//            else {
//                feed = null;
//            }
//        }
//        else {
//            feed = null;
//        }

        loadNewsFeed(feedUrl);
    }
    private void loadNewsFeed(String url) {
        rssFetchTask = new MNRssFetchTask(new MNRssFetchTask.OnFetchListener() {
            @Override
            public void onFetch(RssFeed rssFeed) {
                setNewRssFeed(rssFeed);
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

                feed = null;
                updateUI();
            }

            @Override
            public void onError() {
                Log.i("RSS Reader", "error occurred while fetching rss feed.");

                feed = null;
                updateUI();
            }
        });
        rssFetchTask.execute(url);
    }

    private void setNewRssFeed(RssFeed feed) {
        this.feed = feed;
        newsIdx = 0;
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (feed != null && feed.getRssItems().size() > 0) {
            if (!isHandlerRunning) {
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
        updateUI();
    }

    @Override
    public void onActivityPause() {
        super.onActivityPause();
        stopHandler();

        if (rssFetchTask != null) {
            rssFetchTask.cancel(true);
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
