package com.yooiistudios.morningkit.panel.date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.date.model.DateUtil;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 12.
 *
 * MNDatePanelLayout
 */
public class MNDatePanelLayout extends MNPanelLayout {
    private static final String TAG = "MNDatePanelLayout";
    protected static final String DATE_DATA_DATE_IS_LUNAR_ON = "DATE_DATA_DATE_IS_LUNAR_ON";

    // UI
    private RelativeLayout innerContentLayout;
    private LinearLayout calendarLayout;
    private LinearLayout lunarCalendarLayout;

    private TextView monthTextView;
    private TextView dayTextView;
    private TextView dayOfWeekTextView;

    private TextView lunarMonthTextView;
    private TextView lunarDayTextView;
    private TextView lunarDayOfWeekTextView;

    // Model
    private boolean isLunarCalendarOn = false;
    private Date todayDate;
    private Date lunarDate;

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
        final LayoutParams lunarCalendarLayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarCalendarLayoutParams.addRule(CENTER_VERTICAL);
        lunarCalendarLayoutParams.addRule(RIGHT_OF, calendarLayout.getId());
        lunarCalendarLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.panel_date_margin_between_dates);
        lunarCalendarLayout.setLayoutParams(lunarCalendarLayoutParams);
        innerContentLayout.addView(lunarCalendarLayout);

        // calendar
        int minusMargin = getResources().getDimensionPixelSize(R.dimen.panel_date_minus_margin);
        monthTextView = new TextView(getContext());
        monthTextView.setGravity(Gravity.CENTER);
        monthTextView.setSingleLine();
        monthTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_month_text_size));
//        monthTextView.setTypeface(monthTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams monthTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        monthTextViewParams.setMargins(0, 0, 0, minusMargin);
        monthTextView.setLayoutParams(monthTextViewParams);
        calendarLayout.addView(monthTextView);

        dayTextView = new TextView(getContext());
        dayTextView.setGravity(Gravity.CENTER);
        dayTextView.setSingleLine();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_day_text_size));
        dayTextView.setTypeface(dayTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams dayTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayTextView.setLayoutParams(dayTextViewParams);
        calendarLayout.addView(dayTextView);

        dayOfWeekTextView = new TextView(getContext());
        dayOfWeekTextView.setGravity(Gravity.CENTER);
        dayOfWeekTextView.setSingleLine();
        dayOfWeekTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_day_of_week_text_size));
//        dayOfWeekTextView.setTypeface(dayOfWeekTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams dayOfWeekTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        dayOfWeekTextViewParams.setMargins(0, minusMargin, 0, 0);
        dayOfWeekTextView.setLayoutParams(dayOfWeekTextViewParams);
        calendarLayout.addView(dayOfWeekTextView);

        // lunar
        int lunarMinusMargin = getResources().getDimensionPixelSize(R.dimen.panel_date_lunar_minus_margin);
        lunarMonthTextView = new TextView(getContext());
        lunarMonthTextView.setGravity(Gravity.CENTER);
        lunarMonthTextView.setSingleLine();
        lunarMonthTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_month_text_size));
//        lunarMonthTextView.setTypeface(lunarMonthTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams lunarMonthTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarMonthTextViewParams.setMargins(0, 0, 0, lunarMinusMargin);
        lunarMonthTextView.setLayoutParams(lunarMonthTextViewParams);
        lunarCalendarLayout.addView(lunarMonthTextView);

        lunarDayTextView = new TextView(getContext());
        lunarDayTextView.setGravity(Gravity.CENTER);
        lunarDayTextView.setSingleLine();
        lunarDayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_day_text_size));
        lunarDayTextView.setTypeface(lunarDayTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams lunarDayTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarDayTextView.setLayoutParams(lunarDayTextViewParams);
        lunarCalendarLayout.addView(lunarDayTextView);

        lunarDayOfWeekTextView = new TextView(getContext());
        lunarDayOfWeekTextView.setGravity(Gravity.CENTER);
        lunarDayOfWeekTextView.setSingleLine();
        lunarDayOfWeekTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_lunar_day_of_week_text_size));
