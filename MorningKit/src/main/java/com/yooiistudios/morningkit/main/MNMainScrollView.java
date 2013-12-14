package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 13.
 * MNMainScrollView
 *  메인 스크롤뷰
 */
public class MNMainScrollView extends ScrollView {
    private static final String TAG = "MNMainScrollView";
    private Context context;
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 80;
    private int maxYOverscrollDistance;

    /**
     * Constructor
     */
    public MNMainScrollView(Context context) {
        super(context);
        this.context = context;
        initBounceScrollView();
    }

    public MNMainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initBounceScrollView();
    }

    public MNMainScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initBounceScrollView();
    }

    private void initBounceScrollView() {
        // get the density of the screen and do some maths with it on the max
        // overscroll distance
        // variable so that you get similar behaviors no matter what the screen
        // size

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float density = metrics.density;

        maxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        // This is where the magic happens, we have replaced the incoming
        // maxOverScrollY with our own custom variable maxYOverscrollDistance;
//        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        float ratio = 1;

        if (scrollY < 0 || scrollY >= scrollRangeY) {
            float convertedScrollY;
            if (scrollY < 0) {
                convertedScrollY = Math.abs(scrollY);
            } else {
                convertedScrollY = scrollY - scrollRangeY;
            }
            // 관성을 주기 위해 10.f 값으로 조절
            ratio = (float) maxYOverscrollDistance / convertedScrollY / 10.f;
            if (ratio > 1) {
                ratio = 1;
            }
        }

        if ((scrollY < 0 && deltaY > 0) || (scrollY >= scrollRangeY && deltaY < 0)) {
//            Log.i(TAG, "normal way to back");
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxYOverscrollDistance, isTouchEvent);
        } else {
            return super.overScrollBy(deltaX, (int) (deltaY * ratio), scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxYOverscrollDistance, isTouchEvent);
        }
    }
}
