package com.yooiistudios.morningkit;

/**
 * Created by yongbinbae on 13. 10. 23..
 */

public final class MN {

    public static final String FLURRY_KEY = "CSHGTN3FNSZBGMFMMX4N";
    public static final String FLICKR_API_KEY = "ccc5c75e5380273b78d246a71353fab9";

    public static final class main {
        public static final String APP_TAG = "YOOII_MORNING KIT";
    }

    public static final class alarm {

        public static final String ALARM_ID = "alarmId";

        // Local Data Paths
        public static final String SHARED_PREFS_FILE = "alarm_data2";
        public static final String ALARM_LIST = "alarm_list2";
        public static final String ALARM_POSITION_TO_MODIFY = "AlarmPositionToModify";

        // Preferences
        public static final String ALARM_PREFERENCE_ALARM_ID = "ALARM_PREFERENCE_ALARM_ID";

        // Sound SourcePath and Name for save
        public static final String ALARM_SOUND_SOURCE_TYPE = "alarm_sound_source_type3";
        public static final String ALARM_SOUND_SOURCE_PATH = "alarm_sound_source_path2";
        public static final String ALARM_SOUND_SOURCE_NAME = "alarm_sound_source_name1";
        public static final String ALARM_SOUND_SOURCE_RAW_INT = "alarm_sound_source_raw_int";

        // Sound Type
        public static final int ALARM_SOUNDTYPE_NONE = 0;
        public static final int ALARM_SOUNDTYPE_RINGTONE = 1;
        public static final int ALARM_SOUNDTYPE_MUSIC = 2;
        public static final int ALARM_SOUNDTYPE_APP_MUSIC = 3;

        public static final int MUSIC_ACT = 100;

        public static final int ALARM_MODE_ADD = 200;
        public static final int ALARM_MODE_MODIFY = 201;

        public static final int ALARM_HEIGHT_TABLET_DP = 103;
        public static final int ALARM_HEIGHT_PHONE_DP = 69;

        public static final int SWIPE_MIN_DISTANCE = 120;
        public static final int SWIPE_MAX_OFF_PATH = 250;
        public static final int SWIPE_THRESHOLD_VELOCITY = 200;
    }

    public final static class widget {
        public final static class position {
            public final static int LT = 0;
            public final static int RT = 1;
            public final static int LB = 2;
            public final static int RB = 3;
        }

        public final static float LAND_SCALE = 1.25f;

        public final static class flickr {
            public final static String FLICKR_DEFAULT_KEYWORD = "Morning";
            public final static String FLICKR_LATEST_KEYWORD = "FLICKR_LATEST_KEYWORD";
            public final static String FLICKR_SHARED_PREFS = "FLICKR_SHARED_PREFS";
            public final static String FLICKR_IS_FIRST_LOADING = "FLICKR_IS_FIRST_LOADING";
        }

        public final static int NUM_OF_WIDGET = 8;

        public final static int WEATHER = 0;
        public final static int CALENDAR = 1;
        public final static int DATE_COUNTDOWN = 2;
        public final static int WORLD_CLOCK = 3;
        public final static int QUOTES = 4;
        public final static int FLICKR = 5;
        public final static int MEMO = 6;
        public final static int EXCHANGE_RATE = 7;

        // public final static double WIDGET_HEIGHT_RATIO = 5.125;
        public final static double WIDGET_HEIGHT_RATIO = 5.355648;
        public final static int WIDGET_HEIGHT = 240;
        public final static int WIDGET_HEIGHT_TABLET_DP = 200; // 263 에서 4 증가 후
        // 나누기 1.3333
        public final static int WIDGET_HEIGHT_PHONE_DP = 122; // 240 에서 4 증가 후
        // 나누기 2

//		public final static int getWidgetID(int viewID) {
//			switch (viewID) {
//			case R.id.tv_worldclock:
//				return WORLD_CLOCK;
//			case R.id.tv_weather:
//				return WEATHER;
//			case R.id.tv_memo:
//				return MEMO;
//			case R.id.tv_flickr:
//				return FLICKR;
//			case R.id.tv_datecalculator:
//				return DATE_COUNTDOWN;
//			case R.id.tv_calendar:
//				return CALENDAR;
//			case R.id.tv_quotes:
//				return QUOTES;
//			case R.id.tv_exchangerate:
//				return EXCHANGE_RATE;
//
//			default:
//				return -1;
//			}
//		}
    }

    public final static class ads {
        public final static String ADMOB_ID = "a14fc9baecf16e9";
//        public final static String ADMOB_ID = "a15278abca8d8ec";
    }

    public final static class generalSetting {
        public final static class BackGroundColor {
            public final static int DEFAULT = 0;
            public final static int WHITE = 1;
            public final static int LIGHT_GRAY = 2;
            public final static int GRAY = 3;
            public final static int BLACK = 4;
            public final static int RED = 5;
            public final static int ORANGE = 6;
            public final static int YELLOW = 7;
            public final static int GREEN = 8;
            public final static int BLUE = 9;
            public final static int INDIGO = 10;
            public final static int PURPLE = 11;
        }

        public final static class MainFontColor {
            public final static int WHITE = 0;
            public final static int GRAY = 1;
            public final static int BLACK = 2;
            public final static int RED = 3;
            public final static int BLUE = 4;
            public final static int YELLOW = 5;
            public final static int GREEN = 6;
            public final static int PINK = 7;

        }

        public final static class SubFontColor {
            public final static int WHITE = 0;
            public final static int GRAY = 1;
            public final static int BLACK = 2;
            public final static int RED = 3;
            public final static int BLUE = 4;
            public final static int YELLOW = 5;
            public final static int GREEN = 6;
            public final static int PINK = 7;
        }

        public final static class Language {

            public final static int Default = -1;
        }

        public final static class Theme {
            public final static String SHARED_PREFS_FILE = "Theme_SHARED_PREFS_FILE";
            public final static String CAMERA_FACING_INFO = "CAMERA_FACING_INFO";
            public final static int REQ_CODE_PICK_IMAGE_PORTRAIT = 0;
            public final static int REQ_CODE_PICK_IMAGE_LANDSCAPE = 1;
            public final static int REQ_CROP_FROM_CAMERA_PORTRAIT = 2;
            public final static int REQ_CROP_FROM_CAMERA_LANDSCAPE = 3;
        }
    }
}

