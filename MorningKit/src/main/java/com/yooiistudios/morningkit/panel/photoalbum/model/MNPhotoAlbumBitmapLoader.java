package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dongheyon Jeong on in morning-kit from Yooii Studios Co., LTD. on 2014. 5. 26.
 */
public class MNPhotoAlbumBitmapLoader extends AsyncTask<Void, Void, Bitmap> {

    public enum TYPE {
        FILE,
        URI;
    }

    private OnBitmapLoadListener mOnBitmapLoadListener;

    private Context mContext;
    private TYPE mType;
    private String mUrl;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private boolean mUseGrayscale;

    public MNPhotoAlbumBitmapLoader(Context context, TYPE type, String url,
                                    int photoWidth, int photoHeight,
                                    boolean useGrayscale,
                                    OnBitmapLoadListener listener) {
        mContext = context;
        mType = type;
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
            file = getFile(mContext, mType, mUrl);

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
            if (mType.equals(TYPE.URI)) {
                if (file != null && file.exists()) {
                    file.delete();
                }
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

    private File getFile(Context context, TYPE type,
                             String url) throws IOException{
        File file;

        switch (type) {
            case URI:
                file = saveTempFile(context, url);
                break;
            case FILE:
                file = new File(url);
                break;
            default:
                file = null;
                break;
        }

        return file;
    }

    private File saveTempFile(Context context, String uri) {
        File file = new File(context.getCacheDir(), "temp_photo_album_img");
        InputStream inputStream = null;
        OutputStream outStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(Uri.parse(uri));
            outStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len = 0;

            while ((len = inputStream.read(buf)) > 0){
                outStream.write(buf, 0, len);
            }
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public interface OnBitmapLoadListener {
        public void onLoadBitmap(Bitmap bitmap);
        public void onError();
    }
}
