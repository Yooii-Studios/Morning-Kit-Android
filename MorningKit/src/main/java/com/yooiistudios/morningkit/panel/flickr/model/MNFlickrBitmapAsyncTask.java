package com.yooiistudios.morningkit.panel.flickr.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.common.log.MNLog;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrBitmapAsyncTask
 *  원본 플리커 비트맵을 다양하게 처리
 */
public class MNFlickrBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = "MNFlickrBitmapAsyncTask";
    private Bitmap originalBitmap;
    private int width;
    private int height;
    boolean isGrayScale;
    private OnFlickrBitmapAsyncTaskListener flickrBitmapAsyncTaskListener;
    private Context context;

    public interface OnFlickrBitmapAsyncTaskListener {
        public void onProcessingLoad(Bitmap polishedBitmap);
    }

    public MNFlickrBitmapAsyncTask(Bitmap bitmap, int width, int height, boolean isGrayScale,
                                   OnFlickrBitmapAsyncTaskListener flickrBitmapAsyncTaskListener,
                                   Context context) {
        MNLog.i(TAG, "constructor: " + bitmap);
        this.originalBitmap = bitmap;
        this.width = width;
        this.height = height;
        this.isGrayScale = isGrayScale;
        this.flickrBitmapAsyncTaskListener = flickrBitmapAsyncTaskListener;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // 크롭, 라운딩, 그레이스케일 등등 처리하기
        MNLog.now("doInBackground: " + width + "/" + height);
        Bitmap croppedBitmap = MNBitmapProcessor.getCroppedBiamtp(originalBitmap, width, height);
        return MNBitmapProcessor.getRoundedCornerBitmap(croppedBitmap, width, height, isGrayScale,
                (int) context.getResources().getDimension(R.dimen.panel_flickr_round_radius));
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        MNLog.i(TAG, "onPostExecute: " + bitmap); // 입력 bitmap 과 다른 비트맵 id로 만들어져 나가야 한다
        flickrBitmapAsyncTaskListener.onProcessingLoad(bitmap);
    }
}
