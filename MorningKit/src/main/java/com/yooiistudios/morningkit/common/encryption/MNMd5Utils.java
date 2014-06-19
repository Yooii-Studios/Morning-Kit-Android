package com.yooiistudios.morningkit.common.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 19.
 *
 * MNMd5Utils
 *  스트링을 md5 해시값으로 반환해주는 유틸리티 클래스
 */
public class MNMd5Utils {
    private MNMd5Utils() { throw new AssertionError("You MUST not create this class!"); }

    public static String getMd5String(String string) {
        String md5String;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            md5String = sb.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            md5String = null;
        }
        return md5String;
    }
}
