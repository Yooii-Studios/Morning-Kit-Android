package com.yooiistudios.morningkit.panel.exchangerates;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 4.
 *
 * MNExchangeRatesSelectDialog
 *  디테일 프래그먼트에서 국가를 선택할 때 사용하는 다이얼로그
 */
public class MNExchangeRatesSelectDialog extends AlertDialog {
    protected MNExchangeRatesSelectDialog(Context context) {
        super(context);
        init();
    }

    protected MNExchangeRatesSelectDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected MNExchangeRatesSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        // 커스텀 뷰를 커스터마이징
    }
}
