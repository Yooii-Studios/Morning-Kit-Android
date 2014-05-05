package com.yooiistudios.morningkit.panel.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.yooiistudios.morningkit.panel.core.detail.MNPanelDetailActivity;
import com.yooiistudios.morningkit.setting.theme.themedetail.MNTheme;
import com.yooiistudios.morningkit.theme.MNMainResources;

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
public class MNPanelLayout extends RelativeLayout {

    // UI의 레이아웃 구성을 볼 때 사용
    public static boolean DEBUG_UI = false;

    @Getter @Setter MNPanelType panelType;
    @Getter @Setter int panelIndex;
    @Getter RelativeLayout contentLayout;

    // network
    @Getter RelativeLayout loadingLayout;
    @Getter ImageView loadingImageView;
    @Getter TextView loadingTextView;

    // cover
    @Getter RelativeLayout statusLayout;
    @Getter TextView panelNameTextView;
    @Getter TextView panelStatusTextView;

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

        int padding = getResources().getDimensionPixelSize(R.dimen.margin_inner);
//        setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.panel_height));
        layoutParams.weight = 1;
        layoutParams.setMargins(padding, padding,padding, padding);
        setLayoutParams(layoutParams);


        // content layout
        initContentLayout();

        // status layout
        initCoverLayout();

        // onClickListener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPanelClick();
            }
        });

        panelDataObject = new JSONObject();
    }

    private void initContentLayout() {
        contentLayout = new RelativeLayout(getContext());
        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.setLayoutParams(contentLayoutParams);
        addView(contentLayout);
    }

    // 네트워크 사용 레이아웃을 위함
    protected void initNetworkPanel() {
        isUsingNetwork = true;

        // status layout(network, loading) 추가
        loadingLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingLayout.setLayoutParams(layoutParams);
        int padding = getResources().getDimensionPixelSize(R.dimen.panel_layout_padding);
        loadingLayout.setPadding(padding, padding, padding, padding);
        addView(loadingLayout);

        // loadingImageView
        loadingImageView = new ImageView(getContext());
        loadingImageView.setId(9128374);
        int loadingImageSize = getResources().getDimensionPixelSize(R.dimen.panel_network_animation_size);
        LayoutParams loadingImageViewLayoutParams = new LayoutParams(loadingImageSize, loadingImageSize);
        loadingImageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        loadingImageView.setLayoutParams(loadingImageViewLayoutParams);
        loadingImageView.setBackgroundResource(R.drawable.panel_loading_animation);
        loadingLayout.addView(loadingImageView);

        // loadingTextView
        loadingTextView = new TextView(getContext());
        LayoutParams networkStatusTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        networkStatusTextViewLayoutParams.addRule(RelativeLayout.BELOW, loadingImageView.getId());
        networkStatusTextViewLayoutParams.topMargin
                = (int) getResources().getDimension(R.dimen.panel_network_status_gap_margin);
        loadingTextView.setLayoutParams(networkStatusTextViewLayoutParams);
        loadingTextView.setGravity(Gravity.CENTER);
        loadingLayout.addView(loadingTextView);
    }

    // 패널 이름 디스크립션 텍스트뷰를 포함
    protected void initCoverLayout() {
        statusLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        statusLayout.setLayoutParams(layoutParams);
        int padding = getResources().getDimensionPixelSize(R.dimen.panel_layout_padding);
        statusLayout.setPadding(padding, padding, padding, padding);
        addView(statusLayout);
        statusLayout.setVisibility(INVISIBLE); // 최초 INVISIBLE, 필요에 따라 사용

        // panel name
        panelNameTextView = new TextView(getContext());
        LayoutParams nameLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        nameLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        panelNameTextView.setLayoutParams(nameLayoutParams);
        panelNameTextView.setGravity(Gravity.CENTER);
        statusLayout.addView(panelNameTextView);

        // panel status
        panelStatusTextView = new TextView(getContext());
        LayoutParams descriptionLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        descriptionLayoutParams.bottomMargin
//                = (int) getResources().getDimension(R.dimen.margin_outer);
        panelStatusTextView.setLayoutParams(descriptionLayoutParams);
        panelStatusTextView.setGravity(Gravity.CENTER);
        statusLayout.addView(panelStatusTextView);
    }

    public void refreshPanel() throws JSONException {
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

    protected void archivePanelData() throws JSONException {
        MNPanel.archivePanelData(getContext(), getPanelDataObject(), getPanelIndex());
    }

    protected void startLoadingAnimation() {
        loadingLayout.setVisibility(VISIBLE);
        contentLayout.setVisibility(INVISIBLE);
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.start();
        loadingTextView.setText(R.string.loading);
    }

    protected void stopLoadingAnimation() {
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingImageView.getBackground();
        loadingAnimation.stop();
        loadingLayout.setVisibility(INVISIBLE);
        contentLayout.setVisibility(VISIBLE);
    }

    protected void showCoverLayout(String description) {
        statusLayout.setVisibility(VISIBLE);
        panelNameTextView.setText(MNPanelType.toString(getPanelType().getIndex(), getContext()));
        panelStatusTextView.setText(description);
    }

    protected void hideCoverLayout() {
        statusLayout.setVisibility(INVISIBLE);
    }

    protected void showNetworkIsUnavailable() {

    }

    public void applyTheme() {
        setBackgroundResource(MNMainResources.getPanelLayoutSelectorResourcesId(
                MNTheme.getCurrentThemeType(getContext())));
    }

    protected void onPanelClick() {
        // 패널 타입을 체크해 액티비티를 생성 - 패널 데이터인 맵을 같이 넘길 수 있어야 한다.
        Intent intent = new Intent(getContext(), MNPanelDetailActivity.class);
        intent.putExtra(MNPanel.PANEL_DATA_OBJECT, panelDataObject.toString());
        intent.putExtra(MNPanel.PANEL_WINDOW_INDEX, getPanelIndex());

        ((Activity)getContext()).startActivityForResult(intent, MNPanel.PANEL_DETAIL_ACTIVITY);
    }
}
