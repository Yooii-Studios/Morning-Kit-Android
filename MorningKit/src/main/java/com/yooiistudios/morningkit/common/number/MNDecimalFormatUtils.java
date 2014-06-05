package com.yooiistudios.morningkit.common.number;

import java.text.DecimalFormat;

/**
 * Created by Wooseong Kim in MorningKit from Yooii Studios Co., LTD. on 2014. 6. 5.
 *
 * MNDecimalFormatUtils
 *  돈 스트링을 세자리씩 콤마로 끊어 주는 유틸리티 클래스
 */
public class MNDecimalFormatUtils {
    private MNDecimalFormatUtils() { throw new AssertionError("You MUST not create this class!"); }
    public static String makeStringComma(String priceString) {
        if (priceString == null || priceString.length() == 0)
            return "";
        long value = Long.parseLong(priceString);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
}
