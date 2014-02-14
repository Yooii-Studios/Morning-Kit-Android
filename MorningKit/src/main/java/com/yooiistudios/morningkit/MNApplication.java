package com.yooiistudios.morningkit;

import android.app.Application;
import android.content.res.Configuration;

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

        Configuration config = getBaseContext().getResources().getConfiguration();

        // load language from MNLanguage
        MNLanguageType currentLanguageType = MNLanguage.getCurrentLanguageType(getBaseContext());

        // update locale to current language
        locale = new Locale(currentLanguageType.getCode(), currentLanguageType.getRegion());
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
