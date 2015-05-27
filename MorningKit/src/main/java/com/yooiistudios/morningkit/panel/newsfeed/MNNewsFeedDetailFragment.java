package com.yooiistudios.morningkit.panel.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

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
    private static final String TAG = "MNNewsFeedDetailFragment";
    private static final int INVALID_NEWS_IDX = -1;

    private static final int RC_NEWS_SELECT = 1001;

    @InjectView(R.id.feedTitle) TextView feedTitleTextView;
    @InjectView(R.id.news_feed_detail_globe) ImageView globeImageView;
    @InjectView(R.id.newsList) ListView newsListView;
    @InjectView(R.id.result) LinearLayout newsResult;
    @InjectView(R.id.loadingImageView) ImageView loadingImageView;

    // object data
//    private String feedUrl;
    private MNNewsFeedUrl feedUrl;
    private MNNewsFeedUrl loadingFeedUrl;
    private RssFeed feed;
    private MNNewsFeedAdapter feedAdapter;
    private int highlightNewsIdx;

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
                highlightNewsIdx = INVALID_NEWS_IDX;
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
//            feedUrl = getPanelDataObject().getString(KEY_FEED_URL);
        }
        else {
            String savedUrlJsonStr = prefs.getString(KEY_FEED_URL, null);
            if (savedUrlJsonStr != null) {
                feedUrl = new Gson().fromJson(savedUrlJsonStr, urlType);
            }
            else {
                feedUrl = MNNewsFeedUrlProvider.getInstance(getActivity()).getDefault();
            }
//            feedUrl = prefs.getString(KEY_FEED_URL,
//                    MNNewsFeedUtil.getDefaultFeedUrl(getActivity()));
        }
        if (getPanelDataObject().has(KEY_LOADING_FEED_URL)) {
            loadingFeedUrl = new Gson().fromJson(getPanelDataObject()
                    .getString(KEY_LOADING_FEED_URL), urlType);
//            loadingFeedUrl = getPanelDataObject().getString(KEY_LOADING_FEED_URL);
        }
        else {
            loadingFeedUrl = null;
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
                if (getPanelDataObject().has(KEY_DISPLAYING_NEWS)) {
                    int idx = getPanelDataObject().getInt(
                            KEY_DISPLAYING_NEWS);
                    if (idx < feed.getRssItems().size() && idx >= 0) {
                        highlightNewsIdx = idx;
                        RssItem item = feed.getRssItems().remove(idx);
                        feed.getRssItems().add(0, item);
                    }
                    else {
                        highlightNewsIdx = INVALID_NEWS_IDX;
                    }
                }
            }
            else {
                feed = null;
                highlightNewsIdx = INVALID_NEWS_IDX;
            }
        }
        else {
            feed = null;
            highlightNewsIdx = INVALID_NEWS_IDX;
        }
    }

    private void initUI() {
        feedTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(feed.getLink()));
                startActivity(intent);
            }
        });
        loadingImageView.setVisibility(View.GONE);
        newsResult.setVisibility(View.GONE);

        initGlobeImageView();

        // theme
        initTheme();
    }

    private void initGlobeImageView() {
        globeImageView.setOnClickListener(onSelectFeedClickedListener);
        int iconColor = getResources().getColor(R.color.pastel_green_main_font_color);
        globeImageView.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
    }

    private View.OnClickListener
            onSelectFeedClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MNNewsSelectActivity.class);
            intent.putExtra(MNNewsSelectActivity.INTENT_KEY_URL, feedUrl);
            startActivityForResult(intent, RC_NEWS_SELECT);
//            DialogFragment newFragment =
//                    MNNewsFeedSelectDialogFragment.
//                            newInstance(feedUrl);
//            newFragment.setTargetFragment(
//                    MNNewsFeedDetailFragment.this, -1);
//            newFragment.show(getFragmentManager(), TAG_FEED_SELECT_DIALOG);

        }
    };

    private void initTheme() {
        MNThemeType currentThemeType =
                MNTheme.getCurrentThemeType(getActivity());
    }

    @Override
    protected void archivePanelData() throws JSONException {
        if (loadingFeedUrl != null) {
//            getPanelDataObject().put(KEY_LOADING_FEED_URL, loadingFeedUrl);
            getPanelDataObject().put(KEY_LOADING_FEED_URL,
                    new Gson().toJson(loadingFeedUrl));
            feedUrl = loadingFeedUrl;
            feed = null;
        }
        else {
            getPanelDataObject().remove(KEY_LOADING_FEED_URL);
        }
//        getPanelDataObject().put(KEY_FEED_URL, feedUrl);
        getPanelDataObject().put(KEY_FEED_URL, new Gson().toJson(feedUrl));

        if (feed == null || feed.getRssItems().size() == 0) {
            getPanelDataObject().remove(KEY_RSS_FEED);
            getPanelDataObject().remove(KEY_RSS_ITEMS);
        }
        else {
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

//            Context context = getActivity().getApplicationContext();
//
//            String feedTitle = MNNewsFeedUtil.getFeedTitle(context);
//            feedTitleTextView.setText(feedTitle != null ?
//                    feedTitle : feed.getTitle());
            feedTitleTextView.setText(feed.getTitle());

            feedAdapter = new MNNewsFeedAdapter(getActivity(), feed);
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
//        feedTitleTextView.setText(R.string.news_feed_invalid_url);
//        newsListView.setVisibility(View.GONE);
//        showResultView();
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
                MNLog.now("feedUrl: " + feedUrl.url);
                loadNewsFeed(feedUrl);
            }
        }
    }
}
