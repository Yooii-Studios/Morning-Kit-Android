package com.yooiistudios.morningkit.panel.quotes.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailFragment;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNSettingColors;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 9.
 *
 * MNQuotesDetailFragment
 */
public class MNQuotesDetailFragment extends MNPanelDetailFragment {
    private static final String TAG = "MNQuotesDetailFragment";

    @InjectView(R.id.panel_quotes_detail_content_textview)
    TextView contentTextView;

    @InjectView(R.id.panel_quotes_detail_language_listview)
    MNQuotesLanguageListView languageListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.panel_quotes_detail_fragment, container, false);
        if (rootView != null) {
            ButterKnife.inject(this, rootView);

            //// Logic part ////

            //// UI part ////
            // listView의 높이를 맞추어줌

            languageListView.post(new Runnable() {
                @Override
                public void run() {
                    setListViewHeightBasedOnChildren(languageListView);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme();
    }

    private void applyTheme() {
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getActivity());
        if (getView() != null) {
            getView().setBackgroundColor(MNSettingColors.getBackwardBackgroundColor(currentThemeType));
            contentTextView.setBackgroundColor(MNSettingColors.getExchangeRatesForwardColor(currentThemeType));
        } else {
            MNLog.e(TAG, "getView() is null!");
        }
    }
    @Override
    protected void archivePanelData() throws JSONException {

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            MNLog.now("view: " + view.toString());
            MNLog.now("view.getMeasureHeight: " + view.getMeasuredHeight());

            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        MNLog.now("totalHeight: " + totalHeight);

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = DipToPixel.dpToPixel(listView.getContext(), 300);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
