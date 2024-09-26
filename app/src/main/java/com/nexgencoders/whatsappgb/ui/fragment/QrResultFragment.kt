package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity
import com.nexgencoders.whatsappgb.databinding.FragmentQrResultBinding
import com.nexgencoders.whatsappgb.mvvm.viewmodel.MainViewModel
import com.nexgencoders.whatsappgb.utils.Common.copyToClip
import com.nexgencoders.whatsappgb.utils.Common.generateQRCode
import com.nexgencoders.whatsappgb.utils.Common.shareText
import com.nexgencoders.whatsappgb.utils.toast

class QrResultFragment : Fragment() {

    private lateinit var binding: FragmentQrResultBinding
    private val args: QrResultFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrResultBinding.inflate(layoutInflater ,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvResult.text = args.result

        Glide.with(requireContext()).load(generateQRCode(args.result,512)).into(binding.previewImg)
        if (args.isNew) initDataToDb()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCopy.setOnClickListener {
            context?.copyToClip(args.result)
            context?.toast("Copied to clipboard")
            findNavController().popBackStack(R.id.qrCodeFragment, true)
            findNavController().navigateUp()
        }
        binding.btnShare.setOnClickListener {
            context?.shareText(args.result)
        }

    }

    private fun initDataToDb(){
        val item = ScannedQrCodeEntity(
            qrCode = args.result
        )
        viewModel.addItemToDB(item)
    }
}