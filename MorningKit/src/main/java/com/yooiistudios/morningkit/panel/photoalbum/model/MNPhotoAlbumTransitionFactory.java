package com.yooiistudios.morningkit.panel.photoalbum.model;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Dongheyon Jeong on in My Application 3 from Yooii Studios Co., LTD. on 2014. 5. 12.
 */
public class MNPhotoAlbumTransitionFactory {
    /**
     * Make a new instance of animation for specified PhotoTransitionType.
     * @param type of transition.
     * @return array of animation.
     * Index 0 contains inAnimation.
     * Index 1 contains outAnimation.
     */
    public static Animation[] makeTransitionAnimation(
            MNPhotoAlbumTransitionType type) {
        Animation[] animArr = new Animation[2];
        int duration = type.getDurationInMillisec();
        if (type.equals(MNPhotoAlbumTransitionType.ALPHA)) {
            AlphaAnimation inAnim = new AlphaAnimation(0.0f, 1.0f);
            inAnim.setDuration(duration);
            AlphaAnimation outAnim = new AlphaAnimation(1.0f, 0.0f);
            outAnim.setDuration(duration);

            animArr[0] = inAnim;
            animArr[1] = outAnim;
        }
        else {
            AlphaAnimation inAnim = new AlphaAnimation(0.0f, 1.0f);
            inAnim.setDuration(duration);
            AlphaAnimation outAnim = new AlphaAnimation(1.0f, 0.0f);
            outAnim.setDuration(duration);

            animArr[0] = inAnim;
            animArr[1] = outAnim;
        }
        return animArr;
    }
}
