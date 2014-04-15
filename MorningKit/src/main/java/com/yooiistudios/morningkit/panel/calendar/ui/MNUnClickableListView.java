package com.yooiistudios.morningkit.panel.calendar.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 15.
 *
 * MNUnClickableListView
 *  선택하거나 스크롤 할 수 없는 리스트뷰
 */
public class MNUnClickableListView extends ListView {
    public MNUnClickableListView(Context context) {
        super(context);
    }

    public MNUnClickableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MNUnClickableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
        // on false the parent will handle the touch event
        return false;
    }
}
