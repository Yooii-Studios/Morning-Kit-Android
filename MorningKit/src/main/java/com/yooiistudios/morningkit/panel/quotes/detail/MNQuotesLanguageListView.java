package com.yooiistudios.morningkit.panel.quotes.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesLanguageListView
 */
public class MNQuotesLanguageListView extends ListView {
    public MNQuotesLanguageListView(Context context) {
        super(context);
        init();
    }

    public MNQuotesLanguageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MNQuotesLanguageListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setAdapter(new MNQuotesLanguageListAdapter(getContext()));
    }
}
