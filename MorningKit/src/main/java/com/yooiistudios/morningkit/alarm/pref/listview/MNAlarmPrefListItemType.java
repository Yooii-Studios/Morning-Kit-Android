package com.yooiistudios.morningkit.alarm.pref.listview;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 7.
 *
 * MNAlarmPrefListItemType
 *  순서의 수정의 용이성을 위해 enum으로 선언
 */
public enum MNAlarmPrefListItemType {
    TIME, REPEAT, LABEL, SOUND, SNOOZE, VOLUME; // SOUND_NAME,

//    @Getter private final int index;
//    MNAlarmPrefListItemType(int index) {
//        this.index = index;
//    }

//    public static final int ALARM_PREF_LIST_ITEM_COUNT = 5;

    // 추후 수정의 용이성을 위해
    private static final int TIME_INDEX = 0;
    private static final int REPEAT_INDEX = 1;
    private static final int LABEL_INDEX = 2;
    private static final int SOUND_INDEX = 3;
//    private static final int SOUND_NAME_INDEX = 3;
    private static final int SNOOZE_INDEX = 4;
    private static final int VOLUME_INDEX = 5;

    /**
     * Methods
     */
    private MNAlarmPrefListItemType() {}

    public static MNAlarmPrefListItemType valueOf(int index) {

        switch (index) {
            case TIME_INDEX: return TIME;
            case REPEAT_INDEX: return REPEAT;
            case LABEL_INDEX: return LABEL;
            case SOUND_INDEX: return SOUND;
//            case SOUND_NAME_INDEX: return SOUND_NAME;
            case SNOOZE_INDEX: return SNOOZE;
            case VOLUME_INDEX: return VOLUME;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
    /*
    public static int indexOf(MNAlarmPrefListItemType alarmPrefListItemType) {

        switch (alarmPrefListItemType) {
            case REPEAT: return REPEAT_INDEX;
            case LABEL: return LABEL_INDEX;
            case SOUND_TYPE: return SOUND_TYPE_INDEX;
            case SOUND_NAME: return SOUND_NAME_INDEX;
            case SNOOZE: return SNOOZE_INDEX;
            case TIME: return TIME_INDEX;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
    */
}

