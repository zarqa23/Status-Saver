package com.nexgencoders.whatsappgb.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentDirectChatBinding
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_BUSINESS_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.isWhatsAppBusinessInstalled
import com.nexgencoders.whatsappgb.utils.HomeConst.isWhatsAppInstalled
import com.nexgencoders.whatsappgb.utils.loadSmallMediumSizeNativeAds
import com.nexgencoders.whatsappgb.utils.toast

class DirectChatFragment : Fragment() {

    private var _binding: FragmentDirectChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectChatBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSmallMediumSizeNativeAds(requireContext(), R.string.small_medium_native_ads,binding.templateView)

        handleClick()
    }
    private fun handleClick() {
        with(binding){
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnWhatsApp.setOnClickListener {
                sendWhatsAppMessage(true)
            }
            btnBusWa.setOnClickListener {
                sendWhatsAppMessage(false)
            }
        }
    }


    private fun sendWhatsAppMessage(isWhatsApp:Boolean) {
        val countryCode = binding.ccp.selectedCountryCodeWithPlus
        val phoneNumber = binding.numberInput.text.toString().trim()
        val message = binding.edTxtMsg.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            context?.toast("Please enter a phone number")
            return
        }

        if (message.isEmpty()) {
            context?.toast("Please enter a message")
            return
        }

        val fullPhoneNumber = countryCode + phoneNumber
        if (isWhatsApp) {
            if (isWhatsAppInstalled(requireContext())) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.`package` = WHATSAPP_PACKAGE
                intent.data = Uri.parse("https://wa.me/$fullPhoneNumber?text=${Uri.encode(message)}")
                startActivity(intent)
            } else {
                context?.toast("WhatsApp Not Installed")
            }
        } else {
            if (isWhatsAppBusinessInstalled(requireContext())) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.`package` = WHATSAPP_BUSINESS_PACKAGE
                intent.data = Uri.parse("https://wa.me/$fullPhoneNumber?text=${Uri.encode(message)}")
                startActivity(intent)
            } else {
                context?.toast("WhatsApp Business Not Installed")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}