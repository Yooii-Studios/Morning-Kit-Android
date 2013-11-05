package com.yooii.morningkit.main.admob;

import com.google.ads.internal.AdWebView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowWebView;

/**
 * Created by StevenKim on 2013. 11. 5..
 */
@Implements(AdWebView.class)
public class AdWebViewShadow extends ShadowWebView {
    @Implementation
    public void __constructor__(com.google.ads.n slotState, com.google.ads.AdSize adSize) {
    }

}