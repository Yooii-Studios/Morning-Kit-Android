package com.yooiistudios.morningkit.panel.datecountdown;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.json.JSONException;

import java.lang.reflect.Type;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 3.
 *
 * MNDateCountdownLayout
 */
public class MNDateCountdownPanelLayout extends MNPanelLayout {

    private static final String TAG = "MNDateCountdownLayout";

    protected static final String DATE_COUNTDOWN_DATA_TITLE = "DATE_COUNTDOWN_DATA_TITLE";
    protected static final String DATE_COUNTDOWN_IS_NEW_YEAR = "DATE_COUNTDOWN_IS_NEW_YEAR";
    protected static final String DATE_COUNTDOWN_DATA_DATE = "DATE_COUNTDOWN_DATA_DATE";

    private TextView titleTextView;
    private TextView countTextView;
    private TextView dateTextView;

    private String titleString;
    private MNDate targetDate;
    private int count;

    public MNDateCountdownPanelLayout(Context context) {
        super(context);
    }

    public MNDateCountdownPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // title
        titleTextView = new TextView(getContext());
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_outer);
        titleLayoutParams.setMargins(margin, 0, margin, 0);
        titleTextView.setLayoutParams(titleLayoutParams);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_countdown_title_font_size));
        getContentLayout().addView(titleTextView);

        // count
        countTextView = new TextView(getContext());
        countTextView.setId(3123784);
        RelativeLayout.LayoutParams countLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        countLayoutParams.setMargins(margin, 0, margin, 0);
        countLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        countTextView.setLayoutParams(countLayoutParams);
        countTextView.setGravity(Gravity.CENTER);
        countTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_countdown_count_font_size));
        getContentLayout().addView(countTextView);

        // date
        dateTextView = new TextView(getContext());
        RelativeLayout.LayoutParams dateLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dateLayoutParams.setMargins(margin, 0, margin, 0);
        dateTextView.setLayoutParams(dateLayoutParams);
        dateTextView.setGravity(Gravity.CENTER);
        dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.panel_date_countdown_date_font_size));
        getContentLayout().addView(dateTextView);

        // align layouts
        titleLayoutParams.addRule(RelativeLayout.ABOVE, countTextView.getId());
        dateLayoutParams.addRule(RelativeLayout.BELOW, countTextView.getId());
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // 패널 데이터 가져오기
        if (getPanelDataObject().has(DATE_COUNTDOWN_DATA_TITLE) &&
                getPanelDataObject().has(DATE_COUNTDOWN_DATA_DATE)) {
            try {
                // 기존 정보가 있다면 가져와서 표시
                // title
                titleString = getPanelDataObject().getString(DATE_COUNTDOWN_DATA_TITLE);

                // date - JSONString에서 클래스로 캐스팅
                Type type = new TypeToken<MNDate>(){}.getType();
                String dateJsonString = getPanelDataObject().getString(DATE_COUNTDOWN_DATA_DATE);
                targetDate = new Gson().fromJson(dateJsonString, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // 기존 정보가 없다면 새해 표시
            titleString = getResources().getString(R.string.date_countdown_new_year);
            targetDate = MNDefaultDateMaker.getDefaultDate();
        }

        // targetDate -> count 계산
        count = caculateCountdown(targetDate);
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        // title
        titleTextView.setText(titleString);

        // count
        if (count == 0) {
            countTextView.setText(R.string.world_clock_today);
        } else if (count > 0) {
            countTextView.setText(String.format("D-%d", count));
        } else {
            countTextView.setText(String.format("D+%d", -1 * (count - 1)));
        }

        // date
        Chronology chrono = GregorianChronology.getInstance();
        LocalDate targetLocalDate = new LocalDate(targetDate.getYear(), targetDate.getMonth(), targetDate.getDay(), chrono);
        String pattern = "yyyy.MM.dd";
        dateTextView.setText(targetLocalDate.toString(pattern));
    }

    public int caculateCountdown(MNDate date) {
        Chronology chrono = GregorianChronology.getInstance();
        LocalDate targetLocalDate = new LocalDate(targetDate.getYear(), targetDate.getMonth(), targetDate.getDay(), chrono);

        DateTime todayDateTime = new DateTime();
        LocalDate todayDate = todayDateTime.toLocalDate();

        return Days.daysBetween(todayDate, targetLocalDate).getDays();
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext());

        // text views
        titleTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, getContext()));
        countTextView.setTextColor(MNMainColors.getMainFontColor(currentThemeType, getContext()));
        dateTextView.setTextColor(MNMainColors.getSubFontColor(currentThemeType, getContext()));
    }
}
