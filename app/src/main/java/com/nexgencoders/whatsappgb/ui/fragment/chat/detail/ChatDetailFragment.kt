package com.nexgencoders.whatsappgb.ui.fragment.chat.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nexgencoders.whatsappgb.databinding.FragmentChatDetailBinding
import com.nexgencoders.whatsappgb.utility.hide
import com.nexgencoders.whatsappgb.ui.fragment.chat.ChatViewModel

class ChatDetailFragment : Fragment() {

    private val adapter = ChatDetailAdapter()
    private val viewModel: ChatViewModel by viewModels()

    private var _binding: FragmentChatDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchChatsByUser()
    }

    private fun fetchChatsByUser() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        val user = arguments?.getString("user") ?: ""
        val app = arguments?.getString("app") ?: ""
        binding.username.text = user
        viewModel.getChatByUser(user,app).observe(viewLifecycleOwner) {
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
}