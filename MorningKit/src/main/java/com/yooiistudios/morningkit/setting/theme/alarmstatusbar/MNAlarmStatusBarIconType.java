package com.yooiistudios.morningkit.setting.theme.alarmstatusbar;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNPanelMatrixType
 *  모닝키트의 알람 아이콘 표시 여부를 설정
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNAlarmStatusBarIconType {
    ON(0, 0), OFF(1, 1);

    @Getter private final int index; // 리스트뷰에 표시할 용도의 index
    @Getter private final int uniqueId; // SharedPreferences에 저장될 용도의 unique id

    MNAlarmStatusBarIconType(int index, int uniqueId) {
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public static MNAlarmStatusBarIconType valueOf(int index) {

        switch (index) {
            case 0: return ON;
            case 1: return OFF;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static MNAlarmStatusBarIconType valueOfUniqueId(int uniqueId) {

        switch (uniqueId) {
            case 0: return ON;
            case 1: return OFF;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
