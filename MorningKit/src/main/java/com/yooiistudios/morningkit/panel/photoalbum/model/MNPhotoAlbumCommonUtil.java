package com.yooiistudios.morningkit.panel.photoalbum.model;

import static com.yooiistudios.morningkit.panel.photoalbum.MNPhotoAlbumDetailFragment.INVALID_INTERVAL;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 24.
 */
public class MNPhotoAlbumCommonUtil {

    public static long getTransitionInterval(int intervalMinute,
                                          int intervalSecond) {
        if (intervalMinute != INVALID_INTERVAL &&
                intervalSecond != INVALID_INTERVAL) {
            return (intervalMinute * 60 +
                    intervalSecond) * 1000;
        }
        else {
            return INVALID_INTERVAL;
        }
    }
}
