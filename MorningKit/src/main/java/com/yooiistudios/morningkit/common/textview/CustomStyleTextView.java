package com.yooiistudios.morningkit.common.textview;

import android.content.Context;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2016. 3. 20.
 *
 * CustomStyleTextView
 *  설정화면 상점 탭의 색을 커스터마이징 하기 위한 클래스
 */
public class CustomStyleTextView extends TextView {

    public CustomStyleTextView(Context context, int styleAttribute) {
        super(context, null, styleAttribute);
    }

    // You could also just apply your default style if none is given
    public CustomStyleTextView(Context context) {
        super(context, null, R.attr.storeTabTextStyle);
    }
}
