package com.yooiistudios.morningkit.panel.photoalbum.model;

import com.yooiistudios.morningkit.R;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 12.
 */
public enum MNPhotoAlbumTransitionType {
    NONE("NONE", R.id.radio_none, 0),
    ALPHA("ALPHA", R.id.radio_alpha, 300);

    private String mKey;
    private int mRadioId;
    private int mDurationInMillisec;

    private MNPhotoAlbumTransitionType(String key, int id,
                                       int durationInMillisec) {
        mKey = key;
        mRadioId = id;
        mDurationInMillisec = durationInMillisec;
    }

    public String getKey() {
        return mKey;
    }
    public int getRadioId() {
        return mRadioId;
    }
    public int getDurationInMillisec() {
        return mDurationInMillisec;
    }

    public static MNPhotoAlbumTransitionType getTypeByKey(String key) {
        if (key != null) {
            for (MNPhotoAlbumTransitionType type : MNPhotoAlbumTransitionType.values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }
        }
        return null;
    }
}
