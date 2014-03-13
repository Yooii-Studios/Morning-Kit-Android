package com.yooiistudios.morningkit.panel.worldclock;

import android.content.Context;
import android.util.AttributeSet;

import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNWorldClockPanelLayout
 */
public class MNWorldClockPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNWorldClockPanelLayout";
    protected static final String WORLD_CLOCK_DATA_IS_ALALOG = "WORLD_CLOCK_DATA_IS_ALALOG";
    protected static final String WORLD_CLOCK_DATA_IS_24_HOUR = "WORLD_CLOCK_DATA_IS_24_HOUR";

    public MNWorldClockPanelLayout(Context context) {
        super(context);
    }

    public MNWorldClockPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
