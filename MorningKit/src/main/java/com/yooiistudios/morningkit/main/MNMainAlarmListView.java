package com.yooiistudios.morningkit.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.yooiistudios.morningkit.alarm.listview.MNAlarmListAdapter;
import com.yooiistudios.morningkit.alarm.listview.item.MNAlarmItemClickListener;
import com.yooiistudios.morningkit.alarm.model.MNAlarmListManager;

import lombok.Getter;

/**
 * Created by Steven Kim on 13. 10. 22..
 *
 * MNMainAlarmListView
 *  메인화면의 알람리스트뷰
 */
public class MNMainAlarmListView extends ListView
{
    private static final String TAG = "MNMainAlarmListView";
    @Getter private MNAlarmItemClickListener alarmItemClickListener;

    /**
     * Constructor
     */
    public MNMainAlarmListView(Context context) {
        super(context);
        init();
    }

    public MNMainAlarmListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MNMainAlarmListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    public void initWithListAdapter() {
        alarmItemClickListener = MNAlarmItemClickListener.newInstance(this);
        setAdapter(new MNAlarmListAdapter(getContext(), alarmItemClickListener));
    }

    public void refreshListView() {
        MNAlarmListManager.loadAlarmList(getContext());
        ((MNAlarmListAdapter) getAdapter()).notifyDataSetChanged();
    }
}
