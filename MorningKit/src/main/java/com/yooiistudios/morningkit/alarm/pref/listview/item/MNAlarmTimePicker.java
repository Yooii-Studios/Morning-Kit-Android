package com.yooiistudios.morningkit.alarm.pref.listview.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

public class MNAlarmTimePicker extends TimePicker {

	public MNAlarmTimePicker(Context context) {
		super(context);
		init();
	}
	
	public MNAlarmTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MNAlarmTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 일단 넥서스7에서 죽기 때문에 이 부분은 뺌.
	private void init() {
		
		/*
		// 현재 폰트 색을 제너럴세팅의 색으로 변경 
		LinearLayout linearLayout = (LinearLayout)getChildAt(0);
		if (linearLayout != null) {
			for (int i = 0; i < linearLayout.getChildCount(); i++) {
				if (i != 1) {
					NumberPicker numberPicker = (NumberPicker)linearLayout.getChildAt(i);
					if (numberPicker != null) {
						EditText editText = (EditText)numberPicker.getChildAt(1);
						if (editText != null) {
							editText.setTextColor(GeneralSetting.getMainFontColor());	
						}
					}
				}
			}
		}
		*/
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
	    if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
	    {
	        ViewParent p = getParent();
	        if (p != null)
	            p.requestDisallowInterceptTouchEvent(true);
	    }

	    return false;
	}

}
