package com.nexgencoders.whatsappgb.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdSize
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentWhatsappWebBinding
import com.nexgencoders.whatsappgb.utils.HomeConst.PRIVACY_POLICY
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.visible
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_WEB_LINK
import com.nexgencoders.whatsappgb.utils.loadBannerAds

class WhatsappWebFragment : Fragment() {

    private var _binding: FragmentWhatsappWebBinding? = null
    private val binding get() = _binding!!
    var data: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhatsappWebBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBannerAds(requireContext(),binding.homeAdsFrameLayout, AdSize.FULL_BANNER,R.string.banner_ads1)
        data = arguments?.getString("data").toString()
        handleClick()
    }
    private fun handleClick() {
        with(binding){
            loadWeb()
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWeb() {
        binding.progress.visible()
        binding.whatsappWeb.settings.javaScriptEnabled = true
        binding.whatsappWeb.webViewClient = WebViewClient()
        binding.whatsappWeb.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progress.visible()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.gone()
            }
        }
        if (data == "whatsapp"){
            binding.title.text = context?.getString(R.string.whatsapp_web)
            binding.whatsappWeb.loadUrl(WHATSAPP_WEB_LINK)
        }else{
            binding.title.text = context?.getString(R.string.privacy_policy)
            binding.whatsappWeb.loadUrl(PRIVACY_POLICY)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}