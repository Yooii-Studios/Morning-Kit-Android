package com.yooiistudios.morningkit.panel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.shadow.RoundShadowRelativeLayout;
import com.yooiistudios.morningkit.panel.detail.MNPanelDetailActivity;

import org.json.JSONException;
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
    @Getter RelativeLayout contentLayout;

    // network
    @Getter RelativeLayout statusLayout;
    @Getter ImageView loadingImageView;
    @Getter TextView networkStatusTextView;

    // cover
    @Getter RelativeLayout coverLayout;
    @Getter TextView panelNameTextView;
    @Getter TextView panelDescriptionTextView;

    @Getter boolean isUsingNetwork;
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.panel_height));
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
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        statusLayout.setLayoutParams(layoutParams);
        statusLayout.setBackgroundColor(Color.RED);
        int padding = getResources().getDimensionPixelSize(R.dimen.panel_detail_padding);
        statusLayout.setPadding(padding, padding, padding, padding);
        addView(statusLayout);

        // loadingImageView
        loadingImageView = new ImageView(getContext());
        loadingImageView.setId(9128374);
        int loadingImageSize = getResources().getDimensionPixelSize(R.dimen.panel_network_animation_size);
        LayoutParams loadingImageViewLayoutParams = new LayoutParams(loadingImageSize, loadingImageSize);
        loadingImageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        loadingImageView.setLayoutParams(loadingImageViewLayoutParams);
        loadingImageView.setBackgroundResource(R.drawable.panel_loading_animation);
        statusLayout.addView(loadingImageView);

        // networkStatusTextView
        networkStatusTextView = new TextView(getContext());
        LayoutParams networkStatusTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        networkStatusTextViewLayoutParams.addRule(RelativeLayout.BELOW, loadingImageView.getId());
        networkStatusTextViewLayoutParams.topMargin
                = (int) getResources().getDimension(R.dimen.panel_network_status_gap_margin);
        networkStatusTextView.setLayoutParams(networkStatusTextViewLayoutParams);
        networkStatusTextView.setGravity(Gravity.CENTER);
        statusLayout.addView(networkStatusTextView);
    }

    // 패널 이름 디스크립션 텍스트뷰를 포함
    protected void initCoverPanel() {

    }

    public void refreshPanel() {
        if (isUsingNetwork) {
            // 네트워크 체크
            boolean isReachable = true;
            if (isReachable) {
                try {
                    processLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                archivePanelData();
            } else {
                // 네트워크 불가 이미지
                showNetworkIsUnavailable();
            }
        } else {
            try {
                processLoading();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateUI();
            archivePanelData();
        }
    }

    protected void processLoading() throws JSONException {
        if (isUsingNetwork) {
            startLoadingAnimation();
        }
    }

    protected void updateUI() {
        if (isUsingNetwork) {
            stopLoadingAnimation();
        }
    }

    protected void archivePanelData() {
        MNPanel.archivePanelData(getContext(), getPanelDataObject(), getPanelIndex());
    }

    protected void startLoadingAnimation() {
        statusLayout.setVisibility(VISIBLE);
        contentLayout.setVisibility(INVISIBLE);
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.start();
        networkStatusTextView.setText(R.string.loading);
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
        intent.putExtra(MNPanel.PANEL_WINDOW_INDEX, getPanelIndex());

        ((Activity)getContext()).startActivityForResult(intent, MNPanel.PANEL_DETAIL_ACTIVITY);
    }
}
