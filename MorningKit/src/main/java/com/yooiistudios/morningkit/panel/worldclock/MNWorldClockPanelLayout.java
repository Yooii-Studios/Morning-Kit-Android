package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.graphics.Color;
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
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZone;
import com.yooiistudios.morningkit.panel.worldclock.model.MNTimeZoneLoader;

import org.json.JSONException;

import java.lang.reflect.Type;

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
    private TextView analogAmpmTextView;
    private TextView analogDayDifferencesTextView;
    private TextView analogCityNameTextView;

    private RelativeLayout digitalClockLayout;
    private RelativeLayout digitalTimeLayout;
    private TextView digitalTimeTextView;
    private TextView digitalAmpmTextView;
    private TextView digitalDayDifferencesTextView;
    private TextView digitalCityNameTextView;

    private boolean isClockAnalog = true;
    private boolean isUsing24Hours = false;
    private MNTimeZone selectedTimeZone;

    public MNWorldClockPanelLayout(Context context) {
        super(context);
    }

    public MNWorldClockPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        initAnalogClock();
        initDigitalClock();
    }

    private void initAnalogClock() {
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
        View analogView = new View(getContext());
        analogView.setId(9173751);
        LayoutParams analogViewParams = new LayoutParams(analogViewSize, analogViewSize);
        analogViewParams.addRule(CENTER_HORIZONTAL);
        analogView.setLayoutParams(analogViewParams);
        analogClockLayout.addView(analogView);

        // day differences
        analogDayDifferencesTextView = new TextView(getContext());
        analogDayDifferencesTextView.setId(1859182);
        analogDayDifferencesTextView.setGravity(Gravity.CENTER);
        LayoutParams dayDiffLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayDiffLayoutParams.addRule(CENTER_HORIZONTAL);
        dayDiffLayoutParams.addRule(BELOW, analogView.getId());
        analogDayDifferencesTextView.setLayoutParams(dayDiffLayoutParams);
        analogClockLayout.addView(analogDayDifferencesTextView);

        // city name
        analogCityNameTextView = new TextView(getContext());
        analogCityNameTextView.setGravity(Gravity.CENTER);
        LayoutParams cityNameLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cityNameLayoutParams.addRule(CENTER_HORIZONTAL);
        cityNameLayoutParams.addRule(BELOW, analogDayDifferencesTextView.getId());
        analogCityNameTextView.setLayoutParams(cityNameLayoutParams);
        analogClockLayout.addView(analogCityNameTextView);

        // test
        analogClockLayout.setBackgroundColor(Color.BLUE);
        analogAmpmTextView.setBackgroundColor(Color.CYAN);
        analogAmpmTextView.setText("AM");
        analogView.setBackgroundColor(Color.RED);
        analogDayDifferencesTextView.setBackgroundColor(Color.MAGENTA);
        analogDayDifferencesTextView.setText("Today");
        analogCityNameTextView.setBackgroundColor(Color.GREEN);
        analogCityNameTextView.setText("Milwaukee, WI");
    }

    private void initDigitalClock() {
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
        digitalTimeLayout.addView(digitalTimeTextView);

        // ampm
        digitalAmpmTextView = new TextView(getContext());
        digitalAmpmTextView.setGravity(Gravity.BOTTOM);
        LayoutParams ampmLayoutParms = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ampmLayoutParms.addRule(ALIGN_BASELINE, digitalTimeTextView.getId());
        ampmLayoutParms.addRule(RIGHT_OF, digitalTimeTextView.getId());
        digitalAmpmTextView.setLayoutParams(ampmLayoutParms);
        digitalTimeLayout.addView(digitalAmpmTextView);

        // day differences
        digitalDayDifferencesTextView = new TextView(getContext());
        digitalDayDifferencesTextView.setId(8159182);
        digitalDayDifferencesTextView.setGravity(Gravity.CENTER);
        LayoutParams dayDiffLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayDiffLayoutParams.addRule(CENTER_HORIZONTAL);
        dayDiffLayoutParams.addRule(BELOW, digitalTimeLayout.getId());
        digitalDayDifferencesTextView.setLayoutParams(dayDiffLayoutParams);
        digitalClockLayout.addView(digitalDayDifferencesTextView);

        // city name
        digitalCityNameTextView = new TextView(getContext());
        digitalCityNameTextView.setGravity(Gravity.CENTER);
        LayoutParams cityNameLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cityNameLayoutParams.addRule(CENTER_HORIZONTAL);
        cityNameLayoutParams.addRule(BELOW, digitalDayDifferencesTextView.getId());
        digitalCityNameTextView.setLayoutParams(cityNameLayoutParams);
        digitalClockLayout.addView(digitalCityNameTextView);

        // test
        digitalClockLayout.setBackgroundColor(Color.BLUE);
        digitalTimeLayout.setBackgroundColor(Color.RED);
        digitalTimeTextView.setBackgroundColor(Color.YELLOW);
        digitalTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DipToPixel.dpToPixel(getContext(), 40));
        digitalTimeTextView.setText("3:41");
        digitalAmpmTextView.setBackgroundColor(Color.CYAN);
        digitalAmpmTextView.setText("AM");
        digitalDayDifferencesTextView.setBackgroundColor(Color.MAGENTA);
        digitalDayDifferencesTextView.setText("Today");
        digitalCityNameTextView.setBackgroundColor(Color.GREEN);
        digitalCityNameTextView.setText("Milwaukee, WI");
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
        if (getPanelDataObject().has(WORLD_CLOCK_DATA_TIME_ZONE)) {
            Type type = new TypeToken<MNTimeZone>() {}.getType();
            String timeZoneJsonString = getPanelDataObject().getString(WORLD_CLOCK_DATA_TIME_ZONE);
            selectedTimeZone = new Gson().fromJson(timeZoneJsonString, type);
            MNLog.i(TAG, "selectedTimeZone: " + selectedTimeZone.getName());
        } else {
            selectedTimeZone = MNTimeZoneLoader.getDefaultZone(getContext());
            MNLog.i(TAG, "default time zone: " + selectedTimeZone.getName());
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (isClockAnalog) {
            analogClockLayout.setVisibility(View.VISIBLE);
            analogAmpmTextView.setVisibility(View.VISIBLE);
            digitalClockLayout.setVisibility(View.GONE);
        } else {
            analogClockLayout.setVisibility(View.GONE);
            analogAmpmTextView.setVisibility(View.GONE);
            digitalClockLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
