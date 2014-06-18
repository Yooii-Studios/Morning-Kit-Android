package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ViewSwitcher;

import com.stevenkim.waterlily.bitmapfun.util.AsyncTask;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import lombok.Getter;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 10.
 */
public class MNPhotoAlbumDisplayHelper {
    private Activity mActivity;
    private ViewSwitcher mViewSwitcher;
    private String mRootDir;
    private ArrayList<String> mFileList;
    private String mSelectedFile;
    private MNPhotoAlbumImageView mFirstView;
    private MNPhotoAlbumImageView mSecondView;
    private MNPhotoAlbumTransitionType mTransitionType;
    private long mInterval;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private int mPhotoIdx;
    private boolean mUseGrayscale;

    private long mFirstLoadedTimeInMilli = INVALID_TIME;

    private MNPhotoAlbumBitmapLoader mBitmapLoader;
    private OnStartListener mOnStartListener;

    private static final int HANDLER_WHAT = 100;
    private static final int INVALID_INDEX = -1;
    private static final long INVALID_TIME = -1;

    @Getter private boolean isRunning;

    public MNPhotoAlbumDisplayHelper(Activity activity,
                                     ViewSwitcher viewSwitcher,
                                     final OnStartListener onStartListener) {
        if (viewSwitcher == null) {
            throw new IllegalArgumentException(viewSwitcher.toString() + " " +
                    "CANNOT be null");
        }
        int viewSwitcherChildCount = viewSwitcher.getChildCount();
        if (viewSwitcherChildCount != 2) {
            throw new IllegalArgumentException(viewSwitcher.toString() + " " +
                    "MUST HAVE 2 child exactly.");
        }
        else {
            for (int i = 0; i < viewSwitcherChildCount; i++ ) {
                View child = viewSwitcher.getChildAt(i);
                if (!(child instanceof MNPhotoAlbumImageView)) {
                    throw new IllegalStateException(viewSwitcher.toString()
                            + " MUST HAVE MNPhotoAlbumImageView child ONLY.");
                }
            }
        }
        mActivity = activity;
        mViewSwitcher = viewSwitcher;
        mOnStartListener = onStartListener;
        mFirstView = (MNPhotoAlbumImageView)(mViewSwitcher.getChildAt(0));
        mSecondView = (MNPhotoAlbumImageView)(mViewSwitcher.getChildAt(1));
    }
    public void stop() {
        displayHandler.removeMessages(HANDLER_WHAT);

        mViewSwitcher.setInAnimation(null);
        mViewSwitcher.setOutAnimation(null);

        if (mBitmapLoader != null) {
            mBitmapLoader.cancel(true);
        }
        isRunning = false;
        mFirstLoadedTimeInMilli = INVALID_TIME;
    }

    public synchronized void start(String rootDir,
            ArrayList<String> fileList, String selectedFile,
            MNPhotoAlbumTransitionType transitionType, long interval,
            boolean useGrayscale, int photoWidth, int photoHeight,
            boolean startFromSelection) {

        mFirstLoadedTimeInMilli = System.currentTimeMillis();
        stop();

        mRootDir = rootDir;
        mSelectedFile = selectedFile;
        mTransitionType = transitionType;
        mInterval = interval;
        mUseGrayscale = useGrayscale;
        mPhotoWidth = photoWidth;
        mPhotoHeight = photoHeight;

        isRunning = true;

        mFileList = MNPhotoAlbumFileManager.getValidImageFileList(
                mRootDir, fileList);
        if (mFileList.size() == 0) {
            isRunning = false;
            if (mOnStartListener != null) {
                mOnStartListener.onError(R.string.photo_album_no_image);
            }
        }
        else {
            Collections.shuffle(mFileList, new Random(System.nanoTime()));
            String fileName;

            if (interval < 0 || startFromSelection) {
                // If slideshow turned off, show selected file.
                if (mSelectedFile != null && mFileList.contains(mSelectedFile)) {
                    mFileList.remove(mSelectedFile);
                    mFileList.add(0, mSelectedFile);
                }
            }

            mPhotoIdx = 0;
            fileName = mFileList.get(mPhotoIdx);

            if (mOnStartListener != null) {
                mOnStartListener.onStartLoadingBitmap();
            }

            mBitmapLoader = new MNPhotoAlbumBitmapLoader(mActivity,
                    new File(mRootDir, fileName).getAbsolutePath(),
                    mPhotoWidth, mPhotoHeight, mUseGrayscale,
                    new MNPhotoAlbumBitmapLoader.OnBitmapLoadListener() {
                @Override
                public void onLoadBitmap(Bitmap bitmap) {
                    MNLog.i("MNPhotoAlbumBitmapLoader", "onLoadBitmap");
                    if (mBitmapLoader.isCancelled()) {
                        bitmap.recycle();
                        return;
                    }
                    mFirstView.setImageBitmap(bitmap);
                    if (mOnStartListener != null) {
                        mOnStartListener.onFirstBitmapLoad();
                    }

                    //init view switcher
                    mViewSwitcher.setDisplayedChild(0);

                    if (mFileList.size() > 1 && mInterval > 0) {
                        //prepare for timer

                        long timePassedSinceStart = System.currentTimeMillis() -
                                mFirstLoadedTimeInMilli;
                        long actualInterval = mInterval +
                                mTransitionType.getDurationInMillisec();
                        long delay = timePassedSinceStart % actualInterval;


                        displayHandler.sendEmptyMessageDelayed(HANDLER_WHAT,
                                actualInterval - delay);
                    }
                    else {
                        isRunning = false;
                    }
                }

                @Override
                public void onError() {
                    MNLog.i("MNPhotoAlbumBitmapLoader", "onError");
                    isRunning = false;
                    if (mOnStartListener != null) {
                        mOnStartListener.onError(
                                R.string.photo_album_error_getting_photo);
                    }
                }
            });
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mBitmapLoader.executeOnExecutor(AsyncTask
                        .THREAD_POOL_EXECUTOR);
            }
            else {
                mBitmapLoader.execute();
            }
        }
    }

    public synchronized void setRootDir(String rootDir) {
        mRootDir = rootDir;
    }
    public synchronized void setSelectedFile(String selectedFile) {
        mSelectedFile = selectedFile;
    }
    public synchronized void setFileList(ArrayList<String> fileList) {
        mFileList = fileList;
    }
    public synchronized void setInterval(long interval) {
        mInterval = interval;
    }
    public synchronized void setPhotoWidth(int width) {
        mPhotoWidth = width;
    }
    public synchronized void setPhotoHeight(int height) {
        mPhotoHeight = height;
    }
    public void restart(boolean startFromSelection) {
        stop();
        start(mRootDir, mFileList, mSelectedFile, mTransitionType, mInterval,
                mUseGrayscale, mPhotoWidth, mPhotoHeight, startFromSelection);
    }
    public synchronized void setTransitionType(MNPhotoAlbumTransitionType type) {
        mTransitionType = type;
    }
    public synchronized void setUseGrayscale(boolean useGrayscale) {
        mUseGrayscale = useGrayscale;
    }

    public void notifyContainingActivityWillBeShown() {
        mFirstView.setIsReadyForRecycle(false);
        mSecondView.setIsReadyForRecycle(false);
    }
    public void notifyContainingActivityWillBeGone() {
        mFirstView.setIsReadyForRecycle(true);
        mSecondView.setIsReadyForRecycle(true);
    }
    private synchronized void showNext(Bitmap bitmap) {
        int curViewIdx = mViewSwitcher.getDisplayedChild();

        if (curViewIdx%2 ==  0) {
            mSecondView.setImageBitmap(bitmap);
            mViewSwitcher.showNext();
        } else {
            mFirstView.setImageBitmap(bitmap);
            mViewSwitcher.showPrevious();
        }
    }

