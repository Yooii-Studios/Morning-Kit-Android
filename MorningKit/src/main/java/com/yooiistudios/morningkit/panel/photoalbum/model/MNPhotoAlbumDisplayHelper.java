package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.app.Activity;
import android.widget.ViewSwitcher;

import com.stevenkim.waterlily.bitmapfun.ui.RecyclingImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 10.
 */
public class MNPhotoAlbumDisplayHelper {
    private Activity mActivity;
    private ViewSwitcher mViewSwitcher;
    private ArrayList<File> mFileList;
    private RecyclingImageView mFirstView;
    private RecyclingImageView mSecondView;
    private Timer mTimer;
    private int mPhotoWidth;
    private int mPhotoHeight;
    private int mPhotoIdx;

//    public PhotoDisplayHelper(Activity activity, ViewSwitcher viewSwitcher,
//                              PhotoSetting setting, int photoWidth,
//                              int photoHeight) {
//        if (viewSwitcher == null) {
//            throw new IllegalArgumentException(viewSwitcher.toString() + " " +
//                    "CANNOT be null");
//        }
//        int viewSwitcherChildCount = viewSwitcher.getChildCount();
//        if (viewSwitcherChildCount != 2) {
//            throw new IllegalArgumentException(viewSwitcher.toString() + " " +
//                    "MUST HAVE 2 child exactly.");
//        }
//        else {
//            for (int i = 0; i < viewSwitcherChildCount; i++ ) {
//                View child = viewSwitcher.getChildAt(i);
//                if (!(child instanceof RecyclingImageView)) {
//                    throw new IllegalStateException(viewSwitcher.toString()
//                            + " MUST HAVE ImageView child ONLY.");
//                }
//            }
//        }
//        mActivity = activity;
//        mViewSwitcher = viewSwitcher;
//        mFirstView = (RecyclingImageView)(mViewSwitcher.getChildAt(0));
//        mSecondView = (RecyclingImageView)(mViewSwitcher.getChildAt(1));
//        mPhotoWidth = photoWidth;
//        mPhotoHeight = photoHeight;
//    }
//    public void stop() {
//        if (mTimer != null) {
//            mTimer.cancel();
//        }
//        mFileList = null;
//    }
//
//    public void start() {
//        try {
//            mFileList = BitmapManager.getValidImageFileList(
//                    mSetting.getFileList());
////            mFileList = mSetting.getFileList();
//            if (mFileList.size() == 0) {
//                //TODO no images to display. show error message.
//            }
//            else {
//                mPhotoIdx = 0;
//
//                //show first image
//                Bitmap bitmap = BitmapManager.createScaledBitmap(mActivity
//                                .getApplicationContext(),
//                        mFileList.get(mPhotoIdx),
//                        mPhotoWidth, mPhotoHeight, true);
//                mFirstView.setImageDrawable(new RecyclingBitmapDrawable(
//                        mActivity.getResources(), bitmap));
//
//                //init view switcher
//                mViewSwitcher.setDisplayedChild(0);
//                Animation[] animArr = PhotoTransitionFactory.
//                        getTransitionAnimation(mSetting.getTransitionType());
//                mViewSwitcher.setInAnimation(animArr[0]);
//                mViewSwitcher.setOutAnimation(animArr[1]);
//
//                long interval = mSetting.getIntervalInMillisec();
//                if (mFileList.size() > 1 && interval > 0) {
//                    //prepare for timer
//                    mTimer = new Timer();
//                    mTimer.schedule(new PhotoDisplayTask(), interval, interval);
//                }
//            }
//        } catch(JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    private void showNext() {
//        mPhotoIdx++;
//
//        if (mPhotoIdx == mFileList.size()) {
//            mPhotoIdx = 0;
//        }
//
//        int curViewIdx = mViewSwitcher.getDisplayedChild();
//
//        Bitmap bitmap = BitmapManager.createScaledBitmap(mActivity
//                        .getApplicationContext(),
//                mFileList.get(mPhotoIdx),
//                mPhotoWidth, mPhotoHeight, true);
//
//        if (bitmap == null) {
//            showNext();
//            return;
//        }
//
//        if (curViewIdx%2 ==  0) {
//            mSecondView.setImageDrawable(new RecyclingBitmapDrawable(
//                    mActivity.getResources(), bitmap));
//            mViewSwitcher.showNext();
//        } else {
//            mFirstView.setImageDrawable(new RecyclingBitmapDrawable(
//                    mActivity.getResources(), bitmap));
//            mViewSwitcher.showPrevious();
//        }
//    }
//
//    private class PhotoDisplayTask extends TimerTask {
//        @Override
//        public void run() {
//            if (mFileList != null) {
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showNext();
//                    }
//                });
//            }
//            else {
//                throw new IllegalStateException("mFileList CANNOT be null.");
//            }
//        }
//    }
}
