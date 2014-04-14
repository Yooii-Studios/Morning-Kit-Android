package com.yooiistudios.morningkit.panel.calendar;

import android.content.Context;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 14.
 *
 * MNCalendarPanelLayout
 */
public class MNCalendarPanelLayout extends MNPanelLayout {

    protected static final String CALENDAR_DATA_SELECTED_CALEDNDARS = "CALENDAR_DATA_SELECTED_CALEDNDARS";

    public MNCalendarPanelLayout(Context context) {
        super(context);
    }

    public MNCalendarPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
    }

    @Override
    protected void updateUI() {
        super.updateUI();
    }
}
