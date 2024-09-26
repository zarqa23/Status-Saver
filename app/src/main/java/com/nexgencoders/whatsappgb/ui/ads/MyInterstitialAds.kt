package com.nexgencoders.whatsappgb.ui.ads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MyInterstitialAds {

    private var interstitialAds: InterstitialAd? = null
    private var isAdLoading = false

    fun preloadInterstitialAds(activity: Activity, adUnitId: Int) {
        if (!isAdLoading && interstitialAds == null) {
            isAdLoading = true
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                activity,
                activity.getString(adUnitId),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(mInterstitialAds: InterstitialAd) {
                        interstitialAds = mInterstitialAds
                        isAdLoading = false
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        interstitialAds = null
                        isAdLoading = false
                    }
                }
            )
        }
    }

    fun isAdLoaded(): Boolean {
        return interstitialAds != null
    }

    fun showInterstitialAds(activity: Activity, adUnitId: Int, afterSomeCode: () -> Unit) {
        if (interstitialAds != null) {
            interstitialAds!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        interstitialAds = null
                        afterSomeCode()
                        preloadInterstitialAds(activity, adUnitId)
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        interstitialAds = null
                        afterSomeCode()
                        preloadInterstitialAds(activity, adUnitId)
                    }
                }
            interstitialAds!!.show(activity)
        } else {
            afterSomeCode()
            preloadInterstitialAds(activity, adUnitId)
        }
    }
}
