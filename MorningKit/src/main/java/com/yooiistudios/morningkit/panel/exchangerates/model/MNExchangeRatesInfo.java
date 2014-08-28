package com.yooiistudios.morningkit.panel.exchangerates.model;

import android.content.Context;

import com.yooiistudios.morningkit.setting.theme.language.MNLanguage;
import com.yooiistudios.morningkit.setting.theme.language.MNLanguageType;

import java.text.NumberFormat;


public class MNExchangeRatesInfo {
	public final MNCurrencyInfo baseCurrencyInfo;
	public final MNCurrencyInfo targetCurrencyInfo;

	// 아래는 환율 정보. 기준은 base -> target 순서를 기준으로 함
	// USD 1이 KRW로 1000 일때, exchangeRate = 1000 이다.
	// 즉, targetCurrencyMoney = baseCurrencyMoney * exchangeRate
	private double exchangeRate;				// 비율
	private double baseCurrencyMoney;			// 금액		baseCountry의 금액

	public MNExchangeRatesInfo(String _baseCurrencyCode, String _targetCurrencyCode) {
		baseCurrencyInfo = MNCurrencyInfo.getCurrencyInfo(_baseCurrencyCode);
		targetCurrencyInfo = MNCurrencyInfo.getCurrencyInfo(_targetCurrencyCode);

		exchangeRate = 1;
		baseCurrencyMoney = 1;
	}

	public void setBaseCurrencyMoney(double _baseCurrencymoney)	{ baseCurrencyMoney = _baseCurrencymoney; }
	public double getBaseCurrencyMoney() { return baseCurrencyMoney; }

	public double getTargetCurrencyMoney() { return baseCurrencyMoney * exchangeRate; }
//	public double getBaseCurrencyMoney(double _targetMoney) { return _targetMoney/exchangeRate; }
//	public double getTargetCurrencyMoney(double _baseMoney) { return _baseMoney*exchangeRate; }

	public void setExchangeRate( double _rate) {
		if( _rate != 0 )
			exchangeRate = _rate;
		else
			exchangeRate = 1;
	}
	public double getExchangeRate() { return exchangeRate; }
	public String getBaseCountryCode() { return baseCurrencyInfo.usingCountryCode; }
	public String getTargetCountryCode() { return targetCurrencyInfo.usingCountryCode; }

	public String getBaseCurrencyCode() { return baseCurrencyInfo.currencyCode; }
	public String getTargetCurrencyCode() { return targetCurrencyInfo.currencyCode; }

	public String getBaseCurrencySymbol() { return baseCurrencyInfo.currencySymbol; }
	public String getTargetCurrencySymbol() { return targetCurrencyInfo.currencySymbol; }

	public MNExchangeRatesInfo getReverseExchangeInfo()	{
		MNExchangeRatesInfo reverseExchangeInfo =
				new MNExchangeRatesInfo(targetCurrencyInfo.currencyCode, baseCurrencyInfo.currencyCode);
		reverseExchangeInfo.setBaseCurrencyMoney(baseCurrencyMoney);
			reverseExchangeInfo.setExchangeRate(1/exchangeRate);

		return reverseExchangeInfo;
	}

	static public String getMoneyString(double _money, Context context) {
        String result;

        // 러시아어, 스페인어, 프랑스어일 경우 값이 이상하게 나온다. 예외처리가 필요
        if (MNLanguage.getCurrentLanguageType(context) == MNLanguageType.RUSSIAN ||
                MNLanguage.getCurrentLanguageType(context) == MNLanguageType.FRENCH ||
                MNLanguage.getCurrentLanguageType(context) == MNLanguageType.SPANISH) {
            result = String.valueOf(_money);
        } else {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            result = format.format(_money);
        }

        // 이 때 result의 형식은 현재 폰의 로케일에 따라 "$100.00" 같은 형태로 나타남
        // 앞에 붙는 통화 기호는 맘대로 설정 불가능
        // 따라서 첨삭
        int numberStart = 0;
        for (int i = 0; i < result.length(); ++i) {
            if ('0' <= result.charAt(i) && result.charAt(i) <= '9') {
                numberStart = i;
                break;
            }
        }
        result = result.substring(numberStart, result.length());

        // by 용빈
//		Log.i(TAG, "result: " + result);
        if (result.endsWith("0")) {
            result = result.substring(0, result.length() - 1);
        }

        if (_money / (long) _money == 1) {
            if (result.contains(".")) {
                result = result.substring(0, result.indexOf('.'));
            }
        }

		/*
        // 소수점 아래가 다 0이면 소수점 제거
		if( _money > 1 && _money - (int)_money/1 == 0 )
		{
			if (result.indexOf('.') != -1) {
				result = result.substring(0, result.indexOf('.'));
			}
		}
		*/

        // 추가: 숫자가 .00 으로 끝날 경우 소수점 보여주지 말기 by 우성(Review Report V2.6)
        // 위에 용섭이가 만들어 놨는데 제대로 작동 안하는듯
        // 용빈 comments - 스트링에서 '.'은 단순기호 .이 아니라 정규식에서의 와일드카드를 의미하는 .으로 읽힘
        // 현재 버그의 원인
//		if (result.endsWith(".00")) {
//			String [] splitStrings = result.split(".00");
//			result = splitStrings[0]; 
////			result += "    ";	// .00 이 사라진 공백 메꾸기용 공간. 넣어봤는데 있는게 더 별로다. 
//		}
        return result;
    }

	static public double getDoubleMoney(String _moneyString) {
		double result;

		_moneyString = _moneyString.replace(',', ' ');
		_moneyString = _moneyString.replaceAll(" ", "");
		
		result = Double.parseDouble(_moneyString);
		
		return result;
	}
}















