package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.newsfeed.adapter.MNNewsFeedAdapter;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNNewsFeedUtil;
import com.yooiistudios.morningkit.panel.newsfeed.util.MNRssFetchTask;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_FEED_URL;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_RSS_FEED;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.KEY_RSS_ITEMS;
import static com.yooiistudios.morningkit.panel.newsfeed.MNNewsFeedPanelLayout.PREF_NEWS_FEED;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 */
public class MNNewsFeedDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNNewsFeedDetailFragment";

    @InjectView(R.id.feedTitle)
    TextView feedTitleTextView;
    @InjectView(R.id.newsList) ListView newsListView;

    // object data
    private String feedUrl;
    private RssFeed feed;

    private MNRssFetchTask rssFetchTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.panel_news_feed_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            try {
                initPanelDataObject();
            } catch (JSONException e) {
                e.printStackTrace();

                // init with default setting
                feedUrl = MNNewsFeedUtil.getDefaultFeedUrl(getActivity());
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
        if (getPanelDataObject().has(KEY_FEED_URL)) {
            feedUrl = getPanelDataObject().getString(KEY_FEED_URL);
        }
        else {
            feedUrl = prefs.getString(KEY_FEED_URL,
                    MNNewsFeedUtil.getDefaultFeedUrl(getActivity()));
        }
        if (getPanelDataObject().has(KEY_RSS_FEED)
                && getPanelDataObject().has(KEY_RSS_ITEMS)) {
            String feedStr = getPanelDataObject().getString(KEY_RSS_FEED);
            String newsListStr = getPanelDataObject().getString(KEY_RSS_ITEMS);

            if (feedStr != null && newsListStr != null) {
                Type type = new TypeToken<RssFeed>() {}.getType();
                feed = new Gson().fromJson(feedStr, type);
                type = new TypeToken<ArrayList<RssItem>>() {}.getType();
                feed.setRssItems(
                        (ArrayList<RssItem>)new Gson().fromJson(newsListStr, type));
            }
            else {
                feed = null;
            }
        }
        else {
            feed = null;
        }
    }

    private void initUI() {

        // theme
        initTheme();
    }

    private void initTheme() {
        MNThemeType currentThemeType =
                MNTheme.getCurrentThemeType(getActivity());
    }

    @Override
    protected void archivePanelData() throws JSONException {
        getPanelDataObject().put(KEY_FEED_URL, feedUrl);

        getPanelDataObject().put(KEY_RSS_FEED,
                MNNewsFeedUtil.getRssFeedJsonString(feed));
        getPanelDataObject().put(KEY_RSS_ITEMS,
                MNNewsFeedUtil.getRssItemArrayListString(feed.getRssItems()));

        SharedPreferences prefs = getActivity().getSharedPreferences(
                PREF_NEWS_FEED, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_FEED_URL, feedUrl).commit();
    }

    private void update(RssFeed feed) {
        feedTitleTextView.setText(feed.getTitle());

        MNNewsFeedAdapter adapter = new MNNewsFeedAdapter(getActivity(), feed);
        newsListView.setAdapter(adapter);
    }

    private void loadNewsFeed(String url) {
        rssFetchTask = new MNRssFetchTask(new MNRssFetchTask.OnFetchListener() {
            @Override
            public void onFetch(RssFeed rssFeed) {
//                ArrayList<RssItem> rssItems = rssFeed.getRssItems();
//                StringBuilder content = new StringBuilder();
//                for (RssItem rssItem : rssItems) {
//                    Log.i("RSS Reader", rssItem.getTitle());
//
//                    content.append(rssItem.getTitle()).append("\n")
//                            .append(rssItem.getDescription()).append("\n");
//                }
                feed = rssFeed;

                update(rssFeed);
            }

            @Override
            public void onCancel() {
                Log.i("RSS Reader", "task cancelled.");
            }

            @Override
            public void onError() {
                Log.i("RSS Reader", "error occurred while fetching rss feed.");
            }
        });
        rssFetchTask.execute(url);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (feed != null) {
            update(feed);
        }
        else {
            loadNewsFeed(feedUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //TODO stop handler
        if (rssFetchTask != null) {
            rssFetchTask.cancel(true);
        }
    }

}
