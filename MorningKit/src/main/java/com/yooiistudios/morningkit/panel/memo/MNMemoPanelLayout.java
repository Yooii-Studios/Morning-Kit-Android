package com.yooiistudios.morningkit.panel.memo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.dp.DipToPixel;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

import org.json.JSONException;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 26.
 *
 * MNMemoPanelLayout
 */
public class MNMemoPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNMemoPanelLayout";

    public static final String MEMO_PREFS = "MEMO_PREFS";
    public static final String MEMO_PREFS_CONTENT = "MEMO_PREFS_CONTENT";
    public static final String MEMO_DATA_CONTENT = "MEMO_DATA_CONTENT";

    private AutoResizeTextView memoTextView;
    private String memoString;

    public MNMemoPanelLayout(Context context) {
        super(context);
    }

    public MNMemoPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // TextView
        memoTextView = new AutoResizeTextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = (int) getResources().getDimension(R.dimen.panel_quotes_padding);
        layoutParams.setMargins(margin, margin, margin, margin);
        memoTextView.setLayoutParams(layoutParams);
        getContentLayout().addView(memoTextView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        if (getPanelDataObject().has(MEMO_DATA_CONTENT)) {
            memoString = getPanelDataObject().getString(MEMO_DATA_CONTENT);
        } else {
            SharedPreferences prefs = getContext().getSharedPreferences(MEMO_PREFS, Context.MODE_PRIVATE);
            String archivedString = prefs.getString(MEMO_PREFS_CONTENT, null);
            if (archivedString != null) {
                memoString = archivedString;
                getPanelDataObject().put(MEMO_DATA_CONTENT, memoString);
            } else {
                memoString = null;
                getPanelDataObject().put(MEMO_DATA_CONTENT, null);
            }
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        if (memoString != null && memoString.length() != 0) {
            hideCoverLayout();
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append(memoString);
            memoTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_main_font_size));
            memoTextView.setMinTextSize(DipToPixel.dpToPixel(getContext(), 1));
            memoTextView.setText(memoString);
        } else {
            // 추후 패널쪽으로 올려서 전체적으로 구현할 예정: 패널이름(중앙) + 설명(아래쪽)
            showCoverLayout(getResources().getString(R.string.memo_write_here));
            memoTextView.setText(null);
        }
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext().getApplicationContext());
        memoTextView.setTextColor(
                MNMainColors.getQuoteContentTextColor(currentThemeType, getContext().getApplicationContext()));
    }

    /**
     * Rotate
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 로딩이 끝나기 전에는 진행하지 않음
        if (memoString != null) {
            MNViewSizeMeasure.setViewSizeObserver(this, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
                @Override
                public void onLayoutLoad() {
                    updateUI();
                }
            });
        }
    }
}
