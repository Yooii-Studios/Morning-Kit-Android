package com.yooiistudios.morningkit.panel.flickr;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapLoadSaver;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.common.file.ExternalStorageManager;
import com.yooiistudios.morningkit.common.log.MNLog;
import com.yooiistudios.morningkit.common.size.MNViewSizeMeasure;
import com.yooiistudios.morningkit.panel.core.MNPanelLayout;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrBitmapAsyncTask;
import com.yooiistudios.morningkit.panel.flickr.model.MNFlickrPhotoInfo;
import com.yooiistudios.morningkit.panel.flickr.model.MNPhotoInfoFetcher;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNFlickrPanelLayout
 */
public class MNFlickrPanelLayout extends MNPanelLayout implements MNBitmapLoadSaver.OnLoadListener,
        MNFlickrBitmapAsyncTask.OnFlickrBitmapAsyncTaskListener, MNPhotoInfoFetcher.OnPhotoInfoFetchListener {
    private static final String TAG = "MNFlickrPanelLayout";

    public static final String FLICKR_PREFS = "FLICKR_PREFS";
    public static final String FLICKR_PREFS_KEYWORD = "FLICKR_PREFS_KEYWORD";
    public static final String FLICKR_DATA_KEYWORD = "FLICKR_DATA_KEYWORD";
    public static final String FLICKR_DATA_FLICKR_INFO = "FLICKR_DATA_INFO";
    public static final String FLICKR_DATA_PHOTO_ID = "FLICKR_DATA_PHOTO_ID";
    public static final String FLICKR_DATA_GRAYSCALE = "FLICKR_DATA_GRAYSCALE";

    private MNFlickrPhotoInfo flickrPhotoInfo;
    private ImageView imageView;
    private String keywordString;
    private Bitmap originalBitmap;
    private Bitmap polishedBitmap;
    private MNPhotoInfoFetcher photoInfoFetchAsyncTask;
    private MNFlickrBitmapAsyncTask flickrBitmapAsyncTask;
    private boolean isGrayScale;
    private boolean isRefreshing = false;

    public MNFlickrPanelLayout(Context context) {
        super(context);
    }
    public MNFlickrPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();

        // image view
        imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        int strokeMargin = getResources().getDimensionPixelSize(R.dimen.theme_shape_width_stroke);
        layoutParams.setMargins(strokeMargin, strokeMargin, strokeMargin, strokeMargin);
        imageView.setLayoutParams(layoutParams);
        getContentLayout().addView(imageView);
    }

    private void clearBitmap() {
        if (imageView != null) {
            Drawable d = imageView.getDrawable();
            if (d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable)d).getBitmap();
                if (b != null) {
                    b.recycle();
                    MNLog.now("flickr imageview recycle Bitmap");
                }
            }
            imageView.setImageBitmap(null);
            polishedBitmap = null;
        }
    }

    @Override
    protected void processLoading() throws JSONException {
        super.processLoading();

        // 그레이 스케일 설정 가져옴
        isGrayScale = false;
        if (getPanelDataObject().has(FLICKR_DATA_GRAYSCALE)) {
            isGrayScale = getPanelDataObject().getBoolean(FLICKR_DATA_GRAYSCALE);
        }

        // 이미지뷰 초기화
        clearBitmap();

        // 기존 쿼리는 취소
        if (photoInfoFetchAsyncTask != null) {
            photoInfoFetchAsyncTask.cancel(true);
        }

        // 기존에 읽었던 플리커 정보 로딩 - Gson으로 캐스팅
        if (getPanelDataObject().has(FLICKR_DATA_FLICKR_INFO)) {
            String flickrInfoString = getPanelDataObject().getString(FLICKR_DATA_FLICKR_INFO);
            Type type = new TypeToken<MNFlickrPhotoInfo>(){}.getType();
            flickrPhotoInfo = new Gson().fromJson(flickrInfoString, type);
        }

        // 플리커 키워드를 받아온다
//        keyword = "Miranda Kerr";
        String previousKeyword = keywordString;
        if (getPanelDataObject().has(FLICKR_DATA_KEYWORD)) {
            // 이전 스트링과 비교해서, 같지 않으면 첫 로딩, 같으면 기존에 가지고 있던 총 사진 갯수를 가지고 로딩
            keywordString = getPanelDataObject().getString(FLICKR_DATA_KEYWORD);
            if (keywordString.equals(previousKeyword) && flickrPhotoInfo != null &&
                    flickrPhotoInfo.getTotalPhotos() != 0) {
                // 기존 로딩
                photoInfoFetchAsyncTask = MNPhotoInfoFetcher.newQueryInstance(keywordString,
                        flickrPhotoInfo.getTotalPhotos(), this);
                photoInfoFetchAsyncTask.execute();
            } else {
                // 새 키워드의 첫 로딩
                photoInfoFetchAsyncTask = MNPhotoInfoFetcher.newFirstQueryInstance(keywordString, this);
                photoInfoFetchAsyncTask.execute();
            }
        } else {
            SharedPreferences prefs = getContext().getSharedPreferences(FLICKR_PREFS, Context.MODE_PRIVATE);
            keywordString = prefs.getString(FLICKR_PREFS_KEYWORD, "Morning");

            // 새 키워드의 첫 로딩
            photoInfoFetchAsyncTask = MNPhotoInfoFetcher.newFirstQueryInstance(keywordString, this);
            photoInfoFetchAsyncTask.execute();
        }

        // 키워드 저장
        getPanelDataObject().put(FLICKR_DATA_KEYWORD, keywordString);
    }

    @Override
    protected void updateUI() {
        super.updateUI();

        // 리프레시 플래그 처리(로딩하는 동안엔 회전에 대응하지 않게 구현)
        isRefreshing = false;

        // 마무리 가공된 Bitmap을 RecycleImageView에 대입
//        imageView.setImageDrawable(null);
//        imageView.setImageDrawable(new RecyclingBitmapDrawable(getResources(), polishedBitmap));
        imageView.setImageBitmap(polishedBitmap);
    }

    /**
     * FlickrPhotoInfoFetchAsyncTask Listener
     */
    @Override
    public void onPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo) {
        this.flickrPhotoInfo = flickrPhotoInfo;
        try {
            getPanelDataObject().put(FLICKR_DATA_FLICKR_INFO, new Gson().toJson(flickrPhotoInfo));
            archivePanelData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MNBitmapLoadSaver.loadBitmapUsingVolley(flickrPhotoInfo.getPhotoUrlString(),
                getContext().getApplicationContext(), this);
    }

    @Override
    public void onErrorOnLoad() {
        Toast.makeText(getContext(), getResources().getString(R.string.flickr_error_access_server),
                Toast.LENGTH_SHORT).show();
        showNetworkIsUnavailable();
        updateUI();
    }

    @Override
    public void onNotFoundPhotoInfoOnKeyword() {
        // 해당 키워드에 대한 결과가 없을 경우 처리
        Toast.makeText(getContext(), getResources().getString(R.string.flickr_not_available_flickr_url),
                Toast.LENGTH_SHORT).show();
        showNetworkIsUnavailable();
        updateUI();
    }

    /**
     * BitmapLoader - using volley
     */
    @Override
    public void onBitmapLoad(Bitmap bitmap) throws JSONException {
        // 로드한 비트맵을 그레이스케일과 핏 처리
        if (originalBitmap != null) {
            originalBitmap.recycle();
            originalBitmap = null;
        }
        originalBitmap = bitmap;
        if (isGrayScale) {
            // 쓰레드를 일단 사용하지 않는 것으로 결정
            originalBitmap = MNBitmapProcessor.getGrayScaledBitmap(originalBitmap);
        }
        getPolishedFlickrBitmap();
    }

    /**
     * FlickrBitmapAsyncTask Listener
     */
    @Override
    public void onBitmapProcessingLoad(Bitmap polishedBitmap) {
        if (this.polishedBitmap != null) {
//            imageView.setImageDrawable(null);
            imageView.setImageBitmap(null);
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
                try {
                    if (!isRefreshing) {
                        getPolishedFlickrBitmap();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPolishedFlickrBitmap() throws JSONException {
        // 비동기 처리 - 로딩 애니메이션 on
        startLoadingAnimation();

        // 기존 작업들 취소 & 비트맵 리사이클
        if (flickrBitmapAsyncTask != null) {
            flickrBitmapAsyncTask.cancel(true);
            flickrBitmapAsyncTask = null;
        }
        if (polishedBitmap != null) {
//            imageView.setImageBitmap(null);
//            polishedBitmap = null;
            clearBitmap();
        }

        // originalBitmap이 있으면 로딩이 되었다고 판단
        if (originalBitmap != null) {
            flickrBitmapAsyncTask = new MNFlickrBitmapAsyncTask(originalBitmap,
                    imageView.getWidth(), imageView.getHeight(), isGrayScale, this, getContext());
            flickrBitmapAsyncTask.execute();
        }
    }

    @Override
    protected void onPanelClick() {
        try {
            if (flickrPhotoInfo != null) {
                getPanelDataObject().put(FLICKR_DATA_PHOTO_ID, flickrPhotoInfo.getPhotoId());
            }
            if (originalBitmap != null) {
                try {
                    MNBitmapProcessor.saveBitmapToDirectory(getContext(), originalBitmap,
                            "flickr_" + getPanelIndex(),
                            ExternalStorageManager.APP_DIRECTORY_HIDDEN + "/flickr");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            getPanelDataObject().put(FLICKR_DATA_KEYWORD, keywordString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 모달 액티비티 띄우기
        super.onPanelClick();
    }

    @Override
    public void refreshPanel() throws JSONException {
        super.refreshPanel();

        isRefreshing = true;
    }
}
