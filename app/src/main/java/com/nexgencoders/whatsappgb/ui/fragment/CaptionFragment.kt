package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentCaptionBinding
import com.nexgencoders.whatsappgb.mvvm.viewmodel.MainViewModel
import com.nexgencoders.whatsappgb.ui.adapter.CaptionCategoryAdapter
import com.nexgencoders.whatsappgb.ui.ads.MyInterstitialAds
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.visible

class CaptionFragment : Fragment() {

    private var _binding: FragmentCaptionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var categoryAdapter: CaptionCategoryAdapter? = null

    private lateinit var myInterstitialAds: MyInterstitialAds
    private val adUnitId = R.string.interstitial_ads1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaptionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myInterstitialAds = MyInterstitialAds()
        myInterstitialAds.preloadInterstitialAds(requireActivity(), adUnitId)
        viewModel.getCategoryData()
        handleClick()
    }

    private fun handleClick() {
        with(binding) {
            capCatRv.setHasFixedSize(true)
            capCatRv.setLayoutManager(LinearLayoutManager(requireContext()))
            progress.visible()
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            viewModel.category.observe(viewLifecycleOwner) { data ->
                categoryAdapter = CaptionCategoryAdapter(data) { data, showAd ->
                    if (showAd) {
                        goWithAdScreen(data)
                    } else {
                        val action =
                            CaptionFragmentDirections.actionCaptionFragmentToCaptionDetailFragment(
                                data
                            )
                        findNavController().navigate(action)
                    }
                }
                capCatRv.setAdapter(categoryAdapter)
                binding.progress.gone()
            }
        }
    }

    private fun goWithAdScreen(data: String) {
        Handler(Looper.getMainLooper()).postDelayed({
            myInterstitialAds.showInterstitialAds(requireActivity(), adUnitId) {
                val action = CaptionFragmentDirections.actionCaptionFragmentToCaptionDetailFragment(data)
                findNavController().navigate(action)
            }
        }, 1500)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}