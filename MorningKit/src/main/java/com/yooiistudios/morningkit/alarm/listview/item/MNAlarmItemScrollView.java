package com.yooiistudios.morningkit.alarm.listview.item;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeChecker;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 2.
 *
 * MNAlarmItemScrollView
 *  알람 리스트뷰에 들어갈 뷰로써 수평 스크롤뷰이고 알람뷰를 내포하고 있다. 스크롤로 알람을 삭제할 수 있는 기능을 구현
 */
public class MNAlarmItemScrollView extends HorizontalScrollView {
    private static final String TAG = "MNAlarmItemScrollView";

    /**
     * Variables
     */
    @Getter private ArrayList<LinearLayout> layoutItems = null;
    private GestureDetector gestureDetector;
    private int activeFeature = 1;
    private int activeFeatureDestination = 1;
    @Getter private View alarmView;
    private int deviceWidth = 0;

    private int itemIndex = -1;

    private float actionDown_X;
    private float actionUp_X;

    private int delayMillisec = 90;	// 알람이 삭제되는 딜레이
    private int limitedVelocityX = 1500;
    private int limitedPercent = 30;

    /**
     * Constructor
     */
    // 사용하지 않을 것으로 예상
//    public MNAlarmItemScrollView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public MNAlarmItemScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    // newInstance를 사용해서만 생성할 수 있음
    private MNAlarmItemScrollView(Context context) {
        super(context);
    }

    public static MNAlarmItemScrollView newInstance(Context context, int position, View alarmView) {
        MNAlarmItemScrollView alarmItemScrollView = new MNAlarmItemScrollView(context);
        Integer positionInteger = position;
        alarmItemScrollView.setTag(positionInteger);
        alarmItemScrollView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        alarmItemScrollView.itemIndex = position;
        alarmItemScrollView.alarmView = alarmView;
        alarmItemScrollView.deviceWidth = MNDeviceSizeChecker.getDeviceWidth(context);

        alarmItemScrollView.initScrollView();

        // Callback 구현 필요

        return alarmItemScrollView;
    }


    /**
     * Methods
     */

    private void initScrollView() {
        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);

        initLayoutItems();
        initInternalWrapper();
        initGesture();
    }

    private void initLayoutItems() {
        layoutItems = new ArrayList<LinearLayout>();

        // LinearLayout leftLayout = (LinearLayout)
        // layoutInflater.inflate(R.layout.alarm_item_blank, scrollView);
        LinearLayout leftLayout = (LinearLayout) View.inflate(getContext(),
                R.layout.alarm_item_blank, null);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(deviceWidth,
                LayoutParams.WRAP_CONTENT);
        leftLayout.setLayoutParams(p);
        leftLayout.setGravity(Gravity.CENTER);
        layoutItems.add(leftLayout);

        LinearLayout middleLayout = (LinearLayout) View.inflate(getContext(),
                R.layout.alarm_item_blank, null);
        middleLayout.addView(alarmView);
//        middleLayout.setBackgroundColor(0x00000000);
        p = new LinearLayout.LayoutParams(deviceWidth, LayoutParams.WRAP_CONTENT);
        middleLayout.setLayoutParams(p);
        middleLayout.setGravity(Gravity.CENTER);
        layoutItems.add(middleLayout);

        LinearLayout rightLayout = (LinearLayout) View.inflate(getContext(),
                R.layout.alarm_item_blank, null);
//        rightLayout.setBackgroundColor(0xff5511);
        p = new LinearLayout.LayoutParams(deviceWidth, LayoutParams.WRAP_CONTENT);
        rightLayout.setLayoutParams(p);
        rightLayout.setGravity(Gravity.CENTER);
        layoutItems.add(rightLayout);
    }

    private void initInternalWrapper() {

        LinearLayout internalWrapper = new LinearLayout(getContext());
        internalWrapper.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        addView(internalWrapper);

        // for -> for each

//        for (int i = 0; i < layoutItems.size(); i++) {
//            LinearLayout featureLayout = layoutItems.get(i);
//            // ...
//            // Create the view for each screen in the scroll view
//            // ...
//            internalWrapper.addView(featureLayout);
//        }

        for(LinearLayout featureLayout : layoutItems) {
            internalWrapper.addView(featureLayout);
        }
    }

    private void initGesture() {
        gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // If the user swipes
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                } else if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)) {

                    // 몇 %인지 표시하기
//					Log.i(TAG, "dragging percent is: " + Math.abs(actionDown_X - actionUp_X) / deviceWidth * 100);

                    // onFling이 되지 않을 경우는 전체 width의 35%이상 드래그될 경우 삭제한다.
                    if (Math.abs(actionDown_X - actionUp_X) / deviceWidth * 100 > limitedPercent) {

                        if ((actionDown_X - actionUp_X) >= 0) {
                            // 왼쪽으로 드래그
                            Log.i(TAG, "remove to left");
                            activeFeature = 2;
                        }else{
                            // 오른쪽으로 드래그
                            Log.i(TAG, "remove to right");
                            activeFeature = 0;
                        }
                        Log.i(TAG, "dragged more than 30% : should remove");

                        // mActiveFeature를 구하는 부분이 잘 안되어서 그냥 방향을 정해줌.
//						int scrollX = getScrollX();
                        int featureWidth = v.getMeasuredWidth();
//						Log.i(TAG, "featureWidth: " + featureWidth);
//						activeFeature = ((scrollX + (featureWidth / 16)) / featureWidth);
//						Log.i(TAG, "activeFeature: " + activeFeature);
                        int scrollTo = activeFeature * featureWidth;
                        smoothScrollTo(scrollTo, 0);
                    }else{
//						Log.i(TAG, "need scroll to original position");
                        int featureWidth = v.getMeasuredWidth();
                        int scrollTo = activeFeature * featureWidth;
                        smoothScrollTo(scrollTo, 0);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            Log.i(TAG, "onFling");
//			Log.i(TAG, "velocityX: " + velocityX);
//			setAlarmItemViewTouchingState();

            // right to left
//			if ((e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE))
            // 일정 이하의 속도에서는 삭제되지 않게 구현
            if (velocityX * -1 > limitedVelocityX){
//				Log.i(TAG, "onFling: removeALarmToLeft");
                int featureWidth = getMeasuredWidth();
                activeFeature = (activeFeature < (layoutItems.size() - 1)) ? activeFeature + 1
                        : layoutItems.size() - 1;
                smoothScrollTo(activeFeature * featureWidth, 0);
                return true;
            }

            // left to right
//			else if ((e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE))
            else if(velocityX > limitedVelocityX){
//				Log.i(TAG, "onFling: removeALarmToRight");
                int featureWidth = getMeasuredWidth();
                activeFeature = (activeFeature > 0) ? activeFeature - 1 : 0;
                smoothScrollTo(activeFeature * featureWidth, 0);
                return true;
            }
            return false;
        }
    }

    // 로드 시작시 scrollTo를 실행해줌
    protected void onLayout (boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (deviceWidth == 0) {
            deviceWidth = MNDeviceSizeChecker.getDeviceWidth(getContext());
        }
        this.scrollTo(deviceWidth, 0);

//        Log.i(TAG, "" + getHeight());
    }
}
