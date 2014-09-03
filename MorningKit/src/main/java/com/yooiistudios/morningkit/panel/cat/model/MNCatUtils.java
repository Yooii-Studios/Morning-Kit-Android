package com.yooiistudios.morningkit.panel.cat.model;

import android.content.Context;

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
    private static final int NUM_OF_NOON_ANIM = 2;
    private static final int NUM_OF_EVENING_ANIM = 2;
    private static final int NUM_OF_NIGHT_ANIM = 2;

    // 최초 기획에 따라 시간별로 구분을 해 두었지만, 절대량이 부족하기에 일단 전체랜덤으로 하기로 이사님과 합의.
    // 대신 최근의 인덱스를 확인해서 이전 애니메이션과 같은 것이면 다른 것을 찾을 때 까지 랜덤으로 돌리기
    public static int getRandomCatAnimationResourceId(boolean isDebug) {

        Random randomGenerator = new Random();
        int hourOfDay = DateTime.now().getHourOfDay();

        // 개발 중엔 여러 시간대를 테스트해보자(24시간 중 랜덤 시각으로)
        if (isDebug) {
            hourOfDay = randomGenerator.nextInt(24);
        }

        // 시간에 따른 분류
        if (hourOfDay >= 6 && hourOfDay < 12) {
            // 오전 6시 ~ 오전 11시: Morning
            int randomIndex = randomGenerator.nextInt(NUM_OF_MORNING_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_morning_set_1;
                case 1:
                    return R.drawable.cat_animation_morning_set_2;
                default:
                    return R.drawable.cat_animation_morning_set_1;
            }
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            // 오전 12시 ~ 오후 5시: Noon
            int randomIndex = randomGenerator.nextInt(NUM_OF_NOON_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_noon_set_1;
                case 1:
                    return R.drawable.cat_animation_noon_set_2;
                default:
                    return R.drawable.cat_animation_noon_set_1;
            }
        } else if (hourOfDay >= 18 && hourOfDay < 24) {
            // 오후 6시 ~ 오후 11시: Evening
            int randomIndex = randomGenerator.nextInt(NUM_OF_EVENING_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_evening_set_1;
                case 1:
                    return R.drawable.cat_animation_evening_set_2;
                default:
                    return R.drawable.cat_animation_evening_set_1;
            }
        } else {
            // 오전 12시 ~ 오전 5시
            int randomIndex = randomGenerator.nextInt(NUM_OF_NIGHT_ANIM);
            switch (randomIndex) {
                case 0:
                    return R.drawable.cat_animation_night_set_1;
                case 1:
                    return R.drawable.cat_animation_night_set_2;
                default:
                    return R.drawable.cat_animation_night_set_1;
            }
        }
    }

    // -1일 경우는 최초 로딩, static final 로 미리 선언해놓고 사용하자
    public static MNHappyMessage getRandomHappyString(int previousIndex) {
        MNHappyMessage newHappyMessage = new MNHappyMessage();
        try {
            Random randomGenerator = new Random();
            int randomIndex = 0;
            // 기존의 인덱스가 존재한다면 다른 인덱스를 얻기 위해 최소 100번 돌리기
            if (previousIndex != -1) {
                for (int i = 0; i < 100; i++) {
                    randomIndex = randomGenerator.nextInt(MNHappyMessage.happyMessages.length);
                    if (randomIndex != previousIndex) {
                        break;
                    }
                }
            } else {
                randomIndex = randomGenerator.nextInt(MNHappyMessage.happyMessages.length);
            }

            newHappyMessage.previousIndex = randomIndex;
            newHappyMessage.happyMessageString = MNHappyMessage.happyMessages[randomIndex];

//            reader.close();
        } catch (Exception e) {
            e.printStackTrace();

            // 문제가 있을 경우 기본값을 넣어주기
            newHappyMessage.previousIndex = 1;
            newHappyMessage.happyMessageString = "Have a nice day!";
        }
        return newHappyMessage;
    }
}
