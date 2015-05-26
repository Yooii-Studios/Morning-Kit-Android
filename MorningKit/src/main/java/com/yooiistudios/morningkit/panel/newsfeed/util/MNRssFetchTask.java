package com.yooiistudios.morningkit.panel.newsfeed.util;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

import com.flurry.android.FlurryAgent;
import com.yooiistudios.morningkit.common.log.MNFlurry;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrl;
import com.yooiistudios.morningkit.panel.newsfeed.model.MNNewsFeedUrlType;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by Dongheyon Jeong on in RSSTest from Yooii Studios Co., LTD. on 2014. 6. 27.
 *
 * MNRssFetchTask
 *  RSS 내용을 읽어오는 AsyncTask 메서드
 *
 */
public class MNRssFetchTask extends AsyncTask<MNNewsFeedUrl, Void, RssFeed> {
    private Context mContext;
    private MNNewsFeedUrl mFeedUrl;
    private OnFetchListener mOnFetchListener;

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final String ILLEGAL_CHARACTER_OBJ = Character.toString((char)65532);

    public MNRssFetchTask(Context context, MNNewsFeedUrl feedUrl, OnFetchListener onFetchListener) {
        mContext = context;
        mFeedUrl = feedUrl;
        mOnFetchListener = onFetchListener;
    }

    @Override
    protected RssFeed doInBackground(MNNewsFeedUrl... args) {

        if (!mFeedUrl.type.equals(MNNewsFeedUrlType.CUSTOM)) {
            // 디폴트 세팅을 사용할 경우 패널단에서 언어설정을 감지 못하므로 무조건 현재 언어의
            // 디폴트 url을 가져온다.
            mFeedUrl = MNNewsFeedUrlProvider.getInstance(mContext).getDefault();
        }

        RssFeed feed = null;
        try {
            URL url = new URL(mFeedUrl.url);
//            InputStream is = url.openStream();
            URLConnection conn = url.openConnection();

            feed = RssReader.read(conn.getInputStream());

            for (RssItem item : feed.getRssItems()) {
                String desc = item.getDescription();
                if (desc != null) {
                    String strippedDesc = Html.fromHtml(desc.substring(0,
                            desc.length())).toString();

                    int length = strippedDesc.length() > MAX_DESCRIPTION_LENGTH ?
                            MAX_DESCRIPTION_LENGTH : strippedDesc.length();
                    String refinedDesc = new StringBuilder(strippedDesc).substring
                            (0, length).replaceAll(ILLEGAL_CHARACTER_OBJ, "")
                            .replaceAll("\n", " ");
                    item.setDescription(refinedDesc);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return feed;
    }

    @Override
    protected void onPostExecute(RssFeed rssFeed) {
        super.onPostExecute(rssFeed);
        if (isCancelled()) {
            if (mOnFetchListener != null) {
                mOnFetchListener.onCancel();
            }
            return;
        }

        if (rssFeed != null && rssFeed.getRssItems() != null) {
            // success
            if (mOnFetchListener != null) {
                mOnFetchListener.onFetch(rssFeed);
            }

            // flurry
            Map<String, String> params = new HashMap<String, String>();
            params.put(MNFlurry.NEWS,
                    mFeedUrl.type.equals(MNNewsFeedUrlType.CUSTOM) ?
                    "Custom RSS" : "Default News");
            FlurryAgent.logEvent(MNFlurry.PANEL, params);

//            ArrayList<RssItem> rssItems = rssFeed.getRssItems();
//            for (RssItem rssItem : rssItems) {
//                Log.i("RSS Reader", rssItem.getTitle());
//            }
        }
        else {
            // error
            if (mOnFetchListener != null) {
                mOnFetchListener.onError();
            }
        }
    }

    public interface OnFetchListener {
        public void onFetch(RssFeed rssFeed);
        public void onCancel();
        public void onError();
    }
}
