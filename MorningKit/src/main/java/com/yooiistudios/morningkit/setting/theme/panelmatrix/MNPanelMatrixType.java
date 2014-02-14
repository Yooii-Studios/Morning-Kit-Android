package com.yooiistudios.morningkit.setting.theme.panelmatrix;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2013. 12. 4.
 *
 * MNPanelMatrixType
 *  모닝키트의 패널 매트릭스를 enum으로 표현
 *  index = 설정 창에서 순서를 표현
 *  uniqueId = 이 테마의 고유 id를 표시
 */
public enum MNPanelMatrixType {
    PANEL_MATRIX_2X2(0, 0), PANEL_MATRIX_2X1(1, 1);

    @Getter private final int index; // 리스트뷰에 표시할 용도의 index
    @Getter private final int uniqueId; // SharedPreferences에 저장될 용도의 unique id

    MNPanelMatrixType(int index, int uniqueId) {
        this.index = index;
        this.uniqueId = uniqueId;
    }

    public static MNPanelMatrixType valueOf(int index) {

        switch (index) {
            case 0: return PANEL_MATRIX_2X2;
            case 1: return PANEL_MATRIX_2X1;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }

    public static MNPanelMatrixType valueOfUniqueId(int uniqueId) {

        switch (uniqueId) {
            case 0: return PANEL_MATRIX_2X2;
            case 1: return PANEL_MATRIX_2X1;
            default: throw new IndexOutOfBoundsException("Undefined Enumeration Index");
        }
    }
}
