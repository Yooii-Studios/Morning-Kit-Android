package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ViewSwitcher;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;
import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Getter;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 10.
 */
public class MNPhotoAlbumDisplayHelper {
    private Activity mActivity;
    private ViewSwitcher mViewSwitcher;
//    private ViewGroup mParentView;
    private String mRootDir;
    private ArrayList<String> mFileList;
    private MNPhotoAlbumImageView mFirstView;
    private MNPhotoAlbumImageView mSecondView;
    private Timer mTimer;
    private MNPhotoAlbumTransitionType mTransitionType;
    private long mInterval;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private int mPhotoIdx;
    private boolean mUseGrayscale;

//    private final Object fileListLock = new Object();
//    private final Object rootDirLock = new Object();
//    private final Object grayscaleLock = new Object();

    @Getter private boolean isRunning;

    public MNPhotoAlbumDisplayHelper(Activity activity,
                                     ViewSwitcher viewSwitcher,
                                     int photoWidth, int photoHeight) {
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
//        mViewSwitcher.setOnSwitchAttachStateChangeListener(
//                mOnSwitcherAttachStateChangedListener);
        mFirstView = (MNPhotoAlbumImageView)(mViewSwitcher.getChildAt(0));
        mSecondView = (MNPhotoAlbumImageView)(mViewSwitcher.getChildAt(1));
        mPhotoWidth = photoWidth;
        mPhotoHeight = photoHeight;
    }
    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        isRunning = false;

//        mParentView.removeView(mViewSwitcher);

//        MNBitmapUtils.recycleImageView(mFirstView);
//        MNBitmapUtils.recycleImageView(mSecondView);
    }

    public synchronized void start(String rootDir,
            ArrayList<String> fileList,
            MNPhotoAlbumTransitionType transitionType,
            long interval,
            boolean useGrayscale) {
        stop();

        mRootDir = rootDir;
//        setFileList(fileList);
        mTransitionType = transitionType;
        mInterval = interval;
        mUseGrayscale = useGrayscale;

//        mParentView.addView(mViewSwitcher,
//                new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT)
//        );

        isRunning = true;

        mFileList = MNPhotoAlbumFileManager.getValidImageFileList(
                mRootDir, fileList);
//        setFileList(MNPhotoAlbumFileManager.getValidImageFileList(
//                        mRootDir, fileList));
        if (mFileList.size() == 0) {
            //TODO no images to display. show error message.
        }
        else {
//            mPhotoIdx = 0;
            mPhotoIdx = getRandomIndex();

            //show first image
            String fileName = mFileList.get(mPhotoIdx);
            Bitmap bitmap = getPolishedBitmap(new File(mRootDir, fileName));
            mFirstView.setImageBitmap(bitmap);

            //init view switcher
            mViewSwitcher.setDisplayedChild(0);
            Animation[] animArr = MNPhotoAlbumTransitionFactory.
                    makeTransitionAnimation(mTransitionType);
            Animation inAnimation = animArr[0];
            Animation outAnimation = animArr[1];
            inAnimation.setAnimationListener(mAnimListener);
            outAnimation.setAnimationListener(mAnimListener);
            mViewSwitcher.setInAnimation(inAnimation);
            mViewSwitcher.setOutAnimation(outAnimation);

            if (mFileList.size() > 1 && mInterval > 0) {
                //prepare for timer
                mTimer = new Timer();
                mTimer.schedule(new PhotoDisplayTask(), mInterval, mInterval);
            }
            else {
                isRunning = false;
            }
        }
    }

    public synchronized void setRootDir(String rootDir) {
        mRootDir = rootDir;
    }
    public synchronized void setFileList(ArrayList<String> fileList) {
        mFileList = fileList;
    }
    public synchronized void setInterval(long interval) {
        mInterval = interval;
//        if (isRunning()) {
//            restart();
//        }
    }
    public void restart() {
        stop();
        start(mRootDir, mFileList, mTransitionType, mInterval, mUseGrayscale);
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer.purge();
//            mTimer = null;
//        }
//        mTimer = new Timer();
//        mTimer.schedule(new PhotoDisplayTask(), mInterval, mInterval);
    }
    public void pause() {

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

    private Bitmap getPolishedBitmap(File file) {
        Bitmap bitmap = MNBitmapProcessor.createSampleSizedBitmap(
                file, mPhotoWidth, mPhotoHeight);
        Bitmap croppedBitmap = MNBitmapProcessor.
                getCroppedBitmap(bitmap, mPhotoWidth, mPhotoHeight);
        Bitmap polishedBitmap = MNBitmapProcessor.
                getRoundedCornerBitmap(croppedBitmap, mPhotoWidth, mPhotoHeight,
                        mUseGrayscale,
                        (int) mActivity.getResources()
                                .getDimension(
                                        R.dimen.panel_flickr_round_radius));
        croppedBitmap.recycle();

        return polishedBitmap;
    }
    private synchronized void showNext() {
        mPhotoIdx = getRandomIndex();
//        mPhotoIdx++;
//
//        if (mPhotoIdx == mFileList.size()) {
//            mPhotoIdx = 0;
//        }

        int curViewIdx = mViewSwitcher.getDisplayedChild();

        Bitmap bitmap = null;
        String fileName = mFileList.get(mPhotoIdx);
        bitmap = getPolishedBitmap(new File(mRootDir, fileName));

        if (bitmap == null) {
            // remove invalid photo item.
            mFileList.remove(mPhotoIdx);

            showNext();
            return;
        }

        if (curViewIdx%2 ==  0) {
            mSecondView.setImageBitmap(bitmap);
            mViewSwitcher.showNext();
        } else {
            mFirstView.setImageBitmap(bitmap);
            mViewSwitcher.showPrevious();
        }
    }

    private int getRandomIndex() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(mFileList.size());
    }

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

    private class PhotoDisplayTask extends TimerTask {
        @Override
        public void run() {
            if (mFileList != null) {
                MNLog.i("Timer", "timer running...");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showNext();
                    }
                });
            }
            else {
                throw new IllegalStateException("mFileList CANNOT be null.");
            }
        }
    }
}
