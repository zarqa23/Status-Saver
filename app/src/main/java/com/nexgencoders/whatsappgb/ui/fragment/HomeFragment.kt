package com.nexgencoders.whatsappgb.ui.fragment

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.storage.StorageManager
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.ACTIVITY_SERVICE
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdSize
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentHomeBinding
import com.nexgencoders.whatsappgb.mvvm.viewmodel.MainViewModel
import com.nexgencoders.whatsappgb.ui.activity.HomeActivity
import com.nexgencoders.whatsappgb.ui.adapter.HomeAdapter
import com.nexgencoders.whatsappgb.ui.ads.MyInterstitialAds
import com.nexgencoders.whatsappgb.utils.HomeConst.CAPTION
import com.nexgencoders.whatsappgb.utils.HomeConst.CHATTING
import com.nexgencoders.whatsappgb.utils.HomeConst.DELETED_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.DIRECT_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.EDIT_STATUS
import com.nexgencoders.whatsappgb.utils.HomeConst.QR_CODE
import com.nexgencoders.whatsappgb.utils.HomeConst.REPLY_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.SEARCH_MESSAGE
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.loadBannerAds
import com.nexgencoders.whatsappgb.utils.visible
import java.util.Objects

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var myInterstitialAds: MyInterstitialAds
    private val adUnitId = R.string.interstitial_ads1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myInterstitialAds = MyInterstitialAds()
        myInterstitialAds.preloadInterstitialAds(requireActivity(), adUnitId)
        loadBannerAds(requireContext(),binding.homeAdsFrameLayout, AdSize.FULL_BANNER,R.string.banner_ads1)
        viewModel.getHomeData()
        handleClickListener()
        initHomeRV()

    }

    private fun initHomeRV() {
        homeAdapter = HomeAdapter {
            when(it) {
                EDIT_STATUS -> {
                    findNavController().navigate(R.id.action_homeFragment_to_imageFragment,
                        bundleOf("type" to "edit")
                    )
                }
                QR_CODE -> goWithAdScreen(R.id.action_homeFragment_to_qrCodeFragment)
                CHATTING -> findNavController().navigate(R.id.action_homeFragment_to_chattingFragment)
                CAPTION -> findNavController().navigate(R.id.action_homeFragment_to_captionFragment)
                DIRECT_MESSAGE -> findNavController().navigate(R.id.action_homeFragment_to_directChatFragment)
                SEARCH_MESSAGE -> findNavController().navigate(R.id.action_homeFragment_to_searchMessageFragment)
                REPLY_MESSAGE -> goWithAdScreen(R.id.action_homeFragment_to_replyMessageFragment)
                DELETED_MESSAGE -> {
                    goWithAdScreen(R.id.action_homeFragment_to_chatListFragment)
                }
            }

        }
        binding.homeRv.apply {
            this.adapter = homeAdapter
            layoutManager = GridLayoutManager(binding.root.context, 2)
            setHasFixedSize(true)
        }
        binding.progress.visible()
        viewModel.homeData.observe(viewLifecycleOwner) { data ->
            homeAdapter.setData(data)
            binding.progress.gone()
        }
    }

    private fun handleClickListener() {
        with(binding){
            btnOpenDrwaer.setOnClickListener {
                (activity as? HomeActivity)?.openDrawer()
            }
            btnSaveNow.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_statusFragment)
            }
        }
    }


    private fun goWithAdScreen(navigate: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
        myInterstitialAds.showInterstitialAds(requireActivity(), adUnitId) {
            findNavController().navigate(navigate)
        }
        }, 1500)

    }

}