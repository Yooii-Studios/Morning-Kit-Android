package com.yooiistudios.morningkit.panel.flickr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 18.
 *
 * MNFlickrPhotoInfo
 *  Json에서 파싱한 정보를 포함하는 자료구조
 */
public class MNFlickrPhotoInfo {
    @Getter @Setter int totalPhotos;
    @Getter @Setter String photoUrlString;
    @Getter @Setter String photoId;
}
