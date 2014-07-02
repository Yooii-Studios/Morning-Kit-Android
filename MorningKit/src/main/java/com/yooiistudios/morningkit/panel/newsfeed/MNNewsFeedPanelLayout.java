package com.yooiistudios.morningkit.panel.newsfeed;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 7. 2.
 */
public class MNNewsFeedPanelLayout extends MNPanelLayout {

    public MNNewsFeedPanelLayout(Context context) {
        super(context);
    }

    public MNNewsFeedPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        TextView textView = new TextView(getContext());
        textView.setText("blah..news feed panel layout test");
        addView(textView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
    }

    @Override
    protected void updateUI() {
        super.updateUI();
    }
}
