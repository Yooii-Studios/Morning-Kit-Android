package com.yooiistudios.morningkit.panel.cat.model;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;

import org.json.JSONException;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 7. 23.
 *
 * MNCatPanelLayout
 *  시간대별로 고양이 애니메이션을 보여주는 패널
 */
public class MNCatPanelLayout extends MNPanelLayout {
    private static final String TAG = "MNCatPanelLayout";

    private ImageView catImageView;
    private static final int CAT_ANIMATION_HANDLER_DELAY = 8000;
    private boolean isHandlerRunning = false;
    private MNCatAnimationHandler catHandler = new MNCatAnimationHandler();
    private class MNCatAnimationHandler extends Handler {
        @Override
        public void handleMessage( Message msg ){
            if (MNTutorialManager.isTutorialShown(getContext().getApplicationContext())) {
                // 갱신
                try {
                    refreshPanel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // tick 의 동작 시간을 계산해서 정확히 틱 초마다 UI 갱신을 요청할 수 있게 구현
            catHandler.sendEmptyMessageDelayed(0, CAT_ANIMATION_HANDLER_DELAY);
        }
    }

    public MNCatPanelLayout(Context context) {
        super(context);
    }

    public MNCatPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // image view
        catImageView = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.panel_cat_image_view_width),
                getResources().getDimensionPixelSize(R.dimen.panel_cat_image_view_height));
        catImageView.setLayoutParams(layoutParams);
        layoutParams.addRule(CENTER_IN_PARENT);
        getContentLayout().addView(catImageView);

        catImageView.setBackgroundResource(R.drawable.cat_animation_morning_set_1);
        if (catImageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable loadingAnimation = (AnimationDrawable) catImageView.getBackground();
            loadingAnimation.start();
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
    }

    @Override
    protected void updateUI() {
        super.updateUI();
    }

    @Override
    public void applyTheme() {
        super.applyTheme();
    }

    private void startHandler() {
        if (isHandlerRunning) {
            return;
        }
        isHandlerRunning = true;
        catHandler.sendEmptyMessageDelayed(0, CAT_ANIMATION_HANDLER_DELAY);
    }

    private void stopHandler() {
        if (!isHandlerRunning) {
            return;
        }
        isHandlerRunning = false;
        catHandler.removeMessages(0);
    }

    // 뷰가 붙을 때 아날로그 시계뷰 재가동
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startHandler();
    }

    // 뷰가 사라질 때 아날로그 시계뷰 핸들러 중지
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopHandler();
    }
}
