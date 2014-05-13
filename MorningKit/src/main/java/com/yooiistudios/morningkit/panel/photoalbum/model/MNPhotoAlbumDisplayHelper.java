package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapProcessor;
import com.yooiistudios.morningkit.common.bitmap.MNBitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 10.
 */
public class MNPhotoAlbumDisplayHelper {
    private Activity mActivity;
    private ViewSwitcher mViewSwitcher;
    private ArrayList<File> mFileList;
    private ImageView mFirstView;
    private ImageView mSecondView;
    private Timer mTimer;
    private MNPhotoAlbumTransitionType mTransitionType;
    private long mInterval;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private int mPhotoIdx;

    public MNPhotoAlbumDisplayHelper(Activity activity, ViewSwitcher viewSwitcher,
                              ArrayList<File> fileList,
                              MNPhotoAlbumTransitionType transitionType,
                              long interval, int photoWidth, int photoHeight) {
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
                if (!(child instanceof ImageView)) {
                    throw new IllegalStateException(viewSwitcher.toString()
                            + " MUST HAVE ImageView child ONLY.");
                }
            }
        }
        mActivity = activity;
        mViewSwitcher = viewSwitcher;
        mFirstView = (ImageView)(mViewSwitcher.getChildAt(0));
        mSecondView = (ImageView)(mViewSwitcher.getChildAt(1));
        mPhotoWidth = photoWidth;
        mPhotoHeight = photoHeight;

        mFileList = fileList;
        mTransitionType = transitionType;
        mInterval = interval;
    }
    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        MNBitmapUtils.recycleImageView(mFirstView);
        MNBitmapUtils.recycleImageView(mSecondView);
    }

    public void start() {
        mFileList = MNPhotoAlbumFileManager.getValidImageFileList(
                mFileList);
//            mFileList = mSetting.getFileList();
        if (mFileList.size() == 0) {
            //TODO no images to display. show error message.
        }
        else {
            mPhotoIdx = 0;

            //show first image
            Bitmap bitmap = getPolishedBitmap(mFileList.get(mPhotoIdx),
                    mPhotoWidth, mPhotoHeight);
//                Bitmap bitmap = BitmapManager.createScaledBitmap(mActivity
//                                .getApplicationContext(),
//                        mFileList.get(mPhotoIdx),
//                        mPhotoWidth, mPhotoHeight, true);
            mFirstView.setImageBitmap(bitmap);

            //init view switcher
            mViewSwitcher.setDisplayedChild(0);
            Animation[] animArr = MNPhotoAlbumTransitionFactory.
                    getTransitionAnimation(mTransitionType);
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
        }
    }
    private Bitmap getPolishedBitmap(File file, int width, int height) {
        Bitmap bitmap = MNBitmapProcessor.createSampleSizedBitmap(
                file, width, height);
        Bitmap croppedBitmap = MNBitmapProcessor.
                getCroppedBitmap(bitmap, width, height);
        Bitmap polishedBitmap = MNBitmapProcessor.
                getRoundedCornerBitmap(croppedBitmap, width, height,
                        false,
                        (int) mActivity.getResources()
                                .getDimension(
                                        R.dimen.panel_flickr_round_radius));
        croppedBitmap.recycle();

        return polishedBitmap;
    }
    private void showNext() {
        mPhotoIdx++;

        if (mPhotoIdx == mFileList.size()) {
            mPhotoIdx = 0;
        }

        int curViewIdx = mViewSwitcher.getDisplayedChild();

        Bitmap bitmap = getPolishedBitmap(mFileList.get(mPhotoIdx),
                mPhotoWidth, mPhotoHeight);

        if (bitmap == null) {
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
