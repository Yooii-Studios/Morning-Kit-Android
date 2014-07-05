package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.panel.newsfeed.adapter.MNNewsFeedAdapter;
import com.yooiistudios.morningkit.panel.newsfeed.ui.MNNewsFeedSelectDialogFragment;
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
public class MNNewsFeedDetailFragment extends MNPanelDetailFragment
            implements MNNewsFeedSelectDialogFragment.OnClickListener{

    private static final String TAG = "MNNewsFeedDetailFragment";
    public static final String TAG_FEED_SELECT_DIALOG = "feed select dialog";

    @InjectView(R.id.feedTitle) TextView feedTitleTextView;
    @InjectView(R.id.newsList) ListView newsListView;
    @InjectView(R.id.result) LinearLayout newsResult;
    @InjectView(R.id.loadingImageView) ImageView loadingImageView;


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
        feedTitleTextView.setOnClickListener(onSelectFeedClickedListener);

        // theme
        initTheme();
    }
    private View.OnClickListener
            onSelectFeedClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DialogFragment newFragment =
                    MNNewsFeedSelectDialogFragment.
                            newInstance(feedUrl);
            newFragment.setTargetFragment(
                    MNNewsFeedDetailFragment.this, -1);
            newFragment.show(getFragmentManager(), TAG_FEED_SELECT_DIALOG);

        }
    };

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
    private void showResultView() {
        loadingImageView.setVisibility(View.GONE);
        newsResult.setVisibility(View.VISIBLE);
    }
    private void showLoadingImageView() {
        loadingImageView.setVisibility(View.VISIBLE);
        newsResult.setVisibility(View.GONE);
    }

    private void update(RssFeed feed) {
        this.feed = feed;
        if (feed != null && feed.getRssItems().size() > 0) {
            feedTitleTextView.setText(feed.getTitle());

            MNNewsFeedAdapter adapter = new MNNewsFeedAdapter(getActivity(), feed);
            newsListView.setAdapter(adapter);
            newsListView.setVisibility(View.VISIBLE);
        }
        else {
            feedTitleTextView.setText("blah...feed unavailable");
            newsListView.setVisibility(View.GONE);
        }
        showResultView();
    }

    private void loadNewsFeed(String url) {
        showLoadingImageView();
        feedUrl = url;
        rssFetchTask = new MNRssFetchTask(new MNRssFetchTask.OnFetchListener() {
            @Override
            public void onFetch(RssFeed rssFeed) {
                update(rssFeed);
            }

            @Override
            public void onCancel() {
                MNLog.i("RSS Reader", "task cancelled.");

                update(null);
            }

            @Override
            public void onError() {
                MNLog.i("RSS Reader", "error occurred while fetching rss feed" +
                        ".");

                update(null);
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

        if (rssFetchTask != null) {
            rssFetchTask.cancel(true);
        }
    }


    @Override
    public void onConfirm(String url) {
        loadNewsFeed(url);
    }

    @Override
    public void onCancel() {}
}
