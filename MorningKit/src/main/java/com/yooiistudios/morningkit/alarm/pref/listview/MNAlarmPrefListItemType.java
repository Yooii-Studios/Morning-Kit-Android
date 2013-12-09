package com.yooiistudios.morningkit.alarm.pref.listview;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 7.
 *
 * MNAlarmPrefListItemType
 *  순서의 수정의 용이성을 위해 enum으로 선언
 */
public enum MNAlarmPrefListItemType {
    REPEAT, LABEL, SOUND_TYPE, SOUND_NAME, SNOOZE;

    // REPEAT(0)과 같은 방식을 현재로서는 사용하지 않을 것 같음
//    private final int index;

//    MNAlarmPrefListItemType(int index) {
//        this.index = index;
//    }
//    public int getIndex() { return index; }

    // 추후 수정의 용이성을 위해
    private static final int REPEAT_INDEX = 0;
    private static final int LABEL_INDEX = 1;
    private static final int SOUND_TYPE_INDEX = 2;
    private static final int SOUND_NAME_INDEX = 3;
    private static final int SNOOZE_INDEX = 4;

    private MNAlarmPrefListItemType() {}
    public static MNAlarmPrefListItemType valueOf(int index) {

        switch (index) {
            case REPEAT_INDEX: return REPEAT;
            case LABEL_INDEX: return LABEL;
            case SOUND_TYPE_INDEX: return SOUND_TYPE;
            case SOUND_NAME_INDEX: return SOUND_NAME;
            case SNOOZE_INDEX: return SNOOZE;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}

