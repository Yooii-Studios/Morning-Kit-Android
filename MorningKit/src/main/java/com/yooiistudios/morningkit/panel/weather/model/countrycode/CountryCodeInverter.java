package com.yooiistudios.morningkit.panel.weather.model.countrycode;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CountryCodeInverter {

	static Map<String, String> countryCodeMap = new HashMap<String, String>();

	static public String getCountryNameOfCode(Context context, String _countryCode)	{
		String result = _countryCode;

		// is map is empty, load countrycodes
		if( countryCodeMap.isEmpty() )
		{
			InputStream file;

			file = context.getResources().openRawResource(R.raw.countrycode);

			byte[] tB;
			try {
				tB = new byte[file.available()];
				file.read(tB);

				String buffer = new String(tB);

				String[] lines = buffer.split("\r\n");

                for (String line : lines) {
                    try {
                        String[] words = line.split("/");
                        countryCodeMap.put(words[0], words[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        break;
                    }
                }
			} catch (IOException e) {

            } finally {
				try {
					file.close();
				} catch (IOException e) {}
			}
		}

		if( countryCodeMap.get(_countryCode) != null ) {
            result = countryCodeMap.get(_countryCode);
        }
		return result;
	}
}
