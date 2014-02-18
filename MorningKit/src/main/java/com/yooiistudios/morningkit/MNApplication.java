package com.yooiistudios.morningkit;

import android.app.Application;
import android.content.res.Configuration;

import com.testflightapp.lib.TestFlight;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.util.Locale;

/**
 * Created by StevenKim in Morning Kit from Yooii Studios Co., LTD. on 2014. 1. 15.
 *
 * MNApplication
 *  로케일 설정에 관련된 클래스. 모든 액티비티 설정에 관여한다
 */
public class MNApplication extends Application {
    private Locale locale = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        //Initialize TestFlight with your app token.
        TestFlight.takeOff(this, "07cee2c7-f05a-47e6-a953-678eab2a719e");

        Configuration config = getBaseContext().getResources().getConfiguration();

        // load language from MNLanguage
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getBaseContext());

        // update locale to current language
        locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // https://gist.github.com/benelog/5954649
        // Activity나 Application 등 UI스레드 아래와 같이 AsyncTask를 한번 호출합니다.
        // 메인스레드에서 단순히 클래스 로딩을 한번만 해도 AsyncTask내의 static 멤버 변수가 정상적으로 초기화됩니다.
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
