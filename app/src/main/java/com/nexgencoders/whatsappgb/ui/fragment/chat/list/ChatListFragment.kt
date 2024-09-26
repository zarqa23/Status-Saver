package com.nexgencoders.whatsappgb.ui.fragment.chat.list

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nexgencoders.whatsappgb.databinding.FragmentChatListBinding
import com.nexgencoders.whatsappgb.ui.fragment.chat.ChatViewModel
import com.nexgencoders.whatsappgb.utility.hide
import com.nexgencoders.whatsappgb.utility.show
import com.nexgencoders.whatsappgb.utils.showNotificationPermissionDialog

class ChatListFragment : Fragment() {

    private val adapter = ChatListAdapter()
    private val viewModel: ChatViewModel by viewModels()

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isNotificationServiceEnabled()){
            requireContext().showNotificationPermissionDialog {
                if (it) {
                    findNavController().navigateUp()
                } else {
                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                }
            }
        }
    }

    private fun fetchChat() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getChatList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) run {
                binding.emptyTv.show()
            } else binding.emptyTv.hide()
            adapter.submitList(it)
            binding.recyclerView.adapter = adapter
            binding.loading.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = requireActivity().packageName
        val flat: String? = Settings.Secure.getString(
            requireActivity().contentResolver,
            "enabled_notification_listeners"
        )
        flat?.let {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        if (isNotificationServiceEnabled()) {
            fetchChat()
        }
    }

}