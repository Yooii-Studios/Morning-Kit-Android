package com.yooiistudios.morningkit.panel.datecountdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailFragment;

import net.simonvt.datepicker.DatePicker;

import org.json.JSONException;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownPanelLayout.DATE_COUNTDOWN_DATA_DATE;
import static com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownPanelLayout.DATE_COUNTDOWN_DATA_TITLE;
import static com.yooiistudios.morningkit.panel.datecountdown.MNDateCountdownPanelLayout.DATE_COUNTDOWN_IS_NEW_YEAR;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 28.
 *
 * MNDateCountdownDetailFragment
 */
public class MNDateCountdownDetailFragment extends MNPanelDetailFragment {

    private static final String TAG = "MNDateCountdownDetailFragment";

    @InjectView(R.id.date_countdown_detail_edittext) EditText titleEditText;
//    @InjectView(R.id.date_countdown_detail_date_picker) MNDateCountdownDatePicker datePicker;
    @InjectView(R.id.date_countdown_detail_custom_date_picker) DatePicker customDatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_date_countdown_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            // 패널 데이터 가져오기
            if (getPanelDataObject().has(DATE_COUNTDOWN_DATA_TITLE) &&
                    getPanelDataObject().has(DATE_COUNTDOWN_DATA_DATE)) {
                try {
                    // 기존 정보가 있다면 가져와서 표시
                    // title
                    titleEditText.setText(getPanelDataObject().getString(DATE_COUNTDOWN_DATA_TITLE));

                    // date - JSONString에서 클래스로 캐스팅
                    Type type = new TypeToken<MNDate>(){}.getType();
                    String dateJsonString = getPanelDataObject().getString(DATE_COUNTDOWN_DATA_DATE);
                    MNDate date = new Gson().fromJson(dateJsonString, type);
                    // Calendar의 month는 -1을 해줘야 맞는다
//                    datePicker.init(date.getYear(), date.getMonth() - 1, date.getDay(), null);
                    customDatePicker.init(date.getYear(), date.getMonth() - 1, date.getDay(), null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 기존 정보가 없다면 새해 표시
                titleEditText.setText(R.string.date_countdown_new_year);
                MNDate date = MNDefaultDateMaker.getDefaultDate();
                // Calendar의 month는 -1을 해줘야 맞는다
//                datePicker.init(date.getYear(), date.getMonth() - 1, date.getDay(), null);
                customDatePicker.init(date.getYear(), date.getMonth() - 1, date.getDay(), null);
            }
            titleEditText.requestFocus();
            customDatePicker.setCalendarViewShown(false); // custom은 달력 삭제를 코드로 설정해준다
        }
        return rootView;
    }

    @Override
    protected void archivePanelData() throws JSONException {
        // title
        String titleString = titleEditText.getText().toString();
        // 새해 체크해주기
        if (titleString.equals(getString(R.string.date_countdown_new_year))) {
            getPanelDataObject().put(DATE_COUNTDOWN_IS_NEW_YEAR, true);
        } else {
            getPanelDataObject().remove(DATE_COUNTDOWN_IS_NEW_YEAR);
        }
        getPanelDataObject().put(DATE_COUNTDOWN_DATA_TITLE, titleString);

        // date - 월은 + 1을 해야 제대로 된 값이 된다(1월 ~ 12월)
        MNDate date = new MNDate(customDatePicker.getYear(), customDatePicker.getMonth() + 1,
                customDatePicker.getDayOfMonth());
        String dateJsonString = new Gson().toJson(date);
        getPanelDataObject().put(DATE_COUNTDOWN_DATA_DATE, dateJsonString);
    }

    @OnClick(R.id.panel_detail_date_countdown_removeAllButton)
    public void onRemoveAllButtonClicked() {
        titleEditText.setText("");
    }
}
