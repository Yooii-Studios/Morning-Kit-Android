package com.yooiistudios.morningkit.panel.cat;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
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
//    private static final String TAG = "MNCatPanelLayout";

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
        // 수정: 끝에서 끝으로 가는 애니메이션 때문에 패딩을 삭제
        catImageView = new ImageView(getContext());
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        catImageView.setAdjustViewBounds(true);
        catImageView.setLayoutParams(imageViewLayoutParams);
        catImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageViewLayoutParams.addRule(CENTER_IN_PARENT);
        getContentLayout().addView(catImageView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
        catImageView.setImageDrawable(getResources().getDrawable(MNCatUtils.getRandomCatAnimationResourceId(true)));
//        catImageView.setBackgroundResource(MNCatUtils.getRandomCatAnimationResourceId(true));
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
    public void refreshPanel() throws JSONException {
        // 기존 핸들러를 취소하고 다시 실행하기
        stopHandler();

        super.refreshPanel();

        startHandler();
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
        catImageView.setVisibility(View.VISIBLE);
        catHandler.sendEmptyMessageDelayed(0, CAT_ANIMATION_HANDLER_DELAY);
    }

    private void stopHandler() {
        if (!isHandlerRunning) {
            return;
        }
        isHandlerRunning = false;
        catImageView.setVisibility(View.GONE);
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

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        MNViewSizeMeasure.setViewSizeObserver(catImageView, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                try {
                    refreshPanel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
