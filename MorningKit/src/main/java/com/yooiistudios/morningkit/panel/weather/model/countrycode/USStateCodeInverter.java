package com.yooiistudios.morningkit.panel.weather.model.countrycode;

import android.content.Context;

import com.yooiistudios.morningkit.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class USStateCodeInverter {

	static Map<String, String> stateCodeMap = new HashMap<String, String>();

	static public String getStateNameOfCode(Context context, String _stateCode)
	{
		String result = "Unknown";

		// is map is empty, load countrycodes
		if(stateCodeMap.isEmpty()) {
			InputStream file;

			file = context.getResources().openRawResource(R.raw.statecode_us);

			byte[] tB;
			try {
				tB = new byte[file.available()];
				file.read(tB);

				String buffer = new String(tB);

				String[] lines = buffer.split("\n");

                for (String line : lines) {
                    try {
                        String[] words = line.split("/");
                        stateCodeMap.put(words[0], words[1]);
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

		if(stateCodeMap.get(_stateCode) != null) {
            result = stateCodeMap.get(_stateCode);
        }
		return result;
	}
}
