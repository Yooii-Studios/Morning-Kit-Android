package com.yooiistudios.morningkit.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yooiistudios.morningkit.MN;
import com.yooiistudios.morningkit.common.dp.DipToPixel;

import lombok.Getter;

/**
 * Created by yongbinbae on 13. 10. 22..
 * MNWidgetWindowLayout
 */
public class MNWidgetWindowLayout extends LinearLayout
{
    private static final String TAG = "MNWidgetWindowLayout";

    @Getter private LinearLayout widgetRows[];
    @Getter private FrameLayout[][] widgetSlots;

    public MNWidgetWindowLayout(Context context)
    {
        super(context);
    }

    public MNWidgetWindowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MNWidgetWindowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initWithWidgetMatrix()
    {
        widgetRows = new LinearLayout[2];
        widgetSlots = new FrameLayout[2][2];

        int padding = DipToPixel.getPixel(getContext(), 3);
        this.setOrientation(VERTICAL);
        this.setPadding(padding, padding, padding, padding);
        this.setBackgroundColor(Color.RED);

        for (int i=0; i<2; i++)
        {
            widgetRows[i] = new LinearLayout(getContext());
            widgetRows[i].setOrientation(HORIZONTAL);
            this.addView(widgetRows[i]);

            for (int j=0; j<2; j++)
            {
                widgetSlots[i][j] = new FrameLayout(getContext());

                if (getContext() != null) {
                    Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
//                display.getSize(size);
                    // deprecated 되서 동현이 말 듣고 수정
//                int width = display.getWidth();
//                int height = display.getHeight();
                    int width = size.x;
                    int height = size.y;

//                    Log.i(TAG, "" + width + " " + height);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width/2,
                            DipToPixel.getPixel(getContext(), MN.widget.WIDGET_HEIGHT_PHONE_DP));
                    widgetSlots[i][j].setLayoutParams(params);
                    widgetSlots[i][j].setPadding(3, 3, 3, 3);

                    widgetSlots[i][j].setBackgroundColor(Color.BLUE); // test
                    widgetRows[i].addView(widgetSlots[i][j]);
                }
            }
        }
    }
}
