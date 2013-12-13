package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 13.
 * MNMainScrollView
 *  메인 스크롤뷰
 */
public class MNMainScrollView extends ScrollView {
    private Context context;
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 150;
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
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxYOverscrollDistance, isTouchEvent);
    }
}
