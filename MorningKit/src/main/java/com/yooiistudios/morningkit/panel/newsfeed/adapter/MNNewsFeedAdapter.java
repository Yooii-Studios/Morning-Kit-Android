package com.yooiistudios.morningkit.panel.newsfeed.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 3.
 */
public class MNNewsFeedAdapter extends BaseAdapter {
    private Context mContext;
    private RssFeed mFeed;

    public MNNewsFeedAdapter(Context context, RssFeed feed) {
        mContext = context;
        mFeed = feed;
    }

    @Override
    public int getCount() {
        return mFeed.getRssItems().size();
    }

    @Override
    public Object getItem(int i) {
        return mFeed.getRssItems().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.panel_news_feed_detail_list_item,
                    viewGroup, false);
        }
        RssItem item = mFeed.getRssItems().get(i);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        titleView.setText(item.getTitle());

        TextView contentView = (TextView)view.findViewById(R.id.content);
        //TODO 퍼포먼스 개선을 위해 우선 200글자만 읽어서 보여줌(어차피 2줄 밖에 안보임)
        contentView.setText(Html.fromHtml(item.getDescription().substring(0, 200
                )));

        return view;
    }
}
