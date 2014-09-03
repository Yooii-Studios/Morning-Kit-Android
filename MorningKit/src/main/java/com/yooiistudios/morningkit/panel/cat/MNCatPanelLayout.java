package com.yooiistudios.morningkit.panel.cat;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.cat.model.MNCatUtils;
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

        // 고양이 패널만 특별하게 패딩 삭제
//        LinearLayout.LayoutParams panelLayoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
//                .MATCH_PARENT);
//        panelLayoutParams.setMargins(0, 0, 0, 0);
//        setLayoutParams(layoutParams);

        // image view
        catImageView = new ImageView(getContext());
//        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(
//                getResources().getDimensionPixelSize(R.dimen.panel_cat_image_view_width),
//                getResources().getDimensionPixelSize(R.dimen.panel_cat_image_view_height));
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        catImageView.setLayoutParams(imageViewLayoutParams);
        imageViewLayoutParams.addRule(CENTER_IN_PARENT);
        getContentLayout().addView(catImageView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
        catImageView.setBackgroundResource(MNCatUtils.getRandomCatAnimationResourceId(true));
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        if (catImageView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable catAnimation = (AnimationDrawable) catImageView.getBackground();
            if (!catAnimation.isRunning()) {
                catAnimation.start();
            }
        }
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
