package com.yooiistudios.morningkit.panel.flickr.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;

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
    private OnFlickrBitmapAsyncTaskListener flickrBitmapAsyncTaskListener;

    public interface OnFlickrBitmapAsyncTaskListener {
        public void onProcessingLoad(Bitmap bitmap);
    }

    public MNFlickrBitmapAsyncTask(Bitmap bitmap, int width, int height,
                                   OnFlickrBitmapAsyncTaskListener flickrBitmapAsyncTaskListener) {
        MNLog.i(TAG, "constructor: " + bitmap);
        this.originalBitmap = bitmap;
        this.width = width;
        this.height = height;
        this.flickrBitmapAsyncTaskListener = flickrBitmapAsyncTaskListener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // 크롭, 라운딩, 그레이스케일 등등 처리하기
        Bitmap polishedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height);
        return polishedBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        MNLog.i(TAG, "onPostExecute: " + bitmap);
        flickrBitmapAsyncTaskListener.onProcessingLoad(bitmap);
    }
}
