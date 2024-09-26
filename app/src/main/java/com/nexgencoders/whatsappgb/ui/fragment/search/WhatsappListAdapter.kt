package com.nexgencoders.whatsappgb.ui.fragment.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nexgencoders.whatsappgb.databinding.ItemChatBinding
import com.nexgencoders.whatsappgb.model.WhatsAppContact


class WhatsappListAdapter(private val onItemClick: (String) -> Unit) : ListAdapter<WhatsAppContact, WhatsappListAdapter.ViewHolder>(ChatListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentData: WhatsAppContact? = null

        init {
            binding.root.setOnClickListener {

                onItemClick(currentData!!.number)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: WhatsAppContact?) {
            item?.let {
                binding.user.text = it.name
                binding.lastMessage.text = it.number
            }
            currentData = item
        }
    }

    class ChatListDiffCallback : DiffUtil.ItemCallback<WhatsAppContact>() {
        override fun areItemsTheSame(oldItem: WhatsAppContact, newItem: WhatsAppContact): Boolean {
            return oldItem.number == newItem.number
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: WhatsAppContact, newItem: WhatsAppContact): Boolean {
            return oldItem == newItem
        }
    }

}