package com.yooiistudios.morningkit.panel.cat;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;
import com.yooiistudios.morningkit.common.tutorial.MNTutorialManager;
import com.yooiistudios.morningkit.panel.cat.model.MNCatUtils;
import com.yooiistudios.morningkit.panel.cat.model.MNHappyMessage;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNThemeType;
import com.yooiistudios.morningkit.theme.MNMainColors;

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

    private int previousAnimationResourceId = -1;
    private AutoResizeTextView happyMessageTextView;
    private MNHappyMessage happyMessage;

    private static final int HAPPY_MESSAGE_HANDLER_DELAY = 3000;
    private static final int CAT_ANIMATION_HANDLER_DELAY = 8000;
    private boolean isHandlerRunning = false;
    private MNCatAnimationHandler catAnimationHandler = new MNCatAnimationHandler();
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
            catAnimationHandler.sendEmptyMessageDelayed(0,
                    CAT_ANIMATION_HANDLER_DELAY + HAPPY_MESSAGE_HANDLER_DELAY);
            happyMessageHandler.sendEmptyMessageDelayed(0, CAT_ANIMATION_HANDLER_DELAY);
        }
    }

    private MNHappyMessageHandler happyMessageHandler = new MNHappyMessageHandler();
    private class MNHappyMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (MNTutorialManager.isTutorialShown(getContext().getApplicationContext())) {
                // 애니메이션이 끝난 후 해피 텍스트를 보여주기
                int previousIndex = -1;
                if (happyMessage != null) {
                    previousIndex = happyMessage.previousIndex;
                }
                happyMessage = MNCatUtils.getRandomHappyString(getContext().getApplicationContext(),
                        previousIndex);

                // 언어 길이에 따라 동적으로 크기 조절
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
                stringBuilder.append(happyMessage.happyMessageString);
//                happyMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//                        getResources().getDimensionPixelSize(R.dimen.panel_exchange_rates_main_font_size));
//                happyMessageTextView.setMinTextSize(DipToPixel.dpToPixel(getContext(), 1));

                // 방향에 따라 최초 사이즈를 약간 다르게 주기
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    happyMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimensionPixelSize(R.dimen.panel_cat_default_font_size_port));
                } else {
                    happyMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimensionPixelSize(R.dimen.panel_cat_default_font_size_land));
                }

                happyMessageTextView.setText(stringBuilder, TextView.BufferType.SPANNABLE);

                // 텍스트뷰를 다시 보이게 변경
                catImageView.setVisibility(View.GONE);
                happyMessageTextView.setVisibility(View.VISIBLE);
            }
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
        catImageView.setAdjustViewBounds(true);
        catImageView.setLayoutParams(imageViewLayoutParams);
        catImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageViewLayoutParams.addRule(CENTER_IN_PARENT);
        getContentLayout().addView(catImageView);

        // happyMessageTextView
        happyMessageTextView = new AutoResizeTextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = (int) getResources().getDimension(R.dimen.panel_quotes_padding);
        layoutParams.setMargins(margin, margin, margin, margin);
        happyMessageTextView.setLayoutParams(layoutParams);
        happyMessageTextView.setVisibility(View.GONE);
        happyMessageTextView.setGravity(Gravity.CENTER);
        getContentLayout().addView(happyMessageTextView);
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();
        for (int i = 0; i < 100; i++) {
            int newAnimationResourceId = MNCatUtils.getRandomCatAnimationResourceId(true);
            if (newAnimationResourceId != previousAnimationResourceId) {
                previousAnimationResourceId = newAnimationResourceId;
                break;
            }
        }
        // ScaleType 을 활용하기 위해서 해당 코드로 변경
        if (previousAnimationResourceId != -1) {
            catImageView.setImageDrawable(getResources().getDrawable(previousAnimationResourceId));
//        catImageView.setBackgroundResource(MNCatUtils.getRandomCatAnimationResourceId(true));
        }
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        happyMessageTextView.setVisibility(View.GONE);
        catImageView.setVisibility(View.VISIBLE);
        if (catImageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable catAnimation = (AnimationDrawable) catImageView.getDrawable();
            Log.i(TAG, catAnimation.toString());
            catAnimation.start();
            if (!catAnimation.isRunning()) {
                Log.i(TAG, "!catAnimation.isRunning()");
                catAnimation.start();
            } else {
                Log.i(TAG, "catAnimation.isRunning()");
            }
        } else {
            Log.i(TAG, "catImageView.getDrawable() isn't instanceof AnimationDrawable");
            Log.i(TAG, catImageView.getDrawable().getClass().getName());
        }
    }

    @Override
    public void refreshPanel() throws JSONException {
        // 기존 핸들러를 취소하고 다시 실행하기
        stopAnimationHandler();

        super.refreshPanel();

        startAnimationHandler();
    }

    @Override
    public void applyTheme() {
        super.applyTheme();

        MNThemeType currentThemeType = MNTheme.getCurrentThemeType(getContext().getApplicationContext());
        happyMessageTextView.setTextColor(
                MNMainColors.getQuoteContentTextColor(currentThemeType, getContext().getApplicationContext()));
    }

    private void startAnimationHandler() {
        if (isHandlerRunning) {
            return;
        }
        isHandlerRunning = true;
        catImageView.setVisibility(View.VISIBLE);
        happyMessageTextView.setVisibility(View.INVISIBLE);
        catAnimationHandler.sendEmptyMessageDelayed(0,
                CAT_ANIMATION_HANDLER_DELAY + HAPPY_MESSAGE_HANDLER_DELAY);
        happyMessageHandler.sendEmptyMessageDelayed(0, CAT_ANIMATION_HANDLER_DELAY);
    }

    private void stopAnimationHandler() {
        if (!isHandlerRunning) {
            return;
        }
        isHandlerRunning = false;
        catImageView.setVisibility(View.GONE);
        happyMessageTextView.setVisibility(View.GONE);
        catAnimationHandler.removeMessages(0);
        happyMessageHandler.removeMessages(0);
    }

    // 뷰가 붙을 때 아날로그 시계뷰 재가동
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimationHandler();
    }

    // 뷰가 사라질 때 아날로그 시계뷰 핸들러 중지
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimationHandler();
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
