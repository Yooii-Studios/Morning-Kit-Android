package com.yooiistudios.morningkit.panel.quotes.model;

import android.content.Context;

import com.yooiistudios.morningkit.common.log.MNLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 10.
 *
 * MNQuotesLoader
 *  MNQuotes 리스트를 반환하는 유틸리티 클래스
 */
public class MNQuotesLoader {
    private static final String TAG = "MNQuotesLoader";
    private MNQuotesLoader() { throw new AssertionError("You MUST not create this class!"); }

    public static MNQuote getRandomQuote(Context context, MNQuotesLanguage language) {
        MNQuote quote = null;
        try {
            InputStream file;
            file = context.getResources().openRawResource(language.rawDataFileId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UNICODE"));
            char[] tC = new char[file.available()];
            reader.read(tC);

            String buffer = new String(tC);
            String[] lines = buffer.split("\n");

            Random randomGenerator = new Random();
            int randomIndex = randomGenerator.nextInt(lines.length);

            String searchResult[] = lines[randomIndex].split("\t", 3);

            // 결과에 index 도 추가
            if (searchResult.length == 1) {
                MNLog.e(TAG, "loading error: " + language.toString() + ": " + randomIndex);
                quote = MNQuote.newInstance(searchResult[0], "");
            } else {
                quote = MNQuote.newInstance(searchResult[0], searchResult[1]);
            }
//            result[0] = searchResult[0];
//            result[1] = searchResult[1];
//            result[2] = String.format("%d", index);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quote;
    }

    /*
    public static void testAllQuotes(Context context, MNQuotesLanguage language) {
        try {
            InputStream file;
            file = context.getResources().openRawResource(language.rawDataFileId);

            BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UNICODE"));
            char[] tC = new char[file.available()];
            reader.read(tC);

            String buffer = new String(tC);
            String[] lines = buffer.split("\n");

            for (int i = 0; i < lines.length; i++) {
                String searchResult[] = lines[i].split("\t", 3);
                if (searchResult.length == 1) {
                    MNLog.e(TAG, "loading error: " + language.toString() + ": " + i);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
