package com.yooiistudios.morningkit.common;

import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
public class MNViewSizeMeasure {

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

                    // 사이즈는 필요하면 getWidth()와 getHeight()로 구할 수 있음
//                    Point viewSize = new Point();
//                    viewSize.x = view.getWidth();
//                    viewSize.y = view.getHeight();
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
    private static void removeGlobalOnLayoutListenerUnderAPI16(View view, ViewTreeObserver.OnGlobalLayoutListener listener){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }
}
