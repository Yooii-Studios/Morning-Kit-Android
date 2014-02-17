package com.yooiistudios.morningkit.panel;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 16.
 *
 * PanelLayout
 *  패널의 근간이 되는 레이아웃
 */
public class MNPanelLayout extends RoundShadowRelativeLayout {

    @Getter @Setter MNPanelType panelType;
    @Getter @Setter RelativeLayout contentLayout;
    @Getter @Setter RelativeLayout statusLayout;
    @Getter @Setter ImageView loadingImageView;
    @Getter @Setter boolean isUsingNetwork;

    public MNPanelLayout(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public MNPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    protected void init() {
        setClipChildren(false);

        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.panel_height));
        layoutParams.weight = 1;
        setLayoutParams(layoutParams);

        // content layout 추가
        contentLayout = new RelativeLayout(getContext());
        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int padding = (int) getResources().getDimension(R.dimen.margin_inner);
        contentLayout.setPadding(padding, padding, padding, padding);
        contentLayout.setLayoutParams(contentLayoutParams);
        addView(contentLayout);
    }

    // 네트워크 사용 레이아웃을 위함
    protected void initNetworkPanel() {
        isUsingNetwork = true;

        // status layout(network, loading) 추가
        statusLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int padding = (int) getResources().getDimension(R.dimen.margin_inner);
        contentLayout.setPadding(padding, padding, padding, padding);
        contentLayout.setLayoutParams(layoutParams);
        statusLayout.setLayoutParams(layoutParams);
        addView(statusLayout);

        loadingImageView = new ImageView(getContext());
        ViewGroup.LayoutParams loadingImageViewLayoutParams
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingImageView.setLayoutParams(loadingImageViewLayoutParams);
        loadingImageView.setBackgroundResource(R.drawable.panel_loading_animation);
        statusLayout.addView(loadingImageView);
    }

    public void refreshPanel() {
        if (isUsingNetwork) {
            // 네트워크 체크
            boolean isReachable = true;
            if (isReachable) {
                processLoading();
            } else {
                // 네트워크 불가 이미지
                showNetworkIsUnavailable();
            }
        } else {
            processLoading();
            updateUI();
        }
    }

    protected void processLoading() {
        if (isUsingNetwork) {
            startLoadingAnimation();
        }
    }

    protected void updateUI() {
        if (isUsingNetwork) {
            stopLoadingAnimation();
        }
    }

    protected void startLoadingAnimation() {
        statusLayout.setVisibility(VISIBLE);
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.start();
    }

    protected void stopLoadingAnimation() {
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.stop();
        statusLayout.setVisibility(INVISIBLE);
    }

    protected void showNetworkIsUnavailable() {

    }

    public void applyTheme() {

    }
}
