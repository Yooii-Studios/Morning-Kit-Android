package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainResources;

import lombok.Setter;

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

    private static final float OVERSHOOT_VALUE = 4.4f;
    private static final long OVERSHOOT_DURATION = 120;

    private static final int INVALID_ANGLE = -1;
    private static final int DEGREE_MINUTE = 6;
    private static final int MINUTE_TO_HOUR_DEGREE = 12;
    private static final int HOUR_TO_HOUR_DEGREE = 30;

    private int previousHourAndgle = 0;

    private ImageView clockBaseImageView;
    private ImageView hourHandImageView;
    private ImageView minuteHandImageView;
    private ImageView secondHandImageView;

    @Setter boolean isFirstTick = true;
    private boolean isTimeAm = false;
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
        clockBaseImageView = new ImageView(getContext());
        LayoutParams clockBaseLayoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        clockBaseLayoutParams.addRule(CENTER_IN_PARENT);
        clockBaseImageView.setLayoutParams(clockBaseLayoutParams);
        addView(clockBaseImageView);

        hourHandImageView = new ImageView(getContext());
        LayoutParams hourHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        hourHandLayoutParams.addRule(CENTER_IN_PARENT);
        hourHandImageView.setLayoutParams(hourHandLayoutParams);
        addView(hourHandImageView);

        minuteHandImageView = new ImageView(getContext());
        LayoutParams minuteHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        minuteHandLayoutParams.addRule(CENTER_IN_PARENT);
        minuteHandImageView.setLayoutParams(minuteHandLayoutParams);
        addView(minuteHandImageView);

        secondHandImageView = new ImageView(getContext());
        LayoutParams secondHandLayoutParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        secondHandLayoutParams.addRule(CENTER_IN_PARENT);
        secondHandImageView.setLayoutParams(secondHandLayoutParams);
        addView(secondHandImageView);
    }

    // 시, 분, 초를 대입해서 애니메이션 작동이 되게 한다
    public void animateClock(int hour, int minute, int second) {
        int newHourAngle = hour * HOUR_TO_HOUR_DEGREE + (minute / MINUTE_TO_HOUR_DEGREE) * DEGREE_MINUTE;
        int newMinuteAngle = minute * DEGREE_MINUTE;
        int newSecondAngle = second * DEGREE_MINUTE;

        // when hour changed - check art resources(clock base for AM/PM)
        if (minute == 0 && second == 0) {
            checkAmPm(hour);
            applyTheme();
        }

        if (isFirstTick) {
            checkAmPm(hour);
            applyTheme();

            rotate(hourHandImageView, newHourAngle, newHourAngle);
            rotate(minuteHandImageView, newMinuteAngle, newMinuteAngle);
            rotate(secondHandImageView, newSecondAngle, newSecondAngle);

            isFirstTick = false;
        } else {
            if (minute == 0 && second == 0) {
//                rotate(hourHandImageView, newHourAngle - DEGREE_MINUTE, newHourAngle);
                rotate(hourHandImageView, previousHourAndgle, newHourAngle);
            }
            if (second == 0) {
                // 수정: 시침도 1분마다 조금씩 움직이게 구현.
                rotate(hourHandImageView, previousHourAndgle, newHourAngle);
                rotate(minuteHandImageView, newMinuteAngle - DEGREE_MINUTE, newMinuteAngle);
            }
            rotate(secondHandImageView, newSecondAngle - DEGREE_MINUTE, newSecondAngle);
        }
        previousHourAndgle = newHourAngle;
    }

    private void checkAmPm(int hour) {
        if (hour >= 12) {
            // 정오부터 밤12시까지 PM
            isTimeAm = false;
        } else {
            // 오전 0시부터 정오 전까지 AM
            isTimeAm = true;
        }
    }
    private void rotate(ImageView imageView, int fromAngle, int toAngle){
        Animation anim;
        anim = new RotateAnimation(fromAngle, toAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // animation persist after it has been done
//        int gap = Math.abs(toAngle - fromAngle);

        // 최초 시계 설정시에는 애니메이션 없이, 그 이후에는 애니메이션 작용
//        if (gap != DEGREE_MINUTE) {
        if (isFirstTick) {
            anim.setDuration(0);
        } else {
            anim.setDuration(OVERSHOOT_DURATION);
            OvershootInterpolator i = new OvershootInterpolator(OVERSHOOT_VALUE);
            anim.setInterpolator(i);
        }
        anim.setFillAfter(true);
        imageView.startAnimation(anim);
    }

    public void applyTheme() {

        // recycle before alloc new image resource
//        MNLog.i(TAG, "recycle image views");
        MNBitmapUtils.recycleImageView(clockBaseImageView);
        MNBitmapUtils.recycleImageView(secondHandImageView);
        MNBitmapUtils.recycleImageView(minuteHandImageView);
        MNBitmapUtils.recycleImageView(hourHandImageView);

        // use BitmapFactory instead of setImageResource because of OOM
        MNThemeType currentType = MNTheme.getCurrentThemeType(getContext().getApplicationContext());
        Context context = getContext().getApplicationContext();
        BitmapFactory.Options options = MNBitmapUtils.getDefaultOptions();
        if (isTimeAm) {
            Bitmap clockBaseBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockAMBase(currentType, context), options);
            clockBaseImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), clockBaseBitmap));

            Bitmap hourHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockAMHourHand(currentType, context), options);
            hourHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), hourHandBitmap));

            Bitmap minuteHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockAMMinuteHand(currentType, context), options);
            minuteHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), minuteHandBitmap));

            Bitmap secondHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockAMSecondHand(currentType, context), options);
            secondHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), secondHandBitmap));
        } else {
            Bitmap clockBaseBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockPMBase(currentType, context), options);
            clockBaseImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), clockBaseBitmap));

            Bitmap hourHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockPMHourHand(currentType, context), options);
            hourHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), hourHandBitmap));

            Bitmap minuteHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockPMMinuteHand(currentType, context), options);
            minuteHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), minuteHandBitmap));

            Bitmap secondHandBitmap = BitmapFactory.decodeResource(
                    getContext().getApplicationContext().getResources(),
                    MNMainResources.getWorldClockPMSecondHand(currentType, context), options);
            secondHandImageView.setImageDrawable(
                    new BitmapDrawable(getContext().getApplicationContext().getResources(), secondHandBitmap));
        }
    }
}
