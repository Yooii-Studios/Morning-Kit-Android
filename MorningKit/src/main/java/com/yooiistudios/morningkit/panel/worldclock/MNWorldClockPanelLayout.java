package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZone;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneLoader;
import com.yooiistudios.morningkit.panel.worldclock.model.MNWorldClock;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.Calendar;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNWorldClockPanelLayout
 */
public class MNWorldClockPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNWorldClockPanelLayout";
    public static final String WORLD_CLOCK_PREFS = "WORLD_CLOCK_PREFS";
    public static final String WORLD_CLOCK_PREFS_LATEST_TIME_ZONE = "WORLD_CLOCK_PREFS_LATEST_TIME_ZONE";

    protected static final String WORLD_CLOCK_DATA_IS_ALALOG = "WORLD_CLOCK_DATA_IS_ALALOG";
    protected static final String WORLD_CLOCK_DATA_IS_24_HOUR = "WORLD_CLOCK_DATA_IS_24_HOUR";
    protected static final String WORLD_CLOCK_DATA_TIME_ZONE = "WORLD_CLOCK_DATA_TIME_ZONE";

    private RelativeLayout analogClockLayout;
    private MNAnalogClockView analogClockView;
    private TextView analogAmpmTextView;
    private TextView analogDayDifferenceTextView;
    private TextView analogCityNameTextView;

    private RelativeLayout digitalClockLayout;
    private RelativeLayout digitalTimeLayout;
    private TextView digitalTimeTextView;
    private TextView digitalAmpmTextView;
    private TextView digitalDayDifferenceTextView;
    private TextView digitalCityNameTextView;

    private boolean isClockRunning = false;
    private boolean isClockAnalog = true;
    private boolean isUsing24Hours = false;
    private MNWorldClock worldClock;

    private Handler clockHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ){
            if (isClockRunning){
                // UI갱신
                worldClock.tick();
                updateUI();

                // tick의 동작 시간을 계산해서 정확히 1초마다 UI 갱신을 요청할 수 있게 구현
                long endMilli = System.currentTimeMillis();
                long delay = endMilli % 1000;

                clockHandler.sendEmptyMessageDelayed(0, 1000 - delay);
            }
        }
    };

    public MNWorldClockPanelLayout(Context context) {
        super(context);
    }

    public MNWorldClockPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        initAnalogClockUI();
        initDigitalClockUI();
    }

    private void initClockModel(MNTimeZone timeZone) {
        // 세계 시계 캘린더 세팅
        if (worldClock == null) {
            worldClock = new MNWorldClock();
        }
        worldClock.setTimeZone(timeZone);

        // 핸들러 실행
        startClock();
    }

    private void initAnalogClockUI() {
        // container
        analogClockLayout = new RelativeLayout(getContext());
        LayoutParams analogLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        analogLayoutParams.addRule(CENTER_IN_PARENT);
        analogClockLayout.setLayoutParams(analogLayoutParams);
        getContentLayout().addView(analogClockLayout);

        // ampm
        analogAmpmTextView = new TextView(getContext());
        analogAmpmTextView.setGravity(Gravity.CENTER);
        LayoutParams ampmLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ampmLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        ampmLayoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_outer);
        ampmLayoutParams.addRule(ALIGN_PARENT_TOP);
        ampmLayoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_outer);
        analogAmpmTextView.setLayoutParams(ampmLayoutParams);
        getContentLayout().addView(analogAmpmTextView);

        // analog clock
        int analogViewSize = DipToPixel.dpToPixel(getContext(), 60);
        analogClockView = new MNAnalogClockView(getContext());
        analogClockView.setId(9173751);
        LayoutParams analogViewParams = new LayoutParams(analogViewSize, analogViewSize);
        analogViewParams.addRule(CENTER_HORIZONTAL);
        analogClockView.setLayoutParams(analogViewParams);
        analogClockLayout.addView(analogClockView);

        // day differences
        analogDayDifferenceTextView = new TextView(getContext());
        analogDayDifferenceTextView.setId(1859182);
        analogDayDifferenceTextView.setGravity(Gravity.CENTER);
        LayoutParams dayDiffLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayDiffLayoutParams.addRule(CENTER_HORIZONTAL);
        dayDiffLayoutParams.addRule(BELOW, analogClockView.getId());
        analogDayDifferenceTextView.setLayoutParams(dayDiffLayoutParams);
        analogClockLayout.addView(analogDayDifferenceTextView);

        // city name
        analogCityNameTextView = new TextView(getContext());
        analogCityNameTextView.setGravity(Gravity.CENTER);
        analogCityNameTextView.setSingleLine();
        LayoutParams cityNameLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cityNameLayoutParams.addRule(CENTER_HORIZONTAL);
        cityNameLayoutParams.addRule(BELOW, analogDayDifferenceTextView.getId());
        analogCityNameTextView.setLayoutParams(cityNameLayoutParams);
        analogClockLayout.addView(analogCityNameTextView);

        // test
        if (DEBUG_UI) {
            analogClockLayout.setBackgroundColor(Color.BLUE);
            analogAmpmTextView.setBackgroundColor(Color.CYAN);
            analogAmpmTextView.setText("AM");
            analogClockView.setBackgroundColor(Color.RED);
            analogDayDifferenceTextView.setBackgroundColor(Color.MAGENTA);
            analogDayDifferenceTextView.setText("Today");
            analogCityNameTextView.setBackgroundColor(Color.GREEN);
            analogCityNameTextView.setText("Milwaukee, WI");
        }
    }

    private void initDigitalClockUI() {
        // container
        digitalClockLayout = new RelativeLayout(getContext());
        LayoutParams digitalLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        digitalLayoutParams.addRule(CENTER_IN_PARENT);
        digitalClockLayout.setLayoutParams(digitalLayoutParams);
        getContentLayout().addView(digitalClockLayout);

        // time - 시간 + ampm 합쳐서 horizontal center 정렬을 위한 레이아웃
        digitalTimeLayout = new RelativeLayout(getContext());
        digitalTimeLayout.setId(531871);
        LayoutParams digitalTimeLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        digitalTimeLayoutParams.addRule(CENTER_HORIZONTAL);
        digitalTimeLayout.setLayoutParams(digitalTimeLayoutParams);
        digitalClockLayout.addView(digitalTimeLayout);

        // time text
        digitalTimeTextView = new TextView(getContext());
        digitalTimeTextView.setId(4193752);
        digitalTimeTextView.setGravity(Gravity.BOTTOM);
        LayoutParams timeLayoutParms = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        digitalTimeTextView.setLayoutParams(timeLayoutParms);
        digitalTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DipToPixel.dpToPixel(getContext(), 40));
        digitalTimeLayout.addView(digitalTimeTextView);

        // ampm
        digitalAmpmTextView = new TextView(getContext());
        digitalAmpmTextView.setGravity(Gravity.BOTTOM);
        LayoutParams ampmLayoutParms = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ampmLayoutParms.addRule(ALIGN_BASELINE, digitalTimeTextView.getId());
        ampmLayoutParms.addRule(RIGHT_OF, digitalTimeTextView.getId());
        ampmLayoutParms.leftMargin = getResources().getDimensionPixelOffset(R.dimen.margin_inner);
        digitalAmpmTextView.setLayoutParams(ampmLayoutParms);
        digitalTimeLayout.addView(digitalAmpmTextView);

        // day differences
        digitalDayDifferenceTextView = new TextView(getContext());
        digitalDayDifferenceTextView.setId(8159182);
        digitalDayDifferenceTextView.setGravity(Gravity.CENTER);
        LayoutParams dayDiffLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayDiffLayoutParams.addRule(CENTER_HORIZONTAL);
        dayDiffLayoutParams.addRule(BELOW, digitalTimeLayout.getId());
        digitalDayDifferenceTextView.setLayoutParams(dayDiffLayoutParams);
        digitalClockLayout.addView(digitalDayDifferenceTextView);

        // city name
        digitalCityNameTextView = new TextView(getContext());
        digitalCityNameTextView.setGravity(Gravity.CENTER);
        digitalCityNameTextView.setSingleLine();
        LayoutParams cityNameLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cityNameLayoutParams.addRule(CENTER_HORIZONTAL);
        cityNameLayoutParams.addRule(BELOW, digitalDayDifferenceTextView.getId());
        digitalCityNameTextView.setLayoutParams(cityNameLayoutParams);
        digitalClockLayout.addView(digitalCityNameTextView);

        // test
        if (DEBUG_UI) {
            digitalClockLayout.setBackgroundColor(Color.BLUE);
            digitalTimeLayout.setBackgroundColor(Color.RED);
            digitalTimeTextView.setBackgroundColor(Color.YELLOW);
            digitalTimeTextView.setText("3:41");
            digitalAmpmTextView.setBackgroundColor(Color.CYAN);
            digitalAmpmTextView.setText("AM");
            digitalDayDifferenceTextView.setBackgroundColor(Color.MAGENTA);
            digitalDayDifferenceTextView.setText("Today");
            digitalCityNameTextView.setBackgroundColor(Color.GREEN);
            digitalCityNameTextView.setText("Milwaukee, WI");
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        if (getPanelDataObject().has(WORLD_CLOCK_DATA_IS_ALALOG)) {
            isClockAnalog = getPanelDataObject().getBoolean(WORLD_CLOCK_DATA_IS_ALALOG);
        }
        if (getPanelDataObject().has(WORLD_CLOCK_DATA_IS_24_HOUR)) {
            isUsing24Hours = getPanelDataObject().getBoolean(WORLD_CLOCK_DATA_IS_24_HOUR);
        }
        MNTimeZone timeZone;
        if (getPanelDataObject().has(WORLD_CLOCK_DATA_TIME_ZONE)) {
            Type type = new TypeToken<MNTimeZone>() {}.getType();
            String timeZoneJsonString = getPanelDataObject().getString(WORLD_CLOCK_DATA_TIME_ZONE);
            timeZone = new Gson().fromJson(timeZoneJsonString, type);
        } else {
            timeZone = MNTimeZoneLoader.getDefaultZone(getContext());
        }

        // 세계 시계 정보를 가지고 시간 정보를 다시 계산
        initClockModel(timeZone);
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (isClockAnalog) {
            updateAnalogClockUI();
        } else {
            updateDigitalClockUI();
        }
    }

    private void updateAnalogClockUI() {
        analogClockLayout.setVisibility(View.VISIBLE);
        analogAmpmTextView.setVisibility(View.VISIBLE);
        digitalClockLayout.setVisibility(View.GONE);

        Calendar worldClockCalendar = worldClock.getWorldClockCalendar();

        // time
        analogClockView.animateClock(
                worldClockCalendar.get(Calendar.HOUR_OF_DAY),
                worldClockCalendar.get(Calendar.MINUTE),
                worldClockCalendar.get(Calendar.SECOND));

        // am/pm
        if (worldClockCalendar.get(Calendar.HOUR_OF_DAY) >= 12) {
            analogAmpmTextView.setText(R.string.alarm_pm);
        } else {
            analogAmpmTextView.setText(R.string.alarm_am);
        }

        // day differences
        setDayDifferencesText(analogDayDifferenceTextView, worldClock.getDayDifferences());

        // cityName - 첫 글자는 무조건 대문자로
        analogCityNameTextView.setText(worldClock.getUpperCasedTimeZoneString());
    }

    private void updateDigitalClockUI() {
        analogClockLayout.setVisibility(View.GONE);
        analogAmpmTextView.setVisibility(View.GONE);
        digitalClockLayout.setVisibility(View.VISIBLE);

        Calendar worldClockCalendar = worldClock.getWorldClockCalendar();

        // time
        int hour = worldClockCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = worldClockCalendar.get(Calendar.MINUTE);
        int second = worldClockCalendar.get(Calendar.SECOND);
        if (!isUsing24Hours) {
            if (hour == 0) {
                hour += 12;
            }
            if (hour > 12) {
                hour -= 12;
            }
        }

        String minuteString = String.valueOf(minute);
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        String colonString = second % 2 == 0 ? ":" : " ";
        String timeString = String.valueOf(hour) + colonString + minuteString;
        digitalTimeTextView.setText(timeString);

        // am/pm
        if (isUsing24Hours) {
            digitalAmpmTextView.setVisibility(View.GONE);
        } else {
            digitalAmpmTextView.setVisibility(View.VISIBLE);
            if (worldClockCalendar.get(Calendar.HOUR_OF_DAY) >= 12) {
                digitalAmpmTextView.setText("PM");
            } else {
                digitalAmpmTextView.setText("AM");
            }
        }

        // day differences
        setDayDifferencesText(digitalDayDifferenceTextView, worldClock.getDayDifferences());

        // cityName - 첫 글자는 무조건 대문자로
        digitalCityNameTextView.setText(worldClock.getUpperCasedTimeZoneString());
    }

    private void setDayDifferencesText(TextView dayDifferenceTextView, int dayDifference) {
        switch (dayDifference) {
            case -1:
                dayDifferenceTextView.setText(R.string.world_clock_yesterday);
                break;

            case 0:
                dayDifferenceTextView.setText(R.string.world_clock_today);
                break;
            case 1:
                dayDifferenceTextView.setText(R.string.world_clock_tomorrow);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void startClock() {
        if (isClockRunning) {
            return;
        }
        isClockRunning = true;

        int diffInMilli = (int) System.currentTimeMillis() % 1000;
        clockHandler.sendEmptyMessageDelayed(0, 1000 - diffInMilli);
    }

    private void stopClock() {
        if (!isClockRunning) {
            return;
        }
        isClockRunning = false;
    }

    // 뷰가 붙을 때 아날로그 시계뷰 재가동
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (analogClockView != null && isClockAnalog) {
            analogClockView.setFirstTick(true);     // 다시 붙을 때는 first tick rotate 를 실행
            try {
                // startClock으로도 시계가 맞추어지지만 가능하지만 tick을 기다려야 해서
                // 회전이 끝난 후에 UI갱신이 되는 경우가 있어 refreshPanel을 호출
                refreshPanel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startClock();
    }

    // 뷰가 사라질 때 아날로그 시계뷰 핸들러 중지
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopClock();
    }
}
