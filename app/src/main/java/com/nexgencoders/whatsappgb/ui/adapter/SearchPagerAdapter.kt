package com.nexgencoders.whatsappgb.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nexgencoders.whatsappgb.ui.fragment.ImageFragment
import com.nexgencoders.whatsappgb.ui.fragment.MyCodeFragment
import com.nexgencoders.whatsappgb.ui.fragment.ScanCodeFragment
import com.nexgencoders.whatsappgb.ui.fragment.VideoFragment
import com.nexgencoders.whatsappgb.ui.fragment.search.BusinessFragment
import com.nexgencoders.whatsappgb.ui.fragment.search.WhatsappFragment

class SearchPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WhatsappFragment()
            1 -> BusinessFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
