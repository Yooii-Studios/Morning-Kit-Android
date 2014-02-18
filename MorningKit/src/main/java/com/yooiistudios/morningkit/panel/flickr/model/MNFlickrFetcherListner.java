package com.yooiistudios.morningkit.panel.flickr.model;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrFetcherListner
 */
public interface MNFlickrFetcherListner {
    public void onFlickrPhotoInfoLoaded(MNFlickrPhotoInfo flickrPhotoInfo);
    public void onErrorResponse();
}
