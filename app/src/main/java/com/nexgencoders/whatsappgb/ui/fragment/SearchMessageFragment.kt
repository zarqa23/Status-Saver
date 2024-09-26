package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentSearchMessageBinding
import com.nexgencoders.whatsappgb.ui.adapter.SearchPagerAdapter
import com.nexgencoders.whatsappgb.utils.gone


class SearchMessageFragment : Fragment() {

    private var _binding: FragmentSearchMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: SearchPagerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadSmallMediumSizeNativeAds(requireContext(), R.string.small_medium_native_ads,binding.templateView)
        initViewPager()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun initViewPager() {
        viewPagerAdapter = SearchPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = layoutInflater.inflate(R.layout.tab_custom_layout, null).apply {
                findViewById<ImageView>(R.id.tab_icon).gone()
                findViewById<TextView>(R.id.tab_text).text = when (position) {
                    0 -> "What’s App"
                    1 -> "WA/Business"
                    else -> null
                }
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}