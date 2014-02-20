package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;
import com.stevenkim.waterlily.bitmapfun.util.RecyclingBitmapDrawable;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapLoadSaver;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.MNPanelLayout;
import com.yooiistudios.morningkit.panel.MNPanelType;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrBitmapAsyncTask;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrFetcher;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrPhotoInfo;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout implements MNFlickrFetcher.OnFetcherListner,
        MNBitmapLoadSaver.OnLoadListener, MNFlickrBitmapAsyncTask.OnFlickrBitmapAsyncTaskListener {
    private static final String TAG = "MNFlickrPanelLayout";
    private MNFlickrPhotoInfo flickrPhotoInfo;
    private RecyclingImageView imageView;
    private Bitmap originalBitmap;
    private Bitmap polishedBitmap;
    private JsonObjectRequest queryRequest;
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

        // image view
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
        String keyword = "Miranda Kerr";

        // 플리커 로딩을 요청
        if (queryRequest != null) {
            queryRequest.cancel();
        }
        if (flickrPhotoInfo != null) {
            queryRequest = MNFlickrFetcher.requestQuery(keyword, flickrPhotoInfo.getTotalPhotos(), this, getContext());
        } else {
            queryRequest = MNFlickrFetcher.requestFirstQuery(keyword, this, getContext());
        }
    }

    @Override
    protected void updateUI() {
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
        updateUI();
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
    public void onProcessingLoad(Bitmap polishedBitmap) {
        MNLog.i(TAG, "onProcessingLoad");
        if (this.polishedBitmap != null) {
            imageView.setImageDrawable(null);
            this.polishedBitmap = null;
        }
        this.polishedBitmap = polishedBitmap;
        updateUI();
    }

    /**
     * Rotation
     */

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        MNViewSizeMeasure.setViewSizeObserver(imageView, new MNViewSizeMeasure.OnGlobalLayoutObserver() {
            @Override
            public void onLayoutLoad() {
                getPolishedFlickrBitmap();
            }
        });
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

        // 그레이 스케일 설정 가져옴
        boolean isGrayScale = false;

        // originalBitmap이 있으면 로딩이 되었다고 판단
        if (originalBitmap != null) {
            flickrBitmapAsyncTask = new MNFlickrBitmapAsyncTask(originalBitmap,
                    imageView.getWidth(), imageView.getHeight(), isGrayScale, this, getContext());
            flickrBitmapAsyncTask.execute();
        }
    }
}
