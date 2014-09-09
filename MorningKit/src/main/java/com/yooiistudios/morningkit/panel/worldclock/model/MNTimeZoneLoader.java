package com.yooiistudios.morningkit.panel.worldclock.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yooiistudios.morningkit.R;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_PREFS;
import static com.yooiistudios.morningkit.panel.worldclock.MNWorldClockPanelLayout.WORLD_CLOCK_PREFS_LATEST_TIME_ZONE;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 13.
 *
 * MNTimeZoneLoader
 */
public class MNTimeZoneLoader {
    private MNTimeZoneLoader() { throw new AssertionError("You MUST not create this class!"); }

    /**
     * 전체 시간대를 얻어오는 메서드
     */
    public static ArrayList<MNTimeZone> loadTimeZone(Context context) {
        ArrayList<MNTimeZone> cityList = new ArrayList<MNTimeZone>();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                context.getResources().openRawResource(R.raw.world_clock_city_list)));

        try {
            while (br.ready()) {
                String[] temp = br.readLine().split("\t");

                if(temp[1].contains("+"))
                {
                    temp[1] = temp[1].substring(1);
                }

                if(temp[2].contains("+"))
                {
                    temp[2] = temp[2].substring(1);
                }

                // Proiority 검색전 LocalizedNames 있는지 체크
                ArrayList<String> localizedNames = null;
                if (temp[3].contains(";")) {
                    String [] arrayForLocalizing = temp[3].split(";");
                    //					Log.i(TAG, arrayForLocalizing[1]);
                    temp[3] = arrayForLocalizing[0];

                    localizedNames = new ArrayList<String>();

                    // 여러개 있다면 '/' 로 구분
                    if (arrayForLocalizing[1].contains("/")) {
                        //						Log.i(TAG, "multiple localized names");
                        String [] localizedStrings = arrayForLocalizing[1].split("/");
//                        for (int i = 0; i < localizedStrings.length; i++) {
//                            //							Log.i(TAG, localizedStrings[i]);
//                            localizedNames.add(localizedStrings[i]);
//                        }
                        Collections.addAll(localizedNames, localizedStrings);
                    }else{
                        //						Log.i(TAG, "single localized names");
                        localizedNames.add(arrayForLocalizing[1]);
                    }
                }

                // Priority 추가
                String[] timeZoneNameAndPriority = temp[3].split("/");
                int priority = 100;
                if (timeZoneNameAndPriority.length == 2) {
                    temp[3] = timeZoneNameAndPriority[0];
                    priority = Integer.valueOf(timeZoneNameAndPriority[1]);
                }

                MNTimeZone city = new MNTimeZone(temp[0], temp[3], Integer.parseInt(temp[1]),
                        Integer.parseInt(temp[2]), priority, localizedNames);

                cityList.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityList;
    }

    /**
     * 저장된 시간대 값이 없을 때 기본 값을 가져오는 메서드
     */
    public static MNTimeZone getDefaultZone(Context context) {
        // 첫 번째로 SharedPreferences 의 값을 찾는다
        SharedPreferences prefs = context.getSharedPreferences(WORLD_CLOCK_PREFS, MODE_PRIVATE);
        String latestTimeZoneJsonString = prefs.getString(WORLD_CLOCK_PREFS_LATEST_TIME_ZONE, null);

        // 있으면 그 값을, 없으면 언어에 기반해서 TimeZone을 만들어 반환
        if (latestTimeZoneJsonString != null) {
            Type type = new TypeToken<MNTimeZone>() {}.getType();
            return new Gson().fromJson(latestTimeZoneJsonString, type);
        } else {
            MNTimeZone timeZone = new MNTimeZone();
            MNLanguageType languageType = MNLanguage.getCurrentLanguageType(context);
            if (languageType == MNLanguageType.ENGLISH) {
                // Paris 1 0 Romance Standard Time/1
                timeZone.m_Name = "Paris";
                timeZone.m_Offset_Hour = 1;
                timeZone.m_Offset_Min = 0;
                timeZone.m_TimeZoneName = "Romance Standard Time";
                timeZone.m_priority = 1;
            } else if (languageType == MNLanguageType.GERMAN ||
                    languageType == MNLanguageType.FRENCH) {
                // New York, NY	-5	-00	Eastern Standard Time/1;뉴욕
                timeZone.m_Name = "New York";
                timeZone.m_Offset_Hour = -5;
                timeZone.m_Offset_Min = 0;
                timeZone.m_TimeZoneName = "Eastern Standard Time";
                timeZone.m_priority = 1;
            } else {
                // London 0 0 GMT Standard Time/4
                timeZone.m_Name = "London";
                timeZone.m_Offset_Hour = 0;
                timeZone.m_Offset_Min = 0;
                timeZone.m_TimeZoneName = "GMT Standard Time";
                timeZone.m_priority = 4;
            }
            return timeZone;
        }
    }
}
