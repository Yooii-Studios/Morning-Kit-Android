package com.yooiistudios.morningkit.panel.calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.panel.calendar.adapter.MNCalendarMainListAdapter;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarUtils;
import com.yooiistudios.morningkit.panel.calendar.ui.MNUnClickableListView;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

import java.lang.reflect.Type;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 14.
 *
 * MNCalendarPanelLayout
 */
public class MNCalendarPanelLayout extends MNPanelLayout {

    protected static final String CALENDAR_DATA_SELECTED_CALEDNDARS = "CALENDAR_DATA_SELECTED_CALEDNDARS";

    private MNUnClickableListView eventsListView;
    private boolean[] selectedArr;

    public MNCalendarPanelLayout(Context context) {
        super(context);
    }
    public MNCalendarPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        eventsListView = new MNUnClickableListView(getContext());
        eventsListView.setDividerHeight(0);
        int topPadding = DipToPixel.dpToPixel(getContext(), 1);
        eventsListView.setPadding(0, topPadding, 0, 0);
        LayoutParams eventsListViewLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int marginInner = getResources().getDimensionPixelSize(R.dimen.margin_inner);
        eventsListViewLayoutParams.setMargins(marginInner, marginInner, marginInner, marginInner);
        eventsListView.setLayoutParams(eventsListViewLayoutParams);
        eventsListView.setFocusable(false);
        eventsListView.setClickable(false);
        getContentLayout().addView(eventsListView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // 패널 데이터 가져오기
        if (getPanelDataObject().has(CALENDAR_DATA_SELECTED_CALEDNDARS)) {
            String calendarModelsJsonString;
            try {
                calendarModelsJsonString = getPanelDataObject().getString(CALENDAR_DATA_SELECTED_CALEDNDARS);
                if (calendarModelsJsonString != null) {
                    Type type = new TypeToken<boolean[]>(){}.getType();
                    selectedArr = new Gson().fromJson(calendarModelsJsonString, type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 데이터가 없으면 최근 저장했던 사항들을 읽어오기
            selectedArr = MNCalendarUtils.loadCalendarModels(getContext());
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        eventsListView.setAdapter(new MNCalendarMainListAdapter(getContext(), selectedArr));

        if (eventsListView.getCount() != 0) {
            hideCoverLayout();
        } else {
            showCoverLayout(getResources().getString(R.string.reminder_no_schedule));
        }

        // test
        if (DEBUG_UI) {
            eventsListView.setBackgroundColor(Color.MAGENTA);
        }
    }
}
