package com.yooiistudios.morningkit.alarm.listview.item;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 9.
 *
 * MNHorizontalScrollViewContainerType
 *  HorizontalScrollView 안 뷰의 좌/중/우의 상태를 나타내는 enumeration
 */
public enum MNHorizontalScrollViewContainerType {
    LEFT(0),
    MIDDLE(1),
    RIGHT(2);

    @Getter
    private final int index;
    MNHorizontalScrollViewContainerType(int index) { this.index = index; }


    @Override
    public String toString() {
        switch (index) {
            case 0:
                return "LEFT";
            case 1:
                return "MIDDEL";
            case 2:
                return "RIGHT";
        }
        return super.toString();
    }
}
