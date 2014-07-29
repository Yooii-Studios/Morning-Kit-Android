package com.yooiistudios.morningkit.common.log;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 16.
 *
 * MNFlurry
 *  플러리 관련된 변수를 선언
 *

 플러리 집계 데이터
 실행시:
 1. 풀버전/프리버전의 비율
 2. 사용중인 알람 갯수 분포
 3. 언어 사용 분표
 4. 패널 사용 분포

 설정 - 테마 탭
 1. 어느 테마를 고르는지
 2. 어느 언어를 선택하는지
 3. 어떤 패널 매트릭스를 선택하는지

 패널
 1. 패널 셀렉트 페이저(패널 세부설정 아래쪽)에서 패널을 바꾸는지, 세팅 - 패널 탭에서 패널을 바꾸는지 비율
 2. 달력 음력 사용/비사용 분표
 3. 날씨 현재위치/선택위치 사용 분표

 언락
 1. 어떤 락 아이템을 클릭해서 언락 화면을 부르는지
 2. 실제로 어떤 아이템을 언락하는지

 스토어
 1. 패널 셀렉트 페이저 두번째 페이지 / 도그 이어 / 설정-인포 탭 / 설정-상점 탭 중 어디서 상점을 부르는지 사용 분포

 *
 */
public class MNFlurry {
    public static final String KEY = "RJWQJ2639RQP9PH65WCR";
    public static final String CALLED_FROM = "Called From";

    // 메인
    public static final String ON_LAUNCH = "On Launch";

    // 풀버전 체크
    public static final String VERSION = "Free/Full Version";
    public static final String FULL_VERSION = "Full Version";
    public static final String FREE_VERSION = "Free Version";

    // 매트릭스 타입
    public static final String PANEL_MATRIX_TYPE = "Panel Matrix Type";

    // 언어 체크
    public static final String LANGUAGE = "Language";

    // 테마 체크
    public static final String THEME = "Theme";

    // 알람 갯수 체크
    public static final String NUM_OF_ALARMS = "Number of Alarms";

    // 패널
    public static final String PANEL = "Panel";
    public static final String PANEL_USAGE = "Panels";
    public static final String CHANGE_PANEL_FROM = "Change Panel From";

    public static final String WEATHER = "Weather";
    public static final String DATE = "Date";

    // 테마 안에서의 선택을 로그
    public static final String ON_SETTING_THEME = "On Setting - Theme";
    public static final String PANEL_MATRIX = "Panel Matrix";
    public static final String ALARM_STATUS_BAR_ICON_TYPE = "Alarm Status Bar Icon Type";

    // 어디서 상점이 불렸는지 로그
    public static final String STORE = "Store";

    // 어디서 언락이 불렸는지 로그
    public static final String UNLOCK = "Unlock";
    public static final String UNLOCKED_PRODUCT = "Unlocked Product";
}
