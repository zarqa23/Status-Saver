package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentCaptionDetailBinding
import com.nexgencoders.whatsappgb.model.CaptionData
import com.nexgencoders.whatsappgb.model.CaptionResponse
import com.nexgencoders.whatsappgb.ui.adapter.CaptionCategoryAdapter
import com.nexgencoders.whatsappgb.ui.adapter.CaptionDetailAdapter
import com.nexgencoders.whatsappgb.ui.ads.adsadapter.AdmobNativeAdAdapter
import com.nexgencoders.whatsappgb.utils.Common.copyToClip
import com.nexgencoders.whatsappgb.utils.Common.readRawJsonFile
import com.nexgencoders.whatsappgb.utils.Common.shareText
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.toast
import com.nexgencoders.whatsappgb.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CaptionDetailFragment : Fragment() {

    private var _binding: FragmentCaptionDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CaptionDetailFragmentArgs by navArgs()
    private var categoryAdapter: CaptionDetailAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaptionDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        loadCaptionData()
    }

    private fun loadCaptionData() {
        binding.capDetailRv.setHasFixedSize(true)
        binding.capDetailRv.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.progress.visible()
        CoroutineScope(Dispatchers.IO).launch {
            val data = loadCaptionByCategory(args.category)!!
            withContext(Dispatchers.Main) {
                categoryAdapter = CaptionDetailAdapter(data){ data,isCopy ->
                    if (isCopy){
                        context?.copyToClip(data)
                        context?.toast("Copied to clipboard")
                    }else{
                        context?.shareText(data)
                    }
                }
                val admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                    getString(R.string.recycler_view_native_ads),
                    categoryAdapter,
                    "medium"
                ).adItemInterval(4).build()

                binding.capDetailRv.setAdapter(admobNativeAdAdapter)
                binding.progress.gone()
            }
        }
    }

    private suspend fun loadCaptionByCategory(category: String): List<CaptionData>? {
        return withContext(Dispatchers.IO) {
            val jsonString = context?.readRawJsonFile(R.raw.data)
            val gson = Gson()
            val quotesResponse = gson.fromJson(jsonString, CaptionResponse::class.java)
            when (category) {
                "Love" -> quotesResponse.Love
                "Laugh" -> quotesResponse.Laugh
                "Motivation" -> quotesResponse.Motivation
                "Flirty" -> quotesResponse.Flirty
                "Heartbroken" -> quotesResponse.Heartbroken
                "Sad" -> quotesResponse.Sad
                "Pain" -> quotesResponse.Pain
                "Religious" -> quotesResponse.Religious
                "Cry" -> quotesResponse.Cry
                "Friendship" -> quotesResponse.Friendship
                "Success" -> quotesResponse.Success
                "Happiness" -> quotesResponse.Happiness
                "Life" -> quotesResponse.Life
                "Loneliness" -> quotesResponse.Loneliness
                "Positivity" -> quotesResponse.Positivity
                "Self_love" -> quotesResponse.Self_love
                "inspiration" -> quotesResponse.Inspiration
                "Forgiveness" -> quotesResponse.Forgiveness
                "Growth" -> quotesResponse.Growth
                "Determination" -> quotesResponse.Determination
                "Gratitude" -> quotesResponse.Gratitude
                "Dreams" -> quotesResponse.Dreams
                "Family" -> quotesResponse.Family
                "Hope" -> quotesResponse.Hope
                "Regret" -> quotesResponse.Regret
                "Moving_on" -> quotesResponse.Moving_On
                "Faith" -> quotesResponse.Faith
                "Trust" -> quotesResponse.Trust
                else -> null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}