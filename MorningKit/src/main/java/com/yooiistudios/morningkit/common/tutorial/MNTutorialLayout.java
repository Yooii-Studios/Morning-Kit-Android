package com.yooiistudios.morningkit.common.tutorial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.size.MNDeviceSizeInfo;
import com.yooiistudios.morningkit.common.textview.AutoResizeTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Wooseong Kim in MorningKitTutorialProj from Yooii Studios Co., LTD. on 2014. 5. 28.
 * <p/>
 * MNTutorialView
 * 첫 번째 튜토리얼(앱 설명)을 위한 뷰
 */
public class MNTutorialLayout extends RelativeLayout {
    private static final String TAG = "MNTutorialLayout";

    @InjectView(R.id.tutorial_skip_tutorial_text_view) TextView skipTutorialTextView;
    @InjectView(R.id.tutorial_tap_screen_text_view) TextView tapScreenTextView;

    private Animation tapScreenAnimation2;
    private Animation tapScreenAnimation1;

    private Context applicationContext;
    private Resources resources;

    private View        firstCircleView1;      // LT = left top
    private View        firstCircleAnimView1;
    private View        firstCircleView2;      // RT = right top
    private View        firstCircleAnimView2;
    private View        firstCircleView3;      // LB = left bottom
    private View        firstCircleAnimView3;
    private View        firstCircleView4;      // RB = right bottom
    private View        firstCircleAnimView4;
    private AutoResizeTextView firstTextView;

    private ImageView   secondAlarmImageView;
    private AutoResizeTextView    secondTextView;
    private View        secondCircleView;
    private View        secondCircleAnimView;

    private View        thirdCircleView;
    private View        thirdCircleAnimView;
    private AutoResizeTextView    thirdTextView;

    private RelativeLayout forthButtonLayout;
    private ImageView   forthImageView1; // refresh
    private TextView    forthTextView1;
    private ImageView   forthImageView2; // setting
    private TextView    forthTextView2;

    private int tutorialTapCount = 0;

    private OnTutorialFinishListener listener;
    public interface OnTutorialFinishListener {
        public void onFinishTutorial();
    }

    public MNTutorialLayout(Context context, OnTutorialFinishListener listener) {
        super(context);
        applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            resources = applicationContext.getResources();
        }
        this.listener = listener;
        if (!isInEditMode()) {
            initTutorialLayout();
        }
    }

    private void initTutorialLayout() {
        LayoutParams layoutParams = new LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT);
        setLayoutParams(layoutParams);

        if (applicationContext != null) {
            LayoutInflater inflater = (LayoutInflater) applicationContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout tutorialLayout = (RelativeLayout) inflater.inflate(R.layout.tutorial_layout, null);

            if (tutorialLayout != null) {
                tutorialLayout.setLayoutParams(layoutParams);
                addView(tutorialLayout);

                // Butter Knife
                ButterKnife.inject(this, tutorialLayout);

                // Click Listener
                skipTutorialTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSkipTutorialClicked();
                    }
                });

                initTapScreenTextView();
            }
            showFirstTutorial();
        }
    }

    private void initTapScreenTextView() {
        // 태블릿 지원
        if (MNDeviceSizeInfo.isTablet(applicationContext)) {
            LayoutParams layoutParams = (LayoutParams) tapScreenTextView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.bottomMargin = 0;
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.main_admob_layout_height) +
                        resources.getDimensionPixelSize(R.dimen.margin_inner) * 2;
            }
        }

        // 클릭 리스너
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapScreenClicked();
            }
        });
        hideTapScreenTextView();
    }

    private void showFirstTutorial() {
        // 튜토리얼 1:
        // 동그라미 4개가 각 패널 중심에서 차례대로 나타난 이후
        // 가운데에 텍스트뷰가 하나 뜬다.
        int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(applicationContext);
        int circleSize = resources.getDimensionPixelSize(R.dimen.tutorial_circle_size);
        int innerMargin = resources.getDimensionPixelSize(R.dimen.margin_inner);
        int panelHeight = resources.getDimensionPixelSize(R.dimen.panel_height);
        int circleLeftRightMargin = (int) ((deviceWidth - innerMargin * 2) / 4.0f - circleSize / 2.0f);
        int circleTopMargin = (int) (innerMargin + (panelHeight / 2.0f) - (circleSize / 2.0f));
        int textViewMargin = resources.getDimensionPixelSize(R.dimen.panel_cover_padding);

        // Text
        firstTextView = new AutoResizeTextView(applicationContext);
        LayoutParams textViewLayoutParams =
                new LayoutParams(MATCH_PARENT, panelHeight * 2 - innerMargin * 2);
        textViewLayoutParams.topMargin = innerMargin * 2;
        textViewLayoutParams.leftMargin = textViewMargin;
        textViewLayoutParams.rightMargin = textViewMargin;
        firstTextView.setLayoutParams(textViewLayoutParams);
        initDefaultTextView(firstTextView);
        firstTextView.setText(R.string.tutorial1_label1);
        addView(firstTextView);
        firstTextView.setVisibility(INVISIBLE);

        // Anim 관련
        final int animationFirstOffset = 550;
        int animationOffset = 200;

        // Circle View1
        firstCircleView1 = new View(applicationContext);
        LayoutParams circleView1Params =
                new LayoutParams(circleSize, circleSize);
        circleView1Params.addRule(ALIGN_PARENT_TOP);
        circleView1Params.addRule(ALIGN_PARENT_LEFT);
        circleView1Params.topMargin = circleTopMargin;
        circleView1Params.leftMargin = circleLeftRightMargin;
        firstCircleView1.setLayoutParams(circleView1Params);
        firstCircleView1.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(firstCircleView1);

        // Circle Anim View1
        firstCircleAnimView1 = new View(applicationContext);
        LayoutParams circleAnimView1Params =
                new LayoutParams(circleSize, circleSize);
        circleAnimView1Params.addRule(ALIGN_PARENT_TOP);
        circleAnimView1Params.addRule(ALIGN_PARENT_LEFT);
        circleAnimView1Params.topMargin = circleTopMargin;
        circleAnimView1Params.leftMargin = circleLeftRightMargin;
        firstCircleAnimView1.setLayoutParams(circleAnimView1Params);
        firstCircleAnimView1.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        firstCircleAnimView1.setVisibility(View.INVISIBLE);
        addView(firstCircleAnimView1);

        Animation circleAlphaAnimation1 = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial_circle_alpha_anim);
        if (circleAlphaAnimation1 != null) {
            circleAlphaAnimation1.setStartOffset(animationFirstOffset);
            circleAlphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    firstCircleView1.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                    Animation circleScalingAnimation1 = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_circle_scaling_anim);
                    if (circleScalingAnimation1 != null) {
                        firstCircleAnimView1.startAnimation(circleScalingAnimation1);
                    }
                }
            });
            firstCircleAnimView1.startAnimation(circleAlphaAnimation1);
        }

        // Circle View2
        firstCircleView2 = new View(applicationContext);
        LayoutParams circleView2Params =
                new LayoutParams(circleSize, circleSize);
        circleView2Params.addRule(ALIGN_PARENT_TOP);
        circleView2Params.addRule(ALIGN_PARENT_RIGHT);
        circleView2Params.topMargin = circleTopMargin;
        circleView2Params.rightMargin = circleLeftRightMargin;
        firstCircleView2.setLayoutParams(circleView2Params);
        firstCircleView2.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(firstCircleView2);

        // Circle Anim View2
        firstCircleAnimView2 = new View(applicationContext);
        firstCircleAnimView2.setLayoutParams(circleView2Params);
        firstCircleAnimView2.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        firstCircleAnimView2.setVisibility(INVISIBLE);
        addView(firstCircleAnimView2);

        Animation circleAlphaAnimation2 = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial_circle_alpha_anim);
        if (circleAlphaAnimation2 != null) {
            circleAlphaAnimation2.setStartOffset(animationFirstOffset +
                    animationOffset);
            circleAlphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    firstCircleView2.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                    Animation circleScalingAnimation2 = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_circle_scaling_anim);
                    if (circleScalingAnimation2 != null) {
                        firstCircleAnimView2.startAnimation(circleScalingAnimation2);
                    }
                }
            });
            firstCircleAnimView2.startAnimation(circleAlphaAnimation2);
        }

        // Circle View3
        firstCircleView3 = new View(applicationContext);
        LayoutParams circleView3Params =
                new LayoutParams(circleSize, circleSize);
        circleView3Params.addRule(ALIGN_PARENT_TOP);
        circleView3Params.addRule(ALIGN_PARENT_LEFT);
        circleView3Params.topMargin = circleTopMargin + panelHeight;
        circleView3Params.leftMargin = circleLeftRightMargin;
        firstCircleView3.setLayoutParams(circleView3Params);
        firstCircleView3.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(firstCircleView3);

        // Circle Anim View3
        firstCircleAnimView3 = new View(applicationContext);
        firstCircleAnimView3.setLayoutParams(circleView3Params);
        firstCircleAnimView3.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        firstCircleAnimView3.setVisibility(INVISIBLE);
        addView(firstCircleAnimView3);

        Animation circleAlphaAnimation3 = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial_circle_alpha_anim);
        if (circleAlphaAnimation3 != null) {
            circleAlphaAnimation3.setStartOffset(animationFirstOffset +
                    animationOffset * 2);
            circleAlphaAnimation3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    firstCircleView3.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                    Animation circleScalingAnimation3 = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_circle_scaling_anim);
                    if (circleScalingAnimation3 != null) {
                        firstCircleAnimView3.startAnimation(circleScalingAnimation3);
                    }
                }
            });
            firstCircleAnimView3.startAnimation(circleAlphaAnimation3);
        }

        // Circle View4
        firstCircleView4 = new View(applicationContext);
        LayoutParams circleView4Params =
                new LayoutParams(circleSize, circleSize);
        circleView4Params.addRule(ALIGN_PARENT_TOP);
        circleView4Params.addRule(ALIGN_PARENT_RIGHT);
        circleView4Params.topMargin = circleTopMargin + panelHeight;
        circleView4Params.rightMargin = circleLeftRightMargin;
        firstCircleView4.setLayoutParams(circleView4Params);
        firstCircleView4.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(firstCircleView4);

        // Circle Anim View3
        firstCircleAnimView4 = new View(applicationContext);
        firstCircleAnimView4.setLayoutParams(circleView4Params);
        firstCircleAnimView4.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        firstCircleAnimView4.setVisibility(INVISIBLE);
        addView(firstCircleAnimView4);

        Animation circleAlphaAnimation4 = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial_circle_alpha_anim);
        if (circleAlphaAnimation4 != null) {
            circleAlphaAnimation4.setStartOffset(animationFirstOffset +
                    animationOffset * 3);
            circleAlphaAnimation4.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    firstCircleView4.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                    Animation circleScalingAnimation4 = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_circle_scaling_anim);
                    if (circleScalingAnimation4 != null) {
                        circleScalingAnimation4.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation textAnimation =
                                        AnimationUtils.loadAnimation(applicationContext,
                                                R.anim.tutorial1_text_alpha_anim);
                                if (textAnimation != null) {
                                    textAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {}
                                        @Override
                                        public void onAnimationRepeat(Animation animation) {}
                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            showTapScreenTextView();
                                        }
                                    });
                                    firstTextView.startAnimation(textAnimation);
                                }
                            }
                        });
                        firstCircleAnimView4.startAnimation(circleScalingAnimation4);
                    }
                }
            });
            firstCircleAnimView4.startAnimation(circleAlphaAnimation4);
        }
    }

    private void recycleFirstTutorial() {
        removeView(firstCircleView1);
        firstCircleAnimView1.clearAnimation();
        removeView(firstCircleAnimView1);

        removeView(firstCircleView2);
        firstCircleAnimView2.clearAnimation();
        removeView(firstCircleAnimView2);

        removeView(firstCircleView3);
        firstCircleAnimView3.clearAnimation();
        removeView(firstCircleAnimView3);

        removeView(firstCircleView4);
        firstCircleAnimView4.clearAnimation();
        removeView(firstCircleAnimView4);

        firstTextView.clearAnimation();
        removeView(firstTextView);
    }

    private void showSecondTutorial() {
        recycleFirstTutorial();
        hideTapScreenTextView();

        // 튜토리얼 2:
        // 알람 아이콘, 텍스트뷰, 텍스트뷰가 하나씩 내려온다
        int innerMargin = resources.getDimensionPixelSize(R.dimen.margin_inner);
        int panelHeight = resources.getDimensionPixelSize(R.dimen.panel_height);
        int alarmHeight = resources.getDimensionPixelSize(R.dimen.alarm_item_outer_height);
        int biggerMargin = resources.getDimensionPixelSize(R.dimen.panel_detail_bigger_padding);
        int textViewHeight = alarmHeight / 2;
        int textViewMargin = resources.getDimensionPixelSize(R.dimen.panel_cover_padding);
        int circleSize = resources.getDimensionPixelSize(R.dimen.tutorial_circle_size);
        int circleTopMargin = (int) (innerMargin + panelHeight * 2.0f +
                alarmHeight * 1.5f - circleSize / 2.0f);
        int alarmIconSize = resources.getDimensionPixelSize(R.dimen.tutorial_alarm_icon_size);

        // Anim 관련
        final int animationFirstOffset = 350;
        final int animationOffset = 650;

        // Alarm Icon
        secondAlarmImageView = new ImageView(applicationContext);
        secondAlarmImageView.setId(5934713);
        LayoutParams alarmIconImageViewParams =
                new LayoutParams(alarmIconSize, alarmIconSize);
        alarmIconImageViewParams.addRule(CENTER_HORIZONTAL);
        alarmIconImageViewParams.addRule(ALIGN_PARENT_TOP);
        alarmIconImageViewParams.topMargin = circleTopMargin - biggerMargin
                - textViewHeight - biggerMargin - alarmIconSize;
        secondAlarmImageView.setLayoutParams(alarmIconImageViewParams);

        Bitmap alarmIconBitmap = BitmapFactory.decodeResource(
                resources, R.drawable.tutorial_alarm_icon, MNBitmapUtils.getDefaultOptions());
        secondAlarmImageView.setImageDrawable(new BitmapDrawable(resources, alarmIconBitmap));
        addView(secondAlarmImageView);
        secondAlarmImageView.setVisibility(INVISIBLE);

        Animation alarmIconAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial2_trasit_alpha_anim);
        if (alarmIconAnimation != null) {
            if (Build.VERSION.SDK_INT > 10) {
                alarmIconAnimation.setInterpolator(applicationContext,
                        android.R.interpolator.accelerate_decelerate);
            }
            alarmIconAnimation.setStartOffset(animationFirstOffset);
            secondAlarmImageView.startAnimation(alarmIconAnimation);
        }

        // Text
        secondTextView = new AutoResizeTextView(applicationContext);
        secondTextView.setId(1874575);
        LayoutParams textViewLayoutParams =
                new LayoutParams(WRAP_CONTENT, textViewHeight);
        textViewLayoutParams.addRule(CENTER_HORIZONTAL);
        textViewLayoutParams.addRule(BELOW, secondAlarmImageView.getId());
        textViewLayoutParams.leftMargin = textViewMargin;
        textViewLayoutParams.rightMargin = textViewMargin;
        textViewLayoutParams.topMargin = biggerMargin;
        secondTextView.setLayoutParams(textViewLayoutParams);
        initDefaultTextView(secondTextView);
        secondTextView.setText(R.string.tutorial1_label2);
        addView(secondTextView);
        secondTextView.setVisibility(INVISIBLE);

        Animation textAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial2_trasit_alpha_anim);
        if (textAnimation != null) {
            textAnimation.setStartOffset(animationFirstOffset + animationOffset);
            secondTextView.startAnimation(textAnimation);
        }

        // Circle View
        secondCircleView = new View(applicationContext);
        LayoutParams circleViewParams =
                new LayoutParams(circleSize, circleSize);
        circleViewParams.addRule(CENTER_HORIZONTAL);
        circleViewParams.addRule(BELOW, secondTextView.getId());
        circleViewParams.topMargin = biggerMargin;
        secondCircleView.setLayoutParams(circleViewParams);
        secondCircleView.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(secondCircleView);
        secondCircleView.setVisibility(INVISIBLE);

        // Circle AnimView
        secondCircleAnimView = new View(applicationContext);
        secondCircleAnimView.setLayoutParams(circleViewParams);
        secondCircleAnimView.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        addView(secondCircleAnimView);
        secondCircleAnimView.setVisibility(INVISIBLE);

        Animation circleAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial2_trasit_alpha_anim);
        if (circleAnimation != null) {
            circleAnimation.setStartOffset(animationFirstOffset + animationOffset * 2);
            circleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation circleAlphaAnimation = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_circle_alpha_anim);
                    if (circleAlphaAnimation != null) {
                        circleAlphaAnimation.setStartOffset(100);
                        circleAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {}
                            @Override
                            public void onAnimationRepeat(Animation animation) {}
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                secondCircleView.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                                Animation circleScalingAnimation =
                                        AnimationUtils.loadAnimation(applicationContext,
                                        R.anim.tutorial_circle_scaling_anim);
                                if (circleScalingAnimation != null) {
                                    circleScalingAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {}
                                        @Override
                                        public void onAnimationRepeat(Animation animation) {}
                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            showTapScreenTextView();
                                        }
                                    });
                                    secondCircleAnimView.startAnimation(circleScalingAnimation);
                                }
                            }
                        });
                        secondCircleAnimView.startAnimation(circleAlphaAnimation);
                    }
                }
            });
            secondCircleView.startAnimation(circleAnimation);
        }
    }

    private void recycleSecondTutorial() {
        secondTextView.clearAnimation();
        removeView(secondTextView);

        secondAlarmImageView.clearAnimation();
        MNBitmapUtils.recycleImageView(secondAlarmImageView);
        removeView(secondAlarmImageView);

        secondCircleView.clearAnimation();
        removeView(secondCircleView);

        secondCircleAnimView.clearAnimation();
        removeView(secondCircleAnimView);
    }

    private void showThirdTutorial() {
        recycleSecondTutorial();
        hideTapScreenTextView();

        final int deviceWidth = MNDeviceSizeInfo.getDeviceWidth(applicationContext);
        int innerMargin = resources.getDimensionPixelSize(R.dimen.margin_inner);
        int panelHeight = resources.getDimensionPixelSize(R.dimen.panel_height);
        int alarmHeight = resources.getDimensionPixelSize(R.dimen.alarm_item_outer_height);
        int textTopMargin = resources.getDimensionPixelSize(R.dimen.panel_layout_padding);
        int textViewHeight = alarmHeight / 2;
        int textViewMargin = resources.getDimensionPixelSize(R.dimen.panel_cover_padding);
        int circleSize = resources.getDimensionPixelSize(R.dimen.tutorial_circle_size);
        int circleTopMargin = (int) (innerMargin + panelHeight * 2.0f +
                alarmHeight * 1.5f - circleSize / 2.0f);
        int circleLeftRightMargin = (int) ((deviceWidth - innerMargin * 2) / 4.0f - circleSize / 2.0f);

        // 튜토리얼 3:
        // 드래그해서 알람을 지울 수 있는 애니메이션을 보여준다.
        // 좌우로 움직이면서 텍스트뷰는 중앙에서 살짝 오른쪽으로 움직이면서 페이드 인 된다.

        // Animation 관련
        final int animationOffset = 100;
        final int animationSwipeDuration = 600;

        // Circle View
        thirdCircleView = new View(applicationContext);
        thirdCircleView.setId(4523852);
        LayoutParams circleViewParams =
                new LayoutParams(circleSize, circleSize);
        circleViewParams.addRule(CENTER_HORIZONTAL);
        circleViewParams.addRule(ALIGN_PARENT_RIGHT);
        circleViewParams.topMargin = circleTopMargin;
        circleViewParams.rightMargin = circleLeftRightMargin;
        thirdCircleView.setLayoutParams(circleViewParams);
        thirdCircleView.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);
        addView(thirdCircleView);

        Animation circleAlphaAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial3_circle_alpha_anim);
        if (circleAlphaAnimation != null) {
            circleAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    thirdCircleView.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                    // create translation animation set - to left
                    AnimationSet transitToLeftAnimationSet = new AnimationSet(false);
                    transitToLeftAnimationSet.setFillAfter(true);
                    transitToLeftAnimationSet.setFillEnabled(true);

                    // Animation
                    TranslateAnimation circleTransitAnimation =
                            new TranslateAnimation(
                                    0, 0,
                                    TranslateAnimation.ABSOLUTE, -1 * deviceWidth / 2,
                                    0, 0,
                                    0, 0);
                    circleTransitAnimation.setStartOffset(animationOffset);
                    circleTransitAnimation.setDuration(animationSwipeDuration);
                    if (Build.VERSION.SDK_INT > 10) {
                        circleTransitAnimation.setInterpolator(applicationContext,
                                android.R.interpolator.accelerate_decelerate);
                    }
                    transitToLeftAnimationSet.addAnimation(circleTransitAnimation);
                    transitToLeftAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thirdCircleView.setBackgroundResource(R.drawable.tutorial_circle_gray_shape);

                            // create translation animation set - to left
                            AnimationSet transitLeftAnimationSet = new AnimationSet(false);
                            transitLeftAnimationSet.setFillAfter(true);
                            transitLeftAnimationSet.setFillEnabled(true);

                            // Animation
                            TranslateAnimation circleTransitAnimation =
                                    new TranslateAnimation(
                                            TranslateAnimation.ABSOLUTE, -1 * deviceWidth / 2,
                                            TranslateAnimation.ABSOLUTE, -1 * deviceWidth / 2,
                                            0, 0,
                                            0, 0);
                            circleTransitAnimation.setStartOffset(0);
                            circleTransitAnimation.setDuration(400);
                            if (Build.VERSION.SDK_INT > 10) {
                                circleTransitAnimation.setInterpolator(applicationContext,
                                        android.R.interpolator.accelerate_decelerate);
                            }
                            transitLeftAnimationSet.addAnimation(circleTransitAnimation);
                            transitLeftAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {}
                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    thirdCircleView.setBackgroundResource(R.drawable.tutorial_circle_white_shape);

                                    // create translation animation set - to left
                                    AnimationSet transitToRightAnimationSet = new AnimationSet(false);
                                    transitToRightAnimationSet.setFillAfter(true);
                                    transitToRightAnimationSet.setFillEnabled(true);

                                    // Animation
                                    TranslateAnimation circleTransitAnimation =
                                            new TranslateAnimation(
                                                    TranslateAnimation.ABSOLUTE, -1 * deviceWidth / 2,
                                                    TranslateAnimation.ABSOLUTE, 0,
                                                    0, 0,
                                                    0, 0);
                                    circleTransitAnimation.setStartOffset(animationOffset);
                                    circleTransitAnimation.setDuration(animationSwipeDuration);
                                    if (Build.VERSION.SDK_INT > 10) {
                                        circleTransitAnimation.setInterpolator(applicationContext,
                                                android.R.interpolator.accelerate_decelerate);
                                    }
                                    transitToRightAnimationSet.addAnimation(circleTransitAnimation);
                                    transitToRightAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {}
                                        @Override
                                        public void onAnimationRepeat(Animation animation) {}
                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            thirdCircleAnimView.setVisibility(VISIBLE);
                                            Animation circleScaleAnimation =
                                                    AnimationUtils.loadAnimation(applicationContext,
                                                            R.anim.tutorial_circle_scaling_anim);
                                            if (circleScaleAnimation != null) {
                                                circleScaleAnimation.setStartOffset(animationOffset);
                                                circleScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {}
                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {}
                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                        showTapScreenTextView();
                                                    }
                                                });
                                                thirdCircleAnimView.startAnimation(circleScaleAnimation);
                                            }
                                        }
                                    });
                                    thirdCircleView.startAnimation(transitToRightAnimationSet);
                                }
                            });
                            thirdCircleView.startAnimation(transitLeftAnimationSet);
                        }
                    });
                    thirdCircleView.startAnimation(transitToLeftAnimationSet);
                }
            });
            thirdCircleView.startAnimation(circleAlphaAnimation);
        }

        // Circle Anim View
        thirdCircleAnimView = new View(applicationContext);
        thirdCircleAnimView.setLayoutParams(circleViewParams);
        addView(thirdCircleAnimView);
        thirdCircleAnimView.setBackgroundResource(R.drawable.tutorial_circle_white_shape);
        thirdCircleAnimView.setVisibility(INVISIBLE);

        // Text
        thirdTextView = new AutoResizeTextView(applicationContext);
        LayoutParams textViewLayoutParams =
                new LayoutParams(WRAP_CONTENT, textViewHeight);
        textViewLayoutParams.addRule(CENTER_HORIZONTAL);
        textViewLayoutParams.addRule(BELOW, thirdCircleView.getId());
        textViewLayoutParams.topMargin = textTopMargin;
        textViewLayoutParams.leftMargin = textViewMargin;
        textViewLayoutParams.rightMargin = textViewMargin;
        thirdTextView.setLayoutParams(textViewLayoutParams);
        initDefaultTextView(thirdTextView);
        thirdTextView.setText(R.string.tutorial1_label3);
        addView(thirdTextView);
        thirdTextView.setVisibility(INVISIBLE);

        Animation textAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial3_text_trasit_alpha_anim);
        if (textAnimation != null) {
            thirdTextView.startAnimation(textAnimation);
        }
    }

    private void recycleThirdTutorial() {
        thirdTextView.clearAnimation();
        removeView(thirdTextView);

        thirdCircleView.clearAnimation();
        removeView(thirdCircleView);

        thirdCircleAnimView.clearAnimation();
        removeView(thirdCircleAnimView);
    }

    private void showForthTutorial() {
        recycleThirdTutorial();
        hideTapScreenTextView();

        int admobLayoutHeight;
        int buttonLayoutHeight;
        int margin = resources.getDimensionPixelSize(R.dimen.panel_layout_padding);
        int iconSize = resources.getDimensionPixelSize(R.dimen.main_button_refresh_setting_size);
        int buttonLayoutMargin = resources.getDimensionPixelSize(R.dimen.margin_main_button_layout);
        int buttonLayoutPadding = resources.getDimensionPixelSize(R.dimen.padding_main_button_layout);

        // 태블릿 지원
        if (MNDeviceSizeInfo.isTablet(applicationContext)) {
            admobLayoutHeight = 0;
            buttonLayoutHeight = (resources.getDimensionPixelSize(R.dimen.main_admob_layout_height)
                    + resources.getDimensionPixelSize(R.dimen.margin_inner) * 2);
        } else {
            admobLayoutHeight = resources.getDimensionPixelSize(R.dimen.main_admob_layout_height);
            buttonLayoutHeight = resources.getDimensionPixelSize(R.dimen.main_button_layout_height);
        }

        // 튜토리얼 4:
        // 리프레시, 세팅이 양쪽에서 안쪽으로 들어옴.
        // 이후에 리프레시, 세팅 텍스트뷰도 아래쪽에서 안쪽으로 들어옴
        // Refresh Text
        forthTextView1 = new TextView(applicationContext);
        LayoutParams textView1LayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView1LayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        textView1LayoutParams.addRule(ALIGN_PARENT_LEFT);
        textView1LayoutParams.bottomMargin = admobLayoutHeight + buttonLayoutHeight + margin;
        textView1LayoutParams.leftMargin = (int) (margin * 1.5);
        forthTextView1.setLayoutParams(textView1LayoutParams);
        initDefaultTextView(forthTextView1);
        forthTextView1.setGravity(Gravity.LEFT);
        forthTextView1.setText(R.string.tutorial1_label4);
        addView(forthTextView1);

        // Setting Text
        forthTextView2 = new TextView(applicationContext);
        LayoutParams textView2LayoutParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView2LayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        textView2LayoutParams.addRule(ALIGN_PARENT_RIGHT);
        textView2LayoutParams.bottomMargin = admobLayoutHeight + buttonLayoutHeight + margin;
        textView2LayoutParams.rightMargin = (int) (margin * 1.5);
        forthTextView2.setLayoutParams(textView2LayoutParams);
        initDefaultTextView(forthTextView2);
        forthTextView2.setGravity(Gravity.RIGHT);
        forthTextView2.setText(R.string.tutorial1_label5);
        addView(forthTextView2);

        // Button Layout
        forthButtonLayout = new RelativeLayout(applicationContext);
        LayoutParams buttonLayoutParams =
                new LayoutParams(MATCH_PARENT, buttonLayoutHeight);
        buttonLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        buttonLayoutParams.bottomMargin = admobLayoutHeight;
        forthButtonLayout.setLayoutParams(buttonLayoutParams);
        addView(forthButtonLayout);

        // Refresh Icon
        forthImageView1 = new ImageView(applicationContext);
        LayoutParams forthImageView1Params =
                new LayoutParams(iconSize, iconSize);
        forthImageView1Params.addRule(ALIGN_PARENT_LEFT);
        forthImageView1Params.addRule(CENTER_VERTICAL);
        forthImageView1Params.leftMargin = buttonLayoutMargin + buttonLayoutPadding;
        forthImageView1.setLayoutParams(forthImageView1Params);

        Bitmap refreshBitmap = BitmapFactory.decodeResource(
                resources, R.drawable.button_refresh_pastel_green_normal, MNBitmapUtils.getDefaultOptions());
        forthImageView1.setImageDrawable(new BitmapDrawable(resources, refreshBitmap));
        forthImageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
        forthButtonLayout.addView(forthImageView1);

        // Setting Icon
        forthImageView2 = new ImageView(applicationContext);
        LayoutParams forthImageView2Params =
                new LayoutParams(iconSize, iconSize);
        forthImageView2Params.addRule(ALIGN_PARENT_RIGHT);
        forthImageView2Params.addRule(CENTER_VERTICAL);
        forthImageView2Params.rightMargin = buttonLayoutMargin + buttonLayoutPadding;
        forthImageView2.setLayoutParams(forthImageView2Params);

        Bitmap settingBitmap = BitmapFactory.decodeResource(
                resources, R.drawable.button_setting_pastel_green_normal, MNBitmapUtils.getDefaultOptions());
        forthImageView2.setImageDrawable(new BitmapDrawable(resources, settingBitmap));
        forthImageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
        forthButtonLayout.addView(forthImageView2);

        // Animation
        Animation refreshTextAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial4_refresh_text_anim);
        if (refreshTextAnimation != null) {
            forthTextView1.startAnimation(refreshTextAnimation);
        }

        Animation refreshIconAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial4_refresh_icon_anim);
        if (refreshIconAnimation != null) {
            forthImageView1.startAnimation(refreshIconAnimation);
        }

        Animation settingTextAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial4_setting_text_anim);
        if (settingTextAnimation != null) {
            forthTextView2.startAnimation(settingTextAnimation);
        }

        Animation settingIconAnimation = AnimationUtils.loadAnimation(applicationContext,
                R.anim.tutorial4_setting_icon_anim);
        if (settingIconAnimation != null) {
            settingIconAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    showTapScreenTextView();
                }
            });
            forthImageView2.startAnimation(settingIconAnimation);
        }
    }

    private void recycleForthTutorial() {
        removeView(forthTextView1);
        removeView(forthTextView2);
        MNBitmapUtils.recycleImageView(forthImageView1);
        removeView(forthTextView1);
        MNBitmapUtils.recycleImageView(forthImageView2);
        removeView(forthTextView2);
        removeView(forthButtonLayout);
    }

    private void showTapScreenTextView() {
        if (tapScreenAnimation1 == null) {
            tapScreenAnimation1 = AnimationUtils.loadAnimation(applicationContext,
                            R.anim.tutorial_tap_screen_alpha_anim);
        }
        if (tapScreenAnimation1 != null) {
            tapScreenAnimation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (tapScreenAnimation2 == null) {
                        tapScreenAnimation2 = AnimationUtils.loadAnimation(applicationContext,
                                        R.anim.tutorial_tap_screen_anim);
                    }
                    if (tapScreenAnimation2 != null) {
                        tapScreenTextView.startAnimation(tapScreenAnimation2);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tapScreenTextView.startAnimation(tapScreenAnimation1);
        }
    }

    private void hideTapScreenTextView() {
        tapScreenTextView.clearAnimation();
        tapScreenTextView.setVisibility(INVISIBLE);
    }

    private void initDefaultTextView(TextView textView) {
        Resources resources = getResources();
        if (resources != null && textView != null) {
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimensionPixelSize(R.dimen.tutorial_text_size));
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(firstTextView.getTypeface(), Typeface.BOLD);

            // test
//            textView.setBackgroundColor(Color.parseColor("#40ff00ff"));
        }
    }

    void onSkipTutorialClicked() {
        recycleForthTutorial();
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(MNTutorialLayout.this);
            if (listener != null) {
                listener.onFinishTutorial();
            }
        }
    }

    void onTapScreenClicked() {
//        Log.i(TAG, "onTapScreenClicked: " + tutorialTapCount);

        if (tutorialTapCount == 0) {
            showSecondTutorial();
        } else if (tutorialTapCount == 1) {
            showThirdTutorial();
        } else if (tutorialTapCount == 2) {
            showForthTutorial();
        } else {
            onSkipTutorialClicked();
        }
        tutorialTapCount++;
    }
}

