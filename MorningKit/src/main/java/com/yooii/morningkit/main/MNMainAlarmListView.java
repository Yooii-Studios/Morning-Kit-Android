package com.yooii.morningkit.main;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by yongbinbae on 13. 10. 22..
 */
public class MNMainAlarmListView extends ListView
{
    public MNMainAlarmListView(Context context)
    {
        super(context);

        init();
    }

    public MNMainAlarmListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    private void init()
    {
        this.setBackgroundColor(Color.RED);
    }

}
