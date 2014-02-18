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
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrBitmapAsyncTask;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrFetcher;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrFetcherListner;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrPhotoInfo;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout implements MNFlickrFetcherListner,
        MNBitmapLoadSaver.OnLoadListener, MNFlickrBitmapAsyncTask.OnFlickrBitmapAsyncTaskListener {
    private static final String TAG = "MNFlickrPanelLayout";
    private static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";
    private static final Integer FLICKR_FIRST_LOADING_PER_PAGE = 20;
    private MNFlickrPhotoInfo flickrPhotoInfo;
    private RecyclingImageView imageView;
    private Bitmap originalBitmap;
    private Bitmap polishedBitmap;
    private MNFlickrBitmapAsyncTask flickrBitmapAsyncTask;

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

        if (imageView != null) {
            imageView.setImageDrawable(null);
            polishedBitmap = null;
        }

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
        imageView.setImageDrawable(null);
        imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), polishedBitmap));
    }

    /**
     * Flickr Fetcher Listener
     */
    @Override
    public void onFlickrPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo) {
        this.flickrPhotoInfo = flickrPhotoInfo;
        MNBitmapLoadSaver.loadBitmapUsingVolley(flickrPhotoInfo.getPhotoUrlString(), getContext(), this);
    }

    @Override
    public void onErrorResponse() {
        Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server), Toast.LENGTH_SHORT).show();
        showNetworkIsUnavailable();
    }

    /**
     * BitmapLoader - using volley
     */
    @Override
    public void onBitmapLoad(Bitmap bitmap) {
        // 로드한 비트맵을 그레이스케일과 핏 처리
        if (originalBitmap != null) {
            originalBitmap.recycle();
            originalBitmap = null;
        }
        originalBitmap = bitmap;
        getPolishedFlickrBitmap();
    }

    /**
     * FlickrBitmapAsyncTask Listener
     */
    @Override
    public void onProcessingLoad(Bitmap bitmap) {
        if (polishedBitmap != null) {
            imageView.setImageDrawable(null);
            polishedBitmap = null;
        }
        polishedBitmap = bitmap;
        updateUI();
    }

    /**
     * Rotation
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getPolishedFlickrBitmap();
    }

    private void getPolishedFlickrBitmap() {
        // 비동기 처리 - 로딩 애니메이션 on
        startLoadingAnimation();

        // 기존 작업들 취소 & 비트맵 리사이클
        if (flickrBitmapAsyncTask != null) {
            flickrBitmapAsyncTask.cancel(true);
            flickrBitmapAsyncTask = null;
        }
        if (polishedBitmap != null) {
            imageView.setImageDrawable(null);
            polishedBitmap = null;
        }

        // originalBitmap이 있으면 로딩이 되었다고 판단
        if (originalBitmap != null) {
            flickrBitmapAsyncTask = new MNFlickrBitmapAsyncTask(originalBitmap, getWidth(), getHeight(), this);
            flickrBitmapAsyncTask.execute();
        }
    }
}
