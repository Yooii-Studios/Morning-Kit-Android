package com.yooiistudios.morningkit.panel.memo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

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

    private TextView memoTextView;
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
        memoTextView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = (int) getResources().getDimension(R.dimen.margin_outer);
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
            } else {
                memoString = null;
            }
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        if (memoString != null) {
            hideCoverLayout();
            memoTextView.setText(memoString);
        } else {
            // 추후 패널쪽으로 올려서 전체적으로 구현할 예정: 패널이름(중앙) + 설명(아래쪽)
            showCoverLayout(getResources().getString(R.string.memo_write_here));
            memoTextView.setText(null);
        }
    }
}
