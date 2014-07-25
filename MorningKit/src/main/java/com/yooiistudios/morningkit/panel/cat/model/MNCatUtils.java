package com.yooiistudios.morningkit.panel.cat.model;

import com.yooiistudios.morningkit.R;

import org.joda.time.DateTime;

import java.util.Random;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 7. 23.
 *
 * MNCatUtils
 *  시간대에 맞게 고양이 애니메이션 리소스 id를 제공함
 */
public class MNCatUtils {
    private static final int NUM_OF_MORNING_ANIM = 2;
    private static final int NUM_OF_NOON_ANIM = 4;
    private static final int NUM_OF_EVENING_ANIM = 2;

    public static int getRandomCatAnimationResourceId(boolean isDebug) {

        Random randomGenerator = new Random();
        int hourOfDay = DateTime.now().getHourOfDay();

        // 개발 중엔 여러 시간대를 테스트해보자(24시간 중 랜덤 시각으로)
        if (isDebug) {
            hourOfDay = randomGenerator.nextInt(24);
        }

        // 시간에 따른 분류
        if (hourOfDay >= 4 && hourOfDay < 11) {
            // 오전 4시 ~ 오전 11시
            int randomIndex = randomGenerator.nextInt(NUM_OF_MORNING_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_morning_set_1;
                case 1:
                    return R.drawable.cat_animation_morning_set_2;
                default:
                    return R.drawable.cat_animation_morning_set_1;
            }
        } else if (hourOfDay >= 11 && hourOfDay < 19) {
            // 오전 11시 ~ 오후 7시
            int randomIndex = randomGenerator.nextInt(NUM_OF_NOON_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_noon_set_1;
                case 1:
                    return R.drawable.cat_animation_noon_set_2;
                case 2:
                    return R.drawable.cat_animation_noon_set_3;
                case 3:
                    return R.drawable.cat_animation_noon_set_4;
                default:
                    return R.drawable.cat_animation_noon_set_1;
            }
        } else {
            // 오후 7시 ~ 오전 4시
            int randomIndex = randomGenerator.nextInt(NUM_OF_EVENING_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_evening_set_1;
                case 1:
                    return R.drawable.cat_animation_evening_set_2;
                default:
                    return R.drawable.cat_animation_evening_set_1;
            }
        }
    }
}
