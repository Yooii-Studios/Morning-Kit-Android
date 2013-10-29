package com.yooii.morningkit.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by yongbinbae on 13. 10. 22..
 */
public class MNMainAlarmListView extends ListView
{
    public MNMainAlarmListView(Context context) {
        super(context);
        init();
    }

    public MNMainAlarmListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MNMainAlarmListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}
