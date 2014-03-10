package com.yooiistudios.morningkit.panel.quotes.model;

import lombok.Getter;

/**
 * Created by StevenKim in MorningKit from Yooii Studios Co., LTD. on 2014. 3. 10.
 *
 * MNQuotes
 *  명언 패널에 쓰일 모델
 */
public class MNQuote {
    @Getter String quote;
    @Getter String author;

    private MNQuote() {}
    public static MNQuote newInstance(String quote, String author) {
        MNQuote newQuote = new MNQuote();
        newQuote.quote = quote;
        newQuote.author = author;

        return newQuote;
    }
}
