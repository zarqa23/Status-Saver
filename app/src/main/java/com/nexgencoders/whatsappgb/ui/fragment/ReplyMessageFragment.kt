package com.nexgencoders.whatsappgb.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentDirectChatBinding
import com.nexgencoders.whatsappgb.databinding.FragmentReplyMessageBinding
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_BUSINESS_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.isWhatsAppBusinessInstalled
import com.nexgencoders.whatsappgb.utils.HomeConst.isWhatsAppInstalled
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.hideKeyboard
import com.nexgencoders.whatsappgb.utils.toast
import com.nexgencoders.whatsappgb.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReplyMessageFragment : Fragment() {
    private var _binding: FragmentReplyMessageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReplyMessageBinding.inflate(layoutInflater,container,false)
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

            btnWhatsApp.setOnClickListener {
                sendWhatsAppMessage(true)
            }
            btnBusWa.setOnClickListener {
                sendWhatsAppMessage(false)
            }

            btnGen.setOnClickListener {
                binding.textInput.hideKeyboard()
                binding.pbTxt.visible()
                CoroutineScope(Dispatchers.Main).launch {
                    val resultString = withContext(Dispatchers.IO) {
                        delay(1000)
                        repeatStringWithNewLine(binding.textInput.text.toString().trim(),binding.counter.text.toString().toInt())
                    }
                    tvDisplay.text = resultString
                    binding.pbTxt.gone()
                }
            }
        }
    }

    private fun repeatStringWithNewLine(text: String, count: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 1..count) {
            stringBuilder.append(text).append("\n")
        }
        return stringBuilder.toString()
    }


    private fun sendWhatsAppMessage(isWhatsApp:Boolean) {
        val countryCode = binding.ccp.selectedCountryCodeWithPlus
        val phoneNumber = binding.numberInput.text.toString().trim()
        val message = binding.tvDisplay.text.toString().trim()
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