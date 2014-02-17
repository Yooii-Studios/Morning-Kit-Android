package com.yooiistudios.morningkit.common.utf;

import java.net.URLEncoder;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 2. 17.
 *
 * MNUTF
 *  url 커넥션 시 필요한 메서드들을 제공
 */
public class MNUtf {
    public static String getConverted_UTF_8_String(String url) {
        String convert = null;

        try {
            convert = URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convert;
    }
}
