package com.yooiistudios.morningkit.panel.flickr.model;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 4. 16.
 *
 * MNFlickrFetchAsyncTask
 *  Volley에서 AsyncTask로 변경(Volley에 여러 문제가 있다고 판단)
 */
public class MNFlickrPhotoInfoFetchAsyncTask extends AsyncTask<Void, Void, MNFlickrPhotoInfo> {

    private static final String TAG = "MNFlickrFetchAsyncTask";

    String keyword;
    int totalPhotos = -1;
    OnFlickrFetchAsyncTaskListener listener;

    public interface OnFlickrFetchAsyncTaskListener {
        public void onErrorOnLoad();
        public void onNotFoundPhotoInfoOnKeyword();
        public void onFlickrPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo);
    }

    public MNFlickrPhotoInfoFetchAsyncTask(String keyword, int totalPhotos,
                                           OnFlickrFetchAsyncTaskListener listener) {
        this.keyword = keyword;
        this.totalPhotos = totalPhotos;
        this.listener = listener;
    }

    @Override
    protected MNFlickrPhotoInfo doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(MNFlickrPhotoInfo flickrPhotoInfo) {
        super.onPostExecute(flickrPhotoInfo);
    }
}
