package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;

import java.io.File;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 26.
 */
public class MNPhotoAlbumBitmapLoader extends AsyncTask<Void, Void, Bitmap> {

    private OnBitmapLoadListener mOnBitmapLoadListener;

    private Context mContext;
    private String mUrl;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private boolean mUseGrayscale;

    public MNPhotoAlbumBitmapLoader(Context context, String url,
                                    int photoWidth, int photoHeight,
                                    boolean useGrayscale,
                                    OnBitmapLoadListener listener) {
        mContext = context;
        mUrl = url;
        mPhotoWidth = photoWidth;
        mPhotoHeight = photoHeight;
        mUseGrayscale = useGrayscale;
        mOnBitmapLoadListener = listener;
    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        File file = null;
        Bitmap bitmap = null;
        Bitmap croppedBitmap = null;
        Bitmap polishedBitmap;
        try {
            file = new File(mUrl);

            bitmap = MNBitmapProcessor.createSampleSizedBitmap(
                    file, mPhotoWidth, mPhotoHeight);

            croppedBitmap = MNBitmapProcessor.
                    getCroppedBitmap(bitmap, mPhotoWidth, mPhotoHeight);
            bitmap.recycle();

            polishedBitmap = MNBitmapProcessor.
                    getRoundedCornerBitmap(croppedBitmap, mPhotoWidth, mPhotoHeight,
                            mUseGrayscale,
                            (int) mContext.getResources()
                                    .getDimension(
                                            R.dimen.panel_round_radius));
            croppedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            polishedBitmap = null;
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (croppedBitmap != null) {
                croppedBitmap.recycle();
            }
        }

        return polishedBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (mOnBitmapLoadListener != null) {
            if (bitmap != null) {
                mOnBitmapLoadListener.onLoadBitmap(bitmap);
            }
            else {
                mOnBitmapLoadListener.onError();
            }
        }
    }


    public interface OnBitmapLoadListener {
        public void onLoadBitmap(Bitmap bitmap);
        public void onError();
    }
}