//        lunarDayOfWeekTextView.setTypeface(lunarDayOfWeekTextView.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams lunarDayOfWeekTextViewParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lunarDayOfWeekTextViewParams.setMargins(0, lunarMinusMargin, 0, 0);
        lunarDayOfWeekTextView.setLayoutParams(lunarDayOfWeekTextViewParams);
        lunarCalendarLayout.addView(lunarDayOfWeekTextView);

        // test
        if (DEBUG_UI) {
            innerContentLayout.setBackgroundColor(Color.YELLOW);
            calendarLayout.setBackgroundColor(Color.GREEN);
            lunarCalendarLayout.setBackgroundColor(Color.BLUE);

            monthTextView.setBackgroundColor(Color.CYAN);
            monthTextView.setText("March");
            dayTextView.setBackgroundColor(Color.MAGENTA);
            dayTextView.setText("12");
            dayOfWeekTextView.setBackgroundColor(Color.RED);
            dayOfWeekTextView.setText("WED");

            lunarMonthTextView.setBackgroundColor(Color.CYAN);
            lunarMonthTextView.setText("Feb");
            lunarDayTextView.setBackgroundColor(Color.MAGENTA);
            lunarDayTextView.setText("12");
            lunarDayOfWeekTextView.setBackgroundColor(Color.RED);
            lunarDayOfWeekTextView.setText("WED");
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        if (getPanelDataObject().has(DATE_DATA_DATE_IS_LUNAR_ON)) {
            isLunarCalendarOn = getPanelDataObject().getBoolean(DATE_DATA_DATE_IS_LUNAR_ON);
        } else {
            isLunarCalendarOn = false;
        }

        todayDate = new Date();

        if (isLunarCalendarOn) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayDateString = simpleDateFormat.format(todayDate);
            try {
                lunarDate = DateUtil.getLunaDate(todayDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        if (isLunarCalendarOn && lunarDate != null) {
            // 음력 달력 사용
            lunarCalendarLayout.setVisibility(View.VISIBLE);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
            lunarMonthTextView.setText(simpleDateFormat.format(lunarDate));
            simpleDateFormat = new SimpleDateFormat("dd");
            lunarDayTextView.setText(simpleDateFormat.format(lunarDate));
            simpleDateFormat = new SimpleDateFormat("EEEE");
            lunarDayOfWeekTextView.setText(simpleDateFormat.format(lunarDate));
        } else {
            // 양력 달력만 사용
            lunarCalendarLayout.setVisibility(View.GONE);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        monthTextView.setText(simpleDateFormat.format(todayDate));
        simpleDateFormat = new SimpleDateFormat("dd");
        dayTextView.setText(simpleDateFormat.format(todayDate));
        simpleDateFormat = new SimpleDateFormat("EEEE");
        dayOfWeekTextView.setText(simpleDateFormat.format(todayDate));
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        post(new Runnable() {
            @Override
            public void run() {
                final LayoutParams lunarCalendarLayoutParams
                        = (LayoutParams) lunarCalendarLayout.getLayoutParams();

                // 적당한 너비로 맞추어주자. 나중에 아티스트와 함께 다시 맞출 것.
                if (lunarCalendarLayoutParams != null) {
                    lunarCalendarLayoutParams.leftMargin =
                            (int) ((getWidth() - lunarCalendarLayout.getWidth() - calendarLayout.getWidth()) / 3 * 0.8);
//                    requestLayout();

//                    lunarCalendarLayout.requestLayout();
//                    lunarCalendarLayout.invalidate();

                    innerContentLayout.requestLayout();
                    innerContentLayout.invalidate();
                }
            }
        });

        // View.post 방식은 한박자 늦은 느낌이라 이것을 사용
        // 이것은 아예 제대로 안될 경우가 있다(갤노트 2에서)
//        MNViewSizeMeasure.setViewSizeObserver(this, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
//            @Override
//            public void onLayoutLoad() {
//                final LayoutParams lunarCalendarLayoutParams
//                        = (LayoutParams) lunarCalendarLayout.getLayoutParams();
//
//                // 적당한 너비로 맞추어주자. 나중에 아티스트와 함께 다시 맞출 것.
//                if (lunarCalendarLayoutParams != null) {
//                    lunarCalendarLayoutParams.leftMargin =
//                            (int) ((getWidth() - lunarCalendarLayout.getWidth() - calendarLayout.getWidth()) / 3 * 0.8);
//                    requestLayout();
//                }
//            }
//        });
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());

        int mainFontColor = MNMainColors.getMainFontColor(currentThemeType,
                getContext().getApplicationContext());
        int subFontColor = MNMainColors.getSubFontColor(currentThemeType,
                getContext().getApplicationContext());

        monthTextView.setTextColor(subFontColor);
        dayTextView.setTextColor(mainFontColor);
        dayOfWeekTextView.setTextColor(subFontColor);

        lunarMonthTextView.setTextColor(subFontColor);
        lunarDayTextView.setTextColor(subFontColor);
        lunarDayOfWeekTextView.setTextColor(subFontColor);
    }
}
