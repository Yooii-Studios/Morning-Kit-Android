package com.yooiistudios.morningkit.panel.worldclock.model;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 13.
 *
 * MNTimeZoneLoader
 */
public class MNTimeZoneLoader {
    private MNTimeZoneLoader() { throw new AssertionError("You MUST not create this class!"); }

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
}
