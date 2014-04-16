package com.yooiistudios.morningkit.panel.datecountdown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

public class MNDateCountdownDatePicker extends DatePicker {

	public MNDateCountdownDatePicker(Context context) {
		super(context);
		init();
	}
	
	public MNDateCountdownDatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MNDateCountdownDatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 넥서스 7에서 죽는 가능성 때문에 일단 주석 처
	private void init() {
		
		/*
		// 현재 폰트 색을 제너럴세팅의 색으로 변경 
		LinearLayout linearLayout = (LinearLayout)getChildAt(0);
		if (linearLayout != null) {
			linearLayout = (LinearLayout) linearLayout.getChildAt(0);
			if (linearLayout != null) {
				for (int i = 0; i < linearLayout.getChildCount(); i++) {
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
