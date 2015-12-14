package com.yooiistudios.morningkit.panel.calendar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.calendar.adapter.MNCalendarListAdapter;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarSelectDialog;
import com.yooiistudios.morningkit.panel.calendar.model.MNCalendarUtils;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;

import org.json.JSONException;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.yooiistudios.morningkit.panel.calendar.MNCalendarPanelLayout.CALENDAR_DATA_SELECTED_CALEDNDARS;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 14.
 *
 * MNCalendarDetailFragment
 */
public class MNCalendarDetailFragment extends MNPanelDetailFragment implements MNCalendarSelectDialog.MNCalendarSelectDialogListener {
    public static final int REQ_PERMISSION_READ_CALENDAR = 135;

    @InjectView(R.id.panel_calendar_detail_scrollview) ScrollView scrollView;
    @InjectView(R.id.panel_calendar_detail_events_listview) ListView eventsListView;
    @InjectView(R.id.panel_calendar_detail_select_calendars_image_view) ImageView selectCalendarsImageView;
    @InjectView(R.id.panel_calendar_detail_no_schedule_textview) TextView noScheduleTextView;

    boolean[] selectedArr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout
                .panel_calendar_detail_fragment, container, false);
        if (rootView != null && savedInstanceState == null) {
            ButterKnife.inject(this, rootView);

            if (DEBUG_UI) {
                eventsListView.setBackgroundColor(Color.MAGENTA);
            }

            // 패널 데이터 가져오기
            if (getPanelDataObject().has(CALENDAR_DATA_SELECTED_CALEDNDARS)) {
                String calendarModelsJsonString;
                try {
                    calendarModelsJsonString = getPanelDataObject().getString(CALENDAR_DATA_SELECTED_CALEDNDARS);
                    if (calendarModelsJsonString != null) {
                        Type type = new TypeToken<boolean[]>(){}.getType();
                        selectedArr = new Gson().fromJson(calendarModelsJsonString, type);
                        refreshUI();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 데이터가 없으면 최근 저장했던 사항들을 읽어오기
                selectedArr = MNCalendarUtils.loadCalendarModels(getActivity());
                refreshUI();
            }

            eventsListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    if (rootView instanceof ViewGroup) {
                        ((ViewGroup)rootView)
                                .requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }
            });
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 아이콘 이미지뷰 색 필터 적용
        //noinspection deprecation
        int highlightColor = getResources().getColor(R.color.pastel_green_sub_font_color);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor,
                PorterDuff.Mode.SRC_ATOP);
        selectCalendarsImageView.setColorFilter(colorFilter);
    }

    private void refreshUI() {
        eventsListView.setAdapter(new MNCalendarListAdapter(getActivity(), selectedArr));

        if (eventsListView.getCount() != 0) {
            noScheduleTextView.setVisibility(View.GONE);
        } else {
            noScheduleTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void archivePanelData() throws JSONException {
        try {
            getPanelDataObject().put(MNCalendarPanelLayout.CALENDAR_DATA_SELECTED_CALEDNDARS,
                    new Gson().toJson(selectedArr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.panel_calendar_detail_select_calendars_layout)
    void selectCalendarButtonClicked() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            showSelectCalendarDialog();
        } else {
            requestReadCalendarPermission();
        }
    }

    private void showSelectCalendarDialog() {
        AlertDialog calendarSelectDialog = MNCalendarSelectDialog.makeDialog(getActivity(), this,
                MNCalendarUtils.loadCalendarModels(getActivity()));
        calendarSelectDialog.show();
    }

    private void requestReadCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_CALENDAR)) {
            Snackbar.make(scrollView, R.string.need_permission_calendar, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{ Manifest.permission.READ_CALENDAR },
                                    REQ_PERMISSION_READ_CALENDAR);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ Manifest.permission.READ_CALENDAR }, REQ_PERMISSION_READ_CALENDAR);
        }
    }

    @Override
    public void onSelectCalendars(boolean[] selectedArr) {
        try {
            this.selectedArr = selectedArr;
            archivePanelData();
            MNCalendarUtils.saveCalendarModels(selectedArr, getActivity());
            refreshUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
