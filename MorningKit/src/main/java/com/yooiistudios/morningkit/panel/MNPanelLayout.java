package com.yooiistudios.morningkit.panel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailActivity;

import org.json.JSONObject;

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
    @Getter @Setter int panelIndex;
    @Getter @Setter RelativeLayout contentLayout;
    @Getter @Setter RelativeLayout statusLayout;
    @Getter @Setter ImageView loadingImageView;
    @Getter @Setter boolean isUsingNetwork;
    @Getter @Setter JSONObject panelDataObject;

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
        contentLayout.setLayoutParams(contentLayoutParams);
        addView(contentLayout);

        // onClickListener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPanelClick();
            }
        });

        panelDataObject = new JSONObject();
    }

    // 네트워크 사용 레이아웃을 위함
    protected void initNetworkPanel() {
        isUsingNetwork = true;

        // status layout(network, loading) 추가
        statusLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
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
        contentLayout.setVisibility(INVISIBLE);
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.start();
    }

    protected void stopLoadingAnimation() {
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.stop();
        statusLayout.setVisibility(INVISIBLE);
        contentLayout.setVisibility(VISIBLE);
    }

    protected void showNetworkIsUnavailable() {

    }

    public void applyTheme() {

    }

    protected void onPanelClick() {
        // 패널 타입을 체크해 액티비티를 생성 - 패널 데이터인 맵을 같이 넘길 수 있어야 한다.
        Intent intent = new Intent(getContext(), MNPanelDetailActivity.class);
        intent.putExtra(MNPanel.PANEL_DATA_OBJECT, panelDataObject.toString());
        ((Activity)getContext()).startActivityForResult(intent, MNPanel.PANEL_DETAIL_ACTIVITY);
    }
}
