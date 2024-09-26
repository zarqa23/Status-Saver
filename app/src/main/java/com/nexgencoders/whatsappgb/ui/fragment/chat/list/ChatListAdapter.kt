package com.nexgencoders.whatsappgb.ui.fragment.chat.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.ItemChatBinding
import com.nexgencoders.whatsappgb.model.Chat
import com.nexgencoders.whatsappgb.utility.getTime
import com.nexgencoders.whatsappgb.utility.loadImage


class ChatListAdapter : ListAdapter<Chat, ChatListAdapter.ViewHolder>(ChatListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentData: Chat? = null

        init {
            binding.root.setOnClickListener {
                it.findNavController().navigate(
                    R.id.chatDetailFragment,
                    bundleOf("user" to currentData?.user,"app" to currentData?.app)
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Chat?) {
            item?.let {
                binding.user.text = it.user
                binding.lastMessage.text = it.message
                binding.date.text = it.time.getTime()
                if (it.isGroup) binding.image.loadImage(R.drawable.chat_group)
                else binding.image.loadImage(R.drawable.chat_user)
            }
            currentData = item
        }
    }

    class ChatListDiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.user == newItem.user
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

}