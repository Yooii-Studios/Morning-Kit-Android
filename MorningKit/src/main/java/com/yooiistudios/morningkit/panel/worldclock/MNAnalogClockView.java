package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 18.
 *
 * MNWorldClockAnalogView
 *  세계 시간 아날로그 전용 뷰
 */
public class MNAnalogClockView extends RelativeLayout {
    private static final String TAG = "MNAnalogClockView";

    private static final float OVERSHOOT_VALUE = 3.0f;
    private static final long OVERSHOOT_DURATION = 180;

    private static final int INVALID_ANGLE = -1;
    private static final int DEGREE_MINUTE = 6;
    private static final int MINUTE_TO_HOUR_DEGREE = 12;
    private static final int HOUR_TO_HOUR_DEGREE = 30;

    private ImageView clockBaseImageView;
    private ImageView hourHandImageView;
    private ImageView minuteHandImageView;
    private ImageView secondHandImageView;

    private boolean isFirstTick = true;
    private float overshoot;

    public MNAnalogClockView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNAnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNAnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        initUI();
    }

    private void initUI() {
//        clockBaseImageView = new RecyclingImageView(getContext());
//        clockBaseImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.clock_base_pm_classic_grey)));
        clockBaseImageView = new ImageView(getContext());
        clockBaseImageView.setImageResource(R.drawable.clock_base_pm_classic_grey);
        LayoutParams clockBaseLayoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        clockBaseLayoutParams.addRule(CENTER_IN_PARENT);
        clockBaseImageView.setLayoutParams(clockBaseLayoutParams);
        addView(clockBaseImageView);

//        secondHandImageView = new RecyclingImageView(getContext());
//        secondHandImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.clock_hand_second)));
        secondHandImageView = new ImageView(getContext());
        secondHandImageView.setImageResource(R.drawable.clock_hand_second);
        LayoutParams secondHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        secondHandLayoutParams.addRule(CENTER_IN_PARENT);
        secondHandImageView.setLayoutParams(secondHandLayoutParams);
        addView(secondHandImageView);

//        minuteHandImageView = new RecyclingImageView(getContext());
//        minuteHandImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.clock_hand_minute)));
        minuteHandImageView = new ImageView(getContext());
        minuteHandImageView.setImageResource(R.drawable.clock_hand_minute);
        LayoutParams minuteHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        minuteHandLayoutParams.addRule(CENTER_IN_PARENT);
        minuteHandImageView.setLayoutParams(minuteHandLayoutParams);
        addView(minuteHandImageView);

//        hourHandImageView = new RecyclingImageView(getContext());
//        hourHandImageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.clock_hand_hour)));
        hourHandImageView = new ImageView(getContext());
        hourHandImageView.setImageResource(R.drawable.clock_hand_hour);
        LayoutParams hourHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        hourHandLayoutParams.addRule(CENTER_IN_PARENT);
        hourHandImageView.setLayoutParams(hourHandLayoutParams);
        addView(hourHandImageView);
    }

    // 시, 분, 초를 대입해서 애니메이션 작동이 되게 한다
    public void animateClock(int hour, int minute, int second) {
        int newHourAngle = hour * HOUR_TO_HOUR_DEGREE + (minute / MINUTE_TO_HOUR_DEGREE) * DEGREE_MINUTE;
        int newMinuteAngle = minute * DEGREE_MINUTE;
        int newSecondAngle = second * DEGREE_MINUTE;

        if (isFirstTick) {
            isFirstTick = false;
            rotate(hourHandImageView, 0, newHourAngle);
            rotate(minuteHandImageView, 0, newMinuteAngle);
            rotate(secondHandImageView, 0, newSecondAngle);
        } else {
            if (minute == 0 && second == 0) {
                rotate(hourHandImageView, newHourAngle - DEGREE_MINUTE, newHourAngle);
            }
            if (second == 0) {
                rotate(minuteHandImageView, newMinuteAngle - DEGREE_MINUTE, newMinuteAngle);
            }
            rotate(secondHandImageView, newSecondAngle - DEGREE_MINUTE, newSecondAngle);
        }

        // when hour changed - check art resources(clock base for AM/PM)
        if (minute == 0 && second == 0) {
            if (hour >= 12) {
                // 정오부터 밤12시까지 PM
            } else {
                // 오전 0시부터 정오 전까지 AM
            }
        }
    }

    private void rotate(ImageView imageView, int fromAngle, int toAngle){
        Animation anim;
        anim = new RotateAnimation(fromAngle, toAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        int gap = Math.abs(toAngle - fromAngle);

        // 최초 시계 설정시에는 애니메이션 없이, 그 이후에는 애니메이션 작용
        if (gap != DEGREE_MINUTE) {
            anim.setDuration(0);
        } else {
            anim.setDuration(OVERSHOOT_DURATION);
            OvershootInterpolator i = new OvershootInterpolator(OVERSHOOT_VALUE);
            anim.setInterpolator(i);
        }
        anim.setFillAfter(true);
        imageView.startAnimation(anim);
    }
}
