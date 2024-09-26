package com.nexgencoders.whatsappgb.ui.fragment.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nexgencoders.whatsappgb.databinding.FragmentBusinessBinding
import com.nexgencoders.whatsappgb.ui.fragment.chat.ChatViewModel
import com.nexgencoders.whatsappgb.utility.hide
import com.nexgencoders.whatsappgb.utility.show
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_BUSINESS_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.WHATSAPP_PACKAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.isWhatsAppBusinessInstalled
import com.nexgencoders.whatsappgb.utils.toast


class BusinessFragment : Fragment() {

    private lateinit var binding: FragmentBusinessBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: WhatsappListAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getContact()
            } else {
                context?.toast("Contact permission is required to access contacts")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBusinessBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestContactPermission()
        adapter = WhatsappListAdapter {
            val intent = Intent(Intent.ACTION_VIEW)
                intent.`package` = WHATSAPP_BUSINESS_PACKAGE
            intent.data = Uri.parse("https://wa.me/$it?text=${Uri.encode("")}")
            startActivity(intent)
        }
    }
    private fun isContactPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission() {
        if (isContactPermissionGranted()) {
            getContact()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
    fun getContact() {

        if (isWhatsAppBusinessInstalled(requireContext())) {
            binding.emptyTv.hide()
            viewModel.getBusinessWha(context?.contentResolver!!)
                .observe(viewLifecycleOwner) {
                    if (it.isNullOrEmpty()) run {
                        binding.emptyTv.show()
                    } else binding.emptyTv.hide()
                    adapter.submitList(it)
                    binding.recyclerView.adapter = adapter
                    binding.loading.hide()
                }
        } else {
            binding.loading.hide()
            binding.emptyTv.show()
            binding.emptyTv.text = "Business WhatsApp Not Installed"
        }

    }
}