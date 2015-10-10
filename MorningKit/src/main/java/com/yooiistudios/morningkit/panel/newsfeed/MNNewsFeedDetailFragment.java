package com.yooiistudios.morningkit.panel.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.newsfeed.adapter.MNNewsFeedAdapter;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUrlProvider;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNRssFetchTask;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_DISPLAYING_NEWS;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_FEED_URL;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_LOADING_FEED_URL;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_RSS_FEED;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_RSS_ITEMS;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.PREF_NEWS_FEED;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 *
 * MNNewsFeedDetailFragment
 */
public class MNNewsFeedDetailFragment extends MNPanelDetailFragment {
    private static final int RC_NEWS_SELECT = 1001;

    // Animation
    private static final String KEY_HAS_CLICKED_NEWS_SELECT_BUTTON = "key_has_clicked_news_select_button";
    private static final float START_SCALE = 1.0f;
    private static final float END_SCALE = 1.2f;
    private static final long ANIM_DURATION = 150;
    private static final int TOTAL_ANIM_COUNT = 4;
    private static final int ANIM_REPEAT_COUNT = TOTAL_ANIM_COUNT - 1;

    @InjectView(R.id.feedTitle) TextView feedTitleTextView;
    @InjectView(R.id.news_feed_detail_globe) ImageView newsSelectButton;
    @InjectView(R.id.newsList) ListView newsListView;
    @InjectView(R.id.result) LinearLayout newsResult;
    @InjectView(R.id.loadingImageView) ImageView loadingImageView;

    // Object data
    private MNNewsFeedUrl feedUrl;
    private MNNewsFeedUrl loadingFeedUrl;
    private RssFeed feed;

    private MNRssFetchTask rssFetchTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_news_feed_detail_fragment, container, false);
        if (rootView != null && savedInstanceState == null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

                // init with default setting
                feedUrl = MNNewsFeedUrlProvider.getInstance(getActivity()).getDefault();
                loadingFeedUrl = null;
                feed = null;
            }

            // UI
            initUI();
        }
        return rootView;
    }

    private void initPanelDataObject() throws JSONException {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);
        Type urlType = new TypeToken<MNNewsFeedUrl>(){}.getType();

        if (getPanelDataObject().has(KEY_FEED_URL)) {
            feedUrl = new Gson().fromJson(getPanelDataObject().getString(KEY_FEED_URL), urlType);
        } else {
            String savedUrlJsonStr = prefs.getString(KEY_FEED_URL, null);
            if (savedUrlJsonStr != null) {
                feedUrl = new Gson().fromJson(savedUrlJsonStr, urlType);
            } else {
                feedUrl = MNNewsFeedUrlProvider.getInstance(getActivity()).getDefault();
            }
        }

        if (getPanelDataObject().has(KEY_LOADING_FEED_URL)) {
            loadingFeedUrl = new Gson().fromJson(getPanelDataObject()
                    .getString(KEY_LOADING_FEED_URL), urlType);
        } else {
            loadingFeedUrl = null;
        }

        feed = null;
        if (getPanelDataObject().has(KEY_RSS_FEED)
                && getPanelDataObject().has(KEY_RSS_ITEMS)) {
            String feedStr = getPanelDataObject().getString(KEY_RSS_FEED);
            String newsListStr = getPanelDataObject().getString(KEY_RSS_ITEMS);

            if (feedStr != null && newsListStr != null) {
                Type type = new TypeToken<RssFeed>() {}.getType();
                feed = new Gson().fromJson(feedStr, type);
                type = new TypeToken<ArrayList<RssItem>>() {}.getType();
                ArrayList<RssItem> rssItems = new Gson().fromJson(newsListStr, type);
                feed.setRssItems(rssItems);
                if (getPanelDataObject().has(KEY_DISPLAYING_NEWS)) {
                    int idx = getPanelDataObject().getInt(
                            KEY_DISPLAYING_NEWS);
                    if (idx < feed.getRssItems().size() && idx >= 0) {
                        RssItem item = feed.getRssItems().remove(idx);
                        feed.getRssItems().add(0, item);
                    }
                }
            }
        }
    }

    private void initUI() {
        feedTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getLink()));
                    startActivity(intent);
                } catch (NullPointerException e) {
                    Crashlytics.getInstance().core.logException(e);
                    Crashlytics.getInstance().core.log("feedUrl: " + feedUrl.toString());
                    Crashlytics.getInstance().core.log("loadingFeedUrl: " + loadingFeedUrl.toString());
                }
            }
        });
        loadingImageView.setVisibility(View.GONE);
        newsResult.setVisibility(View.GONE);

        initGlobeImageView();
    }

    private void initGlobeImageView() {
        newsSelectButton.setOnClickListener(onSelectFeedClickedListener);

        SharedPreferences prefs = getActivity().getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);
        boolean hasClicked = prefs.getBoolean(KEY_HAS_CLICKED_NEWS_SELECT_BUTTON, false);
        if (!hasClicked) {
            animateNewsSelectIcon();
        }
    }

    private void animateNewsSelectIcon() {
        newsSelectButton.postDelayed(new Runnable() {
            @Override
            public void run() {

                final ScaleAnimation scaleUp = new ScaleAnimation(START_SCALE, END_SCALE, START_SCALE, END_SCALE,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleUp.setDuration(ANIM_DURATION);
                scaleUp.setRepeatCount(ANIM_REPEAT_COUNT);
                scaleUp.setRepeatMode(Animation.REVERSE);
                newsSelectButton.startAnimation(scaleUp);
            }
        }, 500);
    }

    private View.OnClickListener
            onSelectFeedClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences prefs = getActivity().getSharedPreferences(
                    PREF_NEWS_FEED, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(KEY_HAS_CLICKED_NEWS_SELECT_BUTTON, true).apply();
            newsSelectButton.clearAnimation();

            Intent intent = new Intent(getActivity(), MNNewsSelectActivity.class);
            intent.putExtra(MNNewsSelectActivity.INTENT_KEY_URL, feedUrl);
            startActivityForResult(intent, RC_NEWS_SELECT);
        }
    };

    @Override
    protected void archivePanelData() throws JSONException {
        if (loadingFeedUrl != null) {
            getPanelDataObject().put(KEY_LOADING_FEED_URL,
                    new Gson().toJson(loadingFeedUrl));
            feedUrl = loadingFeedUrl;
            feed = null;
        } else {
            getPanelDataObject().remove(KEY_LOADING_FEED_URL);
        }
        getPanelDataObject().put(KEY_FEED_URL, new Gson().toJson(feedUrl));

        if (feed == null || feed.getRssItems().size() == 0) {
            getPanelDataObject().remove(KEY_RSS_FEED);
            getPanelDataObject().remove(KEY_RSS_ITEMS);
        } else {
            getPanelDataObject().put(KEY_RSS_FEED,
                    MNNewsFeedUtil.getRssFeedJsonString(feed));
            getPanelDataObject().put(KEY_RSS_ITEMS,
                    MNNewsFeedUtil.getRssItemArrayListString(feed.getRssItems()));
        }

        MNNewsFeedUtil.saveNewsFeedUrl(getActivity(), feedUrl);
    }

    private void showResultView() {
        loadingImageView.setVisibility(View.GONE);
        newsResult.setVisibility(View.VISIBLE);
    }

    private void showLoadingImageView() {
        loadingImageView.setVisibility(View.VISIBLE);
        newsResult.setVisibility(View.GONE);
    }

    private void update(MNNewsFeedUrl url, RssFeed feed) {
        if (feed != null && feed.getRssItems().size() > 0) {
            this.feedUrl = url;
            this.loadingFeedUrl = null;
            this.feed = feed;

            String publisher = feedUrl.providerName != null && feedUrl.providerName.length() > 0
                    ? feedUrl.providerName : feed.getTitle();
            feedTitleTextView.setText(publisher);

            MNNewsFeedAdapter feedAdapter = new MNNewsFeedAdapter(getActivity(), feed);
            newsListView.setAdapter(feedAdapter);
            newsListView.setVisibility(View.VISIBLE);
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String link = MNNewsFeedDetailFragment.this.feed
                            .getRssItems().get(position).getLink();

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link));
                    startActivity(intent);
                }
            });
        }
        showResultView();
    }

    private void loadNewsFeed(MNNewsFeedUrl url) {
        cancelRssFetchTask();

        showLoadingImageView();
        loadingFeedUrl = url;
        rssFetchTask = new MNRssFetchTask(getActivity().getApplicationContext(),
                url, new MNRssFetchTask.OnFetchListener() {
            @Override
            public void onFetch(RssFeed rssFeed) {
                update(loadingFeedUrl, rssFeed);
            }

            @Override
            public void onCancel() {
                MNLog.i("RSS Reader", "task cancelled.");

                update(feedUrl, feed);
                showUnavailableMessage();
            }

            @Override
            public void onError() {
                MNLog.i("RSS Reader", "error occurred while fetching rss feed.");

                update(feedUrl, feed);
                showUnavailableMessage();
            }
        });

        // 앞 큐에 있는 AsyncTask 가 막힐 경우 뒷 쓰레드가 되게 하기 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            rssFetchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            rssFetchTask.execute();
        }
    }

    private void cancelRssFetchTask() {
        if (rssFetchTask != null) {
            rssFetchTask.cancel(true);
        }
    }

    private void showUnavailableMessage() {
        Toast.makeText(getActivity().getApplicationContext(),
                R.string.news_feed_invalid_url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (loadingFeedUrl != null) {
            loadNewsFeed(loadingFeedUrl);
        }
        else {
            if (feed != null) {
                update(feedUrl, feed);
            } else {
                loadNewsFeed(feedUrl);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelRssFetchTask();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_NEWS_SELECT) {
                MNNewsFeedUrl feedUrl = (MNNewsFeedUrl)data.getSerializableExtra(
                        MNNewsSelectActivity.INTENT_KEY_URL);
                loadNewsFeed(feedUrl);
            }
        }
    }
}
