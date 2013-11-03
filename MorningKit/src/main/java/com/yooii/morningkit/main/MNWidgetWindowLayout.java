package com.yooii.morningkit.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yooii.morningkit.MN;
import com.yooii.morningkit.common.DipToPixel;

/**
 * Created by yongbinbae on 13. 10. 22..
 */
public class MNWidgetWindowLayout extends LinearLayout
{

    private static final String TAG = "MNWidgetWindowLayout";
    private Context m_Context;

    private LinearLayout m_WidgetRows[];
    private FrameLayout[][] widgetSlots;

    public MNWidgetWindowLayout(Context context)
    {
        super(context);

        m_Context = context;
    }

    public MNWidgetWindowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        m_Context = context;
    }

    public MNWidgetWindowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        m_Context = context;
    }

    public void initWithWidgetMatrix()
    {
        m_WidgetRows = new LinearLayout[2];
        widgetSlots = new FrameLayout[2][2];

        int padding = DipToPixel.getPixel(m_Context, 3);
        this.setOrientation(VERTICAL);
        this.setPadding(padding, padding, padding, padding);
        this.setBackgroundColor(Color.RED);

        for (int i=0; i<2; i++)
        {
            m_WidgetRows[i] = new LinearLayout(m_Context);
            m_WidgetRows[i].setOrientation(HORIZONTAL);
            this.addView(m_WidgetRows[i]);

            for (int j=0; j<2; j++)
            {
                widgetSlots[i][j] = new FrameLayout(m_Context);

                Display display = ((Activity) m_Context).getWindowManager().getDefaultDisplay();
                Point size = new Point();
//                display.getSize(size);
                // deprecated 되서 동현이 말 듣고 수정 
//                int width = display.getWidth();
//                int height = display.getHeight();
                int width = size.x;
                int height = size.y;

                Log.i(TAG, "" + width + " " + height);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width/2,
                        DipToPixel.getPixel(m_Context, MN.widget.WIDGET_HEIGHT_PHONE_DP));
                widgetSlots[i][j].setLayoutParams(params);
                widgetSlots[i][j].setPadding(3, 3, 3, 3);

                widgetSlots[i][j].setBackgroundColor(Color.BLUE); // test
                m_WidgetRows[i].addView(widgetSlots[i][j]);
            }
        }
    }
}
