package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    private RelativeLayout digialClockLayout;

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

    }

    private void initDigitalClock() {
        digialClockLayout = new RelativeLayout(getContext());
        LayoutParams digitalLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        digialClockLayout.setLayoutParams(digitalLayoutParams);

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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
