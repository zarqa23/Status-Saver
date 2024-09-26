package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentQrCodeBinding
import com.nexgencoders.whatsappgb.ui.adapter.QRViewPagerAdapter
import com.nexgencoders.whatsappgb.ui.adapter.StatusViewPageAdapter
import com.nexgencoders.whatsappgb.utils.gone


class QrCodeFragment : Fragment() {

    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: QRViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClick()
    }
    private fun handleClick() {
        with(binding){
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            initViewPager()
        }
    }

    private fun initViewPager() {
        viewPagerAdapter = QRViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = layoutInflater.inflate(R.layout.tab_custom_layout, null).apply {
                findViewById<ImageView>(R.id.tab_icon).gone()
                findViewById<TextView>(R.id.tab_text).text = when (position) {
                    0 -> "Scan Code"
                    1 -> "My Code"
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