package com.yooiistudios.morningkit.panel.exchangerates.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;

import com.yooiistudios.morningkit.R;

public class MNCurrencyInfo {

	public String currencyCode;		// 통화 코드
	public String currencyName;		// 통화 이름
	public String usingCountryCode;		// 주 사용 나라 
	public String currencySymbol;

	private MNCurrencyInfo(String _currencyCode, String _currencyName,
			String _usingCountryCode, String _currencySymbol) 
	{
		currencyCode = _currencyCode;
		currencyName = _currencyName;
		usingCountryCode = _usingCountryCode;
		currencySymbol = _currencySymbol;
	}
	// 오스트레일리아 달러, 캐나다 달러, 중국 위안, 유로, 영국 파운드, 일본 엔, 한국 원, 미국 달러
	public static final MNCurrencyInfo frequentCurrency[] =
		{
		new MNCurrencyInfo("USD", "US Dollar", "US", "$"),
		new MNCurrencyInfo("CAD", "Canadian Dollar", "CA", "$"),
		new MNCurrencyInfo("EUR", "Euro", "EU", "€"),
		new MNCurrencyInfo("GBP", "British Pound", "GB", "£"),
		new MNCurrencyInfo("CNY", "Chinese Yuan", "CN", "¥"),
		new MNCurrencyInfo("JPY", "Japanese Yen", "JP", "¥"),
		new MNCurrencyInfo("KRW", "Korean Won", "KR", "₩"),
		new MNCurrencyInfo("AUD", "Australian Dollar", "AU", "$"),
		};
	
	
	public static ArrayList<MNCurrencyInfo> allCurrency;
	public static void loadAllCurrency(Context _context)
	{// read from file
		if( allCurrency == null )
		{
			allCurrency = new ArrayList<MNCurrencyInfo>();
			try {

				InputStream file = _context.getResources().openRawResource(R.raw.exchange_list);

				byte[] tB = new byte[file.available()];
				file.read(tB);

				String buffer = new String(tB);

				String[] lines = buffer.split("\r\n");

				
				for(int i=0; i<lines.length; ++i)
				{
					try
					{
						String[] splittedStrings = lines[i].split("/", 4);

						allCurrency.add(new MNCurrencyInfo(splittedStrings[0], 
								splittedStrings[1], 
								splittedStrings[2],
								splittedStrings[3]));
					}
					catch(IndexOutOfBoundsException e)
					{
						String[] splittedStrings = lines[i].split("/", 3);
						allCurrency.add(new MNCurrencyInfo(splittedStrings[0], 
								splittedStrings[1],"Common Currency", splittedStrings[0]));
					}

				}

			}
			catch (FileNotFoundException e) {}
			catch (IOException e) {}
		}
	}
	
	public static MNCurrencyInfo getCurrencyInfo(String _currencyCode)
	{
		for(int i=0; i<frequentCurrency.length; ++i)
		{
			if( frequentCurrency[i].currencyCode.compareToIgnoreCase(_currencyCode) == 0 )
				return frequentCurrency[i];
		}
		
		

		for(int i=0; i<allCurrency.size(); ++i)
		{
			if( allCurrency.get(i).currencyCode.compareToIgnoreCase(_currencyCode) == 0 )
				return allCurrency.get(i);
		}
		return frequentCurrency[0];
	}


}
