package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapLoadSaver;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrFetcher;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrFetcherListner;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrPhotoInfo;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout implements MNFlickrFetcherListner, MNBitmapLoadSaver.OnLoadListener {
    private static final String TAG = "MNFlickrPanelLayout";
    private static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;
    private RecyclingImageView imageView;
    private Bitmap flickrBitmap;

    public MNFlickrPanelLayout(Context context) {
        super(context);
    }

    public MNFlickrPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        initNetworkPanel();
        setPanelType(MNPanelType.FLICKR);

        imageView = new RecyclingImageView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);

        getContentLayout().addView(imageView);
    }

    @Override
    protected void processLoading() {
        super.processLoading();

        // 플리커 키워드를 받아온다
        String keyword = "Morning";

        // 플리커 로딩을 요청
        MNFlickrFetcher.requestFirst(keyword, this, getContext());
    }

    @Override
    protected void updateUI() {
        stopLoadingAnimation();
        super.updateUI();

        // 마무리 가공된 Bitmap을 RecycleImageView에 대입
        imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), flickrBitmap));
    }

    @Override
    public void onFlickrPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo) {
        MNBitmapLoadSaver.loadBitmapUsingVolley(flickrPhotoInfo.getPhotoUrlString(), getContext(), this);
    }

    @Override
    public void onErrorResponse() {
        Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
        showNetworkIsUnavailable();
    }

    @Override
    public void onBitmapLoad(Bitmap bitmap) {
        // 로드한 비트맵을 그레이스케일과 핏 처리
        flickrBitmap = bitmap;
        updateUI();
    }
}
