package com.yooiistudios.morningkit.panel.date;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNDatePanelLayout
 */
public class MNDatePanelLayout extends MNPanelLayout {
    private static final String TAG = "MNDatePanelLayout";
    protected static final String DATE_DATA_DATE_IS_LUNAR_ON = "DATE_DATA_DATE_IS_LUNAR_ON";

    boolean isLunarCalendarOn = false;

    RelativeLayout innerContentLayout;
    LinearLayout calendarLayout;
    LinearLayout lunarCalendarLayout;

    TextView monthTextView;
    TextView dayTextView;
    TextView dayOfWeekTextView;

    TextView lunarMonthTextView;
    TextView lunarDayTextView;
    TextView lunarDayOfWeekTextView;

    public MNDatePanelLayout(Context context) {
        super(context);
    }

    public MNDatePanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // containers
        innerContentLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams innerLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        innerLayoutParams.addRule(CENTER_IN_PARENT);
        innerContentLayout.setLayoutParams(innerLayoutParams);
        getContentLayout().addView(innerContentLayout);

        calendarLayout = new LinearLayout(getContext());
        calendarLayout.setId(4123412);
        calendarLayout.setOrientation(LinearLayout.VERTICAL);
        calendarLayout.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams calendarLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        calendarLayout.setLayoutParams(calendarLayoutParams);
        innerContentLayout.addView(calendarLayout);

        lunarCalendarLayout = new LinearLayout(getContext());
        lunarCalendarLayout.setOrientation(LinearLayout.VERTICAL);
        lunarCalendarLayout.setGravity(Gravity.CENTER);
        LayoutParams lunarCalendarLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarCalendarLayoutParams.addRule(CENTER_VERTICAL);
        lunarCalendarLayoutParams.addRule(RIGHT_OF, calendarLayout.getId());
        lunarCalendarLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.panel_layout_padding);
        lunarCalendarLayout.setLayoutParams(lunarCalendarLayoutParams);
        innerContentLayout.addView(lunarCalendarLayout);

        // calendar
        monthTextView = new TextView(getContext());
        monthTextView.setGravity(Gravity.CENTER);
        monthTextView.setSingleLine();
        monthTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_month_text_size));
        LinearLayout.LayoutParams monthTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        monthTextView.setLayoutParams(monthTextViewParams);
        calendarLayout.addView(monthTextView);

        dayTextView = new TextView(getContext());
        dayTextView.setGravity(Gravity.CENTER);
        dayTextView.setSingleLine();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_day_text_size));
        LinearLayout.LayoutParams dayTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayTextView.setLayoutParams(dayTextViewParams);
        calendarLayout.addView(dayTextView);

        dayOfWeekTextView = new TextView(getContext());
        dayOfWeekTextView.setGravity(Gravity.CENTER);
        dayOfWeekTextView.setSingleLine();
        dayOfWeekTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_day_of_week_text_size));
        LinearLayout.LayoutParams dayOfWeekTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayOfWeekTextView.setLayoutParams(dayOfWeekTextViewParams);
        calendarLayout.addView(dayOfWeekTextView);

        // lunar
        lunarMonthTextView = new TextView(getContext());
        lunarMonthTextView.setGravity(Gravity.CENTER);
        lunarMonthTextView.setSingleLine();
        lunarMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_month_text_size));
        LinearLayout.LayoutParams lunarMonthTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarMonthTextView.setLayoutParams(lunarMonthTextViewParams);
        lunarCalendarLayout.addView(lunarMonthTextView);

        lunarDayTextView = new TextView(getContext());
        lunarDayTextView.setGravity(Gravity.CENTER);
        lunarDayTextView.setSingleLine();
        lunarDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_day_text_size));
        LinearLayout.LayoutParams lunarDayTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarDayTextView.setLayoutParams(lunarDayTextViewParams);
        lunarCalendarLayout.addView(lunarDayTextView);

        lunarDayOfWeekTextView = new TextView(getContext());
        lunarDayOfWeekTextView.setGravity(Gravity.CENTER);
        lunarDayOfWeekTextView.setSingleLine();
        lunarDayOfWeekTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_day_of_week_text_size));
        LinearLayout.LayoutParams lunarDayOfWeekTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarDayOfWeekTextView.setLayoutParams(lunarDayOfWeekTextViewParams);
        lunarCalendarLayout.addView(lunarDayOfWeekTextView);

        // test
        innerContentLayout.setBackgroundColor(Color.YELLOW);
        calendarLayout.setBackgroundColor(Color.GREEN);
        lunarCalendarLayout.setBackgroundColor(Color.BLUE);

        monthTextView.setBackgroundColor(Color.CYAN);
        monthTextView.setText("MARCH");
        dayTextView.setBackgroundColor(Color.MAGENTA);
        dayTextView.setText("12");
        dayOfWeekTextView.setBackgroundColor(Color.RED);
        dayOfWeekTextView.setText("WEDNESDAY");

        lunarMonthTextView.setBackgroundColor(Color.CYAN);
        lunarMonthTextView.setText("FEBRUARY");
        lunarDayTextView.setBackgroundColor(Color.MAGENTA);
        lunarDayTextView.setText("12");
        lunarDayOfWeekTextView.setBackgroundColor(Color.RED);
        lunarDayOfWeekTextView.setText("WEDNESDAY");
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
        isLunarCalendarOn = getPanelDataObject().getBoolean(DATE_DATA_DATE_IS_LUNAR_ON);
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (isLunarCalendarOn) {
            // 음력 달력 사용
        } else {
            // 양력 달력만 사용
        }
    }
}
