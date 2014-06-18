package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 20.
 */
public class MNPhotoAlbumImageView extends ImageView {
    private static final String TAG = "MNPhotoAlbumImageView";
    private boolean mIsReadyForRecycle;

    public MNPhotoAlbumImageView(Context context) {
        super(context);

        init(false);
    }
    public MNPhotoAlbumImageView(Context context, AttributeSet attr) {
        super(context, attr);

        init(false);
    }

    private void init(boolean isReadyForRecycle) {
        mIsReadyForRecycle = isReadyForRecycle;
    }

    public void setIsReadyForRecycle(boolean ready) {
        mIsReadyForRecycle = ready;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

//        MNLog.i(TAG, "onWindowVisibilityChanged. visibility : " + visibility);
        if (visibility == View.GONE && mIsReadyForRecycle) {
            MNBitmapUtils.recycleImageView(this);
//            MNLog.i(TAG, "onWindowVisibilityChanged. recycled");
        }
    }
}
