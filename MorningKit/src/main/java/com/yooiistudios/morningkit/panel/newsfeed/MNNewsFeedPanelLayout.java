package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsProviderLanguage;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUrlProvider;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNRssFetchTask;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Random;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 *
 * MNNewsFeedPanelLayout
 */
public class MNNewsFeedPanelLayout extends MNPanelLayout {
//    private static final String TAG = MNNewsFeedPanelLayout.class.getName();

    public static final String PREF_NEWS_FEED = "news feed preference";
    public static final String KEY_FEED_URL = "feed url";
    public static final String KEY_LOADING_FEED_URL = "loading feed url";
    public static final String KEY_RSS_FEED = "rss feed";
    public static final String KEY_RSS_ITEMS = "rss items";
    public static final String KEY_DISPLAYING_NEWS = "displaying news";
    private static final int NEWS_FEED_HANDLER_DELAY = 5000;
    private static final int NEWS_FEED_ANIMATION_DURATION = 250;
    private static final int NEWS_FEED_ANIMATION_FADE_DURATION = 200;
//    private static final int INVALID_NEWS_IDX = -1;

    // views
    private AutoResizeTextView newsFeedTextView;

    // object data
    private MNNewsFeedUrl feedUrl;
    private MNNewsFeedUrl loadingFeedUrl;
    private RssFeed feed;
    private RssItem currentDisplayingItem;
    private ArrayList<RssItem> shuffledRssItemList;
    private int newsIdx;

    private MNRssFetchTask rssFetchTask;
    private MNLanguageType previousLanguageType;
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
        LinkedHashMap<String, MNNewsProviderLanguage> newsProviderLanguages
                = MNNewsFeedUrlProvider.getInstance(getContext()).getUrlsSortedByLocale();
        StringBuilder messageBuilder = new StringBuilder();
        for (String languageRegionCode : newsProviderLanguages.keySet()) {
            messageBuilder.append(languageRegionCode).append(", ");
        }
        MNLog.i("UrlsSortedByLocale", messageBuilder.toString());

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
                MNThemeType currentThemeType = MNTheme.getCurrentThemeType(
                        getContext().getApplicationContext());
                int textColor = MNMainColors.getQuoteContentTextColor
                        (currentThemeType,
                                getContext().getApplicationContext());

                newsFeedTextView.setTextColor(textColor);
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
        Context context = getContext().getApplicationContext();
        Type urlType = new TypeToken<MNNewsFeedUrl>(){}.getType();

