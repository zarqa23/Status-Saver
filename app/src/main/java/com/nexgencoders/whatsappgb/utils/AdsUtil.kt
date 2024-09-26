package com.nexgencoders.whatsappgb.utils

import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.nexgencoders.whatsappgb.ui.ads.mynative.TemplateView


fun loadBannerAds(
    context: Context,
    adsFrameLayout: FrameLayout,
    adSize: AdSize,
    adUnitIdL: Int
) {
    val adView = AdView(context).apply {
        setAdSize(adSize)
        adUnitId = context.getString(adUnitIdL)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 0)
        }
    }

    adsFrameLayout.addView(adView)

    val adRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)

    adView.adListener = object : AdListener() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            super.onAdFailedToLoad(adError)
            adsFrameLayout.gone() // Custom method to hide the layout
            adView.loadAd(adRequest)
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            adsFrameLayout.visible()
        }
    }
}


fun loadSmallMediumSizeNativeAds(context: Context,adUnitIdL: Int,templateView: TemplateView){
    val adLoader = AdLoader.Builder(context,context.getString(adUnitIdL))
        .forNativeAd { nativeAd ->
            templateView.visible()
            templateView.setNativeAd(nativeAd)
        }.withAdListener(object :AdListener(){
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                templateView.gone()
            }
        }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}