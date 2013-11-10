package com.yooiistudios.morningkit.main.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.yooiistudios.morningkit.R;

/**
 * Created by StevenKim on 2013. 11. 10..
 */
public class MNMainLayout {
    public static void adjustScrollViewLayoutparamsOnOrientation(ScrollView scrollView, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                if (scrollViewLayoutParams != null) {
                    // ABOVE 설정 삭제
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        scrollViewLayoutParams.removeRule(RelativeLayout.ABOVE);
                    }else{
                        scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, 0);
                    }
                    scrollViewLayoutParams.bottomMargin = 0;
                }
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                if (scrollViewLayoutParams != null) {
                    scrollViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.main_button_layout);
                    // 아래쪽으로 margin_outer - margin_inner 만큼 주어야 윗 마진(margin_outer)과 같아짐
                    Context context = scrollView.getContext();
                    if (context != null) {
                        scrollViewLayoutParams.bottomMargin = (int)(context.getResources().getDimension(R.dimen.margin_outer) - context.getResources().getDimension(R.dimen.margin_inner));
                    }
                }
                break;
            }
        }
    }
}
