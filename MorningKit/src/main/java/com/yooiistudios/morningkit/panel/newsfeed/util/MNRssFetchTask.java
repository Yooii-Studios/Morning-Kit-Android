package com.yooiistudios.morningkit.panel.newsfeed.util;

import android.os.AsyncTask;
import android.text.Html;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by Dongheyon Jeong on in RSSTest from Yooii Studios Co., LTD. on 2014. 6. 27.
 */
public class MNRssFetchTask extends AsyncTask<String, Void, RssFeed> {
//    private String mRssUrl;
    private OnFetchListener mOnFetchListener;

    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public MNRssFetchTask(OnFetchListener onFetchListener) {
//        mRssUrl = rssUrl;
        mOnFetchListener = onFetchListener;
    }

    @Override
    protected RssFeed doInBackground(String... args) {

        if (args == null || args.length <= 0) {
            //error
            return null;
        }
        String urlStr = args[0];

        RssFeed feed = null;
        try {
            URL url = new URL(urlStr);
//            InputStream is = url.openStream();
            URLConnection conn = url.openConnection();

            feed = RssReader.read(conn.getInputStream());

            for (RssItem item : feed.getRssItems()) {
                String desc = item.getDescription();
                String strippedDesc = Html.fromHtml(desc.substring(0,
                        desc.length())).toString();

                int length = strippedDesc.length() > MAX_DESCRIPTION_LENGTH ?
                        MAX_DESCRIPTION_LENGTH : strippedDesc.length();
                item.setDescription(
                        Html.fromHtml(strippedDesc.substring(0, length)).toString());
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

        if (rssFeed != null) {
            // success
            if (mOnFetchListener != null) {
                mOnFetchListener.onFetch(rssFeed);
            }
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