        if (getPanelDataObject().has(KEY_LOADING_FEED_URL)) {
//            loadingFeedUrl = getPanelDataObject().getString(KEY_LOADING_FEED_URL);
            loadingFeedUrl = new Gson().fromJson(getPanelDataObject().getString
                    (KEY_LOADING_FEED_URL), urlType);

            if (!loadingFeedUrl.type.equals(MNNewsFeedUrlType.CUSTOM)) {
                loadingFeedUrl = MNNewsFeedUrlProvider.getInstance(context).getDefault();
                getPanelDataObject().put(KEY_LOADING_FEED_URL,
                        new Gson().toJson(loadingFeedUrl));
                prefs.edit().putString(KEY_LOADING_FEED_URL,
                        new Gson().toJson(loadingFeedUrl)).apply();
            }
        }
        else {
            loadingFeedUrl = null;
        }
        if (loadingFeedUrl != null) {
            loadNewsFeed(loadingFeedUrl);
        }
        else {
            if (getPanelDataObject().has(KEY_FEED_URL)) {
//                feedUrl = getPanelDataObject().getString(KEY_FEED_URL);
                feedUrl = new Gson().fromJson(
                        getPanelDataObject().getString(KEY_FEED_URL), urlType);

//                if (!feedUrl.type.equals(MNNewsFeedUrlType.CUSTOM)) {
//                    feedUrl = MNNewsFeedUrlProvider.getInstance(context).getDefault();
//                    getPanelDataObject().put(KEY_FEED_URL,
//                            new Gson().toJson(feedUrl));
//                    prefs.edit().putString(KEY_FEED_URL,
//                            new Gson().toJson(feedUrl)).apply();
//                }
            }
            else {
                String savedUrl = prefs.getString(KEY_FEED_URL, null);
                if (savedUrl != null) {
                    feedUrl = new Gson().fromJson(savedUrl, urlType);
                }
                else {
                    feedUrl = MNNewsFeedUrlProvider.getInstance(context).getDefault();
                }
//                feedUrl = prefs.getString(KEY_FEED_URL,
//                        MNNewsFeedUtil.getDefaultFeedUrl(getContext()));
//                getPanelDataObject().put(KEY_FEED_URL,
//                        new Gson().toJson(feedUrl));
//                prefs.edit().putString(KEY_FEED_URL,
//                        new Gson().toJson(feedUrl)).apply();
            }
            //메인에서 이전 피드 캐싱해서 보여주던 루틴 없엠.(피드 url 이 바뀐 경우 의미 없음)
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
                    ArrayList<RssItem> savedNewsList = new Gson().fromJson
                            (newsListStr, type);
                    rssFeed.setRssItems(savedNewsList);

                    setNewRssFeed(feedUrl, rssFeed);
                }
                else {
                    feed = null;
                }
            }
            else {
                feed = null;
            }

            loadNewsFeed(feedUrl);
        }
    }
    private void loadNewsFeed(MNNewsFeedUrl url) {
        startLoadingAnimation();
        stopHandler();
        loadingFeedUrl = url;

        if (rssFetchTask != null) {
            rssFetchTask.cancel(false);
        }

        rssFetchTask = new MNRssFetchTask(getContext().getApplicationContext(),
                url, new MNRssFetchTask.OnFetchListener() {
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
        // 앞 큐에 있는 AsyncTask 가 막힐 경우 뒷 쓰레드가 되게 하기 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            rssFetchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            rssFetchTask.execute();
        }
        startLoadingAnimation();
    }

    private void setNewRssFeed(MNNewsFeedUrl url, RssFeed feed) {
        this.feedUrl = url;
        this.loadingFeedUrl = null;
        this.feed = feed;
        newsIdx = 0;

        shuffledRssItemList = new ArrayList<RssItem>(feed.getRssItems());
        Collections.shuffle(shuffledRssItemList, new Random(System.nanoTime()));
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        showCurrentFeed();
    }
    private void showCurrentFeed() {
        if (feed != null && feed.getRssItems().size() > 0) {
            hideCoverLayout();

            if (!isHandlerRunning) {
                // if handler is not running, news won't be shown. So show
                // next news forcefully.
                showNextNews();
            }
            startHandler();
        }
        else {
            showCoverLayout(R.string.news_feed_feed_unavailable);
//            newsFeedTextView.setText("blah...news feed unavailable.");
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        super.archivePanelData();
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
        if (previousLanguageType == null) {
            previousLanguageType = MNLanguage.getCurrentLanguageType(getContext());
            // 디폴트를 사용하고 있다면 해당 언어에 따른 url 을 동적으로 가져온다
        } else {
            MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getContext());
            if (previousLanguageType != currentLanguageType) {
                // 언어가 바뀔 때 이곳으로 오는데, 새로 바뀐 언어에 대한 url 을 가져와서 로딩한다
                try {
                    processLoading();
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                previousLanguageType = currentLanguageType;
            }
        }

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(
                getContext().getApplicationContext());

        if (currentDisplayingItem != null) {
            String[] result = MNNewsFeedUtil.getTitleAndPublisherName(
                    currentDisplayingItem, feedUrl.type);
//            if (result[1] != null) {
//                newsFeedTextView.setText(result[0] + "\n\n" + result[1]);
//            } else {
//                newsFeedTextView.setText(currentDisplayingItem.getTitle());
//            }


            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

            SpannableString contentString = new SpannableString(result[0]);
            contentString.setSpan(
                    new ForegroundColorSpan(MNMainColors.getQuoteContentTextColor(currentThemeType, getContext().getApplicationContext())),
                    0, contentString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(contentString);

            // if publisher available
            String publisher = result[1] != null ? result[1] : feed.getTitle();
            if (publisher != null) {
                SpannableString emptyString = new SpannableString("\n\n");
                emptyString.setSpan(new RelativeSizeSpan(0.4f), 0, emptyString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.append(emptyString);
                emptyString = new SpannableString("\n");
                emptyString.setSpan(new RelativeSizeSpan(0.6f), 0,
                        emptyString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.insert(0, emptyString);

                SpannableString publisherString = new SpannableString(publisher);

                publisherString.setSpan(
                        new ForegroundColorSpan(MNMainColors.getQuoteAuthorTextColor(currentThemeType, getContext().getApplicationContext())),
                        0, publisherString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // 크기 좀 더 작게 표시
                publisherString.setSpan(new RelativeSizeSpan(0.65f),
                        0, publisherString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                publisherString.setSpan(new AlignmentSpan.Standard(
                        Layout.Alignment.ALIGN_OPPOSITE),
                        0, publisherString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                stringBuilder.append(publisherString);
//                newsFeedTextView.setText(result[0] + "\n\n" + result[1]);
            }

            newsFeedTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    protected void onPanelClick() {
        getPanelDataObject().remove(KEY_DISPLAYING_NEWS);
        if (currentDisplayingItem != null) {
            try {
                getPanelDataObject().put(KEY_DISPLAYING_NEWS,
                        feed.getRssItems().indexOf(currentDisplayingItem));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPanelClick();
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
        if (newsIdx >= shuffledRssItemList.size()) {
            newsIdx = 0;
        }
        currentDisplayingItem = shuffledRssItemList.get(newsIdx++);

        applyTheme();
    }

    private MNNewsHandler newsHandler = new MNNewsHandler();
    private class MNNewsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 튜토리얼이 끝난 후부터 뉴스 갱신
            if (MNTutorialManager.isTutorialShown(getContext().getApplicationContext())) {
                AnimationSet hideSet = new AnimationSet(true);
                hideSet.setInterpolator(new AccelerateInterpolator());

                Animation moveUpAnim = new TranslateAnimation
                        (Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, -0.1f);
                moveUpAnim.setDuration(NEWS_FEED_ANIMATION_DURATION);
                moveUpAnim.setFillEnabled(true);
                moveUpAnim.setFillAfter(true);

                hideSet.addAnimation(moveUpAnim);

                Animation fadeoutAnim = new AlphaAnimation(1.0f, 0.0f);
                fadeoutAnim.setDuration(NEWS_FEED_ANIMATION_FADE_DURATION);
                fadeoutAnim.setFillEnabled(true);
                fadeoutAnim.setFillAfter(true);
                hideSet.addAnimation(fadeoutAnim);

                if (newsFeedTextView != null) {
                    hideSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // 뉴스 갱신
                            showNextNews();

                            // 다시 보여주기
                            AnimationSet showSet = new AnimationSet(false);
                            showSet.setInterpolator(new DecelerateInterpolator());

                            Animation moveDownAnim = new TranslateAnimation
                                    (Animation.RELATIVE_TO_SELF, 0.0f,
                                            Animation.RELATIVE_TO_SELF, 0.0f,
                                            Animation.RELATIVE_TO_SELF, 0.1f,
                                            Animation.RELATIVE_TO_SELF, 0.0f);
                            moveDownAnim.setDuration(NEWS_FEED_ANIMATION_DURATION);
                            moveDownAnim.setFillEnabled(true);
                            moveDownAnim.setFillAfter(true);

                            showSet.addAnimation(moveDownAnim);

                            Animation fadeInAnim = new AlphaAnimation(0.0f, 1.0f);
                            fadeInAnim.setDuration(NEWS_FEED_ANIMATION_FADE_DURATION);
                            fadeInAnim.setFillEnabled(true);
                            fadeInAnim.setFillAfter(true);
                            showSet.addAnimation(fadeInAnim);
                            newsFeedTextView.startAnimation(showSet);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    newsFeedTextView.startAnimation(hideSet);
                }
            }
            // tick 의 동작 시간을 계산해서 정확히 1초마다 UI 갱신을 요청할 수 있게 구현
            newsHandler.sendEmptyMessageDelayed(0, NEWS_FEED_HANDLER_DELAY);
        }
    }
}
