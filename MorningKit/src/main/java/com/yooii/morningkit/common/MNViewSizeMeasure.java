package com.yooii.morningkit.common;

import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by StevenKim on 2013. 11. 4..
 */
public class MNViewSizeMeasure {

    public interface OnGlobalLayoutObserver {
        public void onLayoutLoad(Point size);
    }

    public static void setViewSizeObserver(final View view, final OnGlobalLayoutObserver listener) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    removeGlobalLayoutListener(view, this);
                    Point viewSize = new Point();
                    viewSize.x = view.getWidth();
                    viewSize.y = view.getHeight();
                    if (listener != null) {
                        listener.onLayoutLoad(viewSize);
                    }
                }
            });
        }
    }

    private static void removeGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener){

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
            } else {
                removeGlobalOnLayoutListenerOverAPI13(view, listener);
            }
        }
    }
    private static void removeGlobalOnLayoutListenerOverAPI13(View view, ViewTreeObserver.OnGlobalLayoutListener listener){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver != null) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
