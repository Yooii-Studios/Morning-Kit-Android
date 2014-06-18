package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 17.
 *
 * MNMainButtonLayout
 *  터치를 아래로 보내지 않기 위해 구현
 */
public class MNMainButtonLayout extends RelativeLayout {
    public MNMainButtonLayout(Context context) {
        super(context);
        initTouchListener();
    }

    public MNMainButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTouchListener();
    }

    public MNMainButtonLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTouchListener();
    }

    private void initTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
        // on true won't pass the touch event to parent
//        return true;
//    }
}
