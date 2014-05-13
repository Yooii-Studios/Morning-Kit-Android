package com.yooiistudios.morningkit.panel.photoalbum.model;

import com.yooiistudios.morningkit.R;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 12.
 */
public enum MNPhotoAlbumTransitionType {
    NONE("NONE", R.id.radio_none),
    ALPHA("ALPHA", R.id.radio_alpha);

    private String mKey;
    private int mRadioId;

    private MNPhotoAlbumTransitionType(String key, int id) {
        mKey = key;
        mRadioId = id;
    }

    public String getKey() {
        return mKey;
    }
    public int getRadioId() {
        return mRadioId;
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
