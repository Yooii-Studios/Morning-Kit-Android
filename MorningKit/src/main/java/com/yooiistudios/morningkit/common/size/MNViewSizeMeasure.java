package com.yooiistudios.morningkit.common.size;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by StevenKim on 2013. 11. 4..
 *
 * MNViewSizeMeasure
 * 예전 동현이가 테스트시 UI width, height를 재기 위해서 만들어 주었으나
 * 현재는 사용되고 있지 않음
 */
public class MNViewSizeMeasure {

    private MNViewSizeMeasure() { throw new AssertionError(); } // You must not create instance
    public interface OnGlobalLayoutObserver {
        public void onLayoutLoad();
    }

    public static void setViewSizeObserver(final View view, final OnGlobalLayoutObserver listener) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    removeGlobalLayoutListener(view, this);
                    if (listener != null) {
                        listener.onLayoutLoad();
                    }
                }
            });
        }
    }

    private static void removeGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            } else {
                removeGlobalOnLayoutListenerUnderAPI16(view, listener);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private static void removeGlobalOnLayoutListenerUnderAPI16(View view, ViewTreeObserver.OnGlobalLayoutListener listener){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }
}
