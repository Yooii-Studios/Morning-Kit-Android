package com.yooiistudios.morningkit.panel.weather.model.locationinfo;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 21.
 *
 * MNWeatherLocationInfo
 *  도시 검색 & 현재 위치 사용시 얻는 도시 정보
 */
public class MNWeatherLocationInfo {
    @Getter @Setter String name;
    String regionCode;
    String countryCode;
    String timezoneCode;
    @Getter @Setter double latitude;
    @Getter @Setter double longitude;

    // 추가: woeid
    // originalName(현재에서 사용되는 언어의 도시이름 ex:서울), otherNames 추가: originalName이 검색되면 이것을 보여 주고,
    // toherNames가 검색되면 englishName(name)을 보여 줌

    @Getter @Setter int woeid;
    @Getter @Setter String englishName;
    @Getter @Setter String originalName;
    ArrayList<String> otherNames;

    public MNWeatherLocationInfo() {
        this.otherNames = new ArrayList<String>();
    }

    public boolean startsWith(String searchString) {
        if (name.toLowerCase().replace(" ", "").startsWith(searchString.toLowerCase().replace(" ", ""))) {
            return true;
        }
        return false;
    }

    public boolean contains(String searchString) {
        if (name.toLowerCase().replace(" ", "").contains(searchString.toLowerCase().replace(" ", ""))) {
            return true;
        }
        return false;
    }
}