//    private int getRandomIndex() {
//        int fileCount = mFileList.size();
//        if (fileCount == 0) {
//            return INVALID_INDEX;
//        }
//        Random random = new Random(System.currentTimeMillis());
//        return random.nextInt(mFileList.size());
//    }

    private Animation.AnimationListener mAnimListener =
            new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int curViewIdx = mViewSwitcher.getDisplayedChild();

                    if (curViewIdx%2 ==  0) {
                        MNBitmapUtils.recycleImageView(mSecondView);
                    } else {
                        MNBitmapUtils.recycleImageView(mFirstView);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

    private Handler displayHandler = new Handler() {

        @Override
        public void handleMessage( Message msg ){
            if (mFirstLoadedTimeInMilli == INVALID_TIME) {
                mFirstLoadedTimeInMilli = System.currentTimeMillis();
            }

            long timePassedSinceStart = System.currentTimeMillis() -
                    mFirstLoadedTimeInMilli;
            long actualInterval = mInterval +
                    mTransitionType.getDurationInMillisec();
            long delay = timePassedSinceStart % actualInterval;
//            long delay = mInterval + animationDuration;

            displayHandler.sendEmptyMessageDelayed(HANDLER_WHAT,
                    actualInterval - delay);

            if (isRunning){
                if (mViewSwitcher.getInAnimation() == null ||
                        mViewSwitcher.getOutAnimation() == null) {
                    Animation[] animArr = MNPhotoAlbumTransitionFactory.
                            makeTransitionAnimation(mTransitionType);

                    Animation inAnimation = animArr[0];
                    Animation outAnimation = animArr[1];
                    inAnimation.setAnimationListener(mAnimListener);
                    outAnimation.setAnimationListener(mAnimListener);
                    mViewSwitcher.setInAnimation(inAnimation);
                    mViewSwitcher.setOutAnimation(outAnimation);
                }

                // UI갱신
                if (mFileList.size() == 0) {
                    stop();
                    if (mOnStartListener != null) {
                        mOnStartListener.onError(R.string.photo_album_no_image);
                    }

                    return;
                }

                mPhotoIdx++;
                if (mPhotoIdx == mFileList.size()) {
                    mPhotoIdx = 0;
                }

                String fileName = mFileList.get(mPhotoIdx);
                mBitmapLoader = new MNPhotoAlbumBitmapLoader(mActivity,
                        new File(mRootDir, fileName).getAbsolutePath(),
                        mPhotoWidth, mPhotoHeight, mUseGrayscale,
                        new MNPhotoAlbumBitmapLoader.OnBitmapLoadListener() {
                            @Override
                            public void onLoadBitmap(Bitmap bitmap) {
                                if (mBitmapLoader.isCancelled()) {
                                    bitmap.recycle();
                                    return;
                                }
                                showNext(bitmap);
                            }

                            @Override
                            public void onError() {
                                mFileList.remove(mPhotoIdx);

                                displayHandler.sendEmptyMessage(HANDLER_WHAT);
                            }
                        });
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mBitmapLoader.executeOnExecutor(AsyncTask
                            .THREAD_POOL_EXECUTOR);
                }
                else {
                    mBitmapLoader.execute();
                }
            }
        }
    };

    public interface OnStartListener {
        public void onStartLoadingBitmap();
        public void onFirstBitmapLoad();
        public void onError(int messageResId);
    }

}
