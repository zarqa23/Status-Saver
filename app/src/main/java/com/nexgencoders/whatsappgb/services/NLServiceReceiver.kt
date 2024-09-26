package com.nexgencoders.whatsappgb.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.nexgencoders.whatsappgb.model.Chat
import com.nexgencoders.whatsappgb.mvvm.repo.ChatRepository
import com.nexgencoders.whatsappgb.mvvm.repo.MainRepo
import com.nexgencoders.whatsappgb.utility.Notifications
import com.nexgencoders.whatsappgb.utility.getRandomNum
import com.nexgencoders.whatsappgb.utility.isValidTitle
import kotlinx.coroutines.launch

class NLServiceReceiver : BroadcastReceiver() {

    private val applicationScope = ProcessLifecycleOwner.get().lifecycleScope

    lateinit var repository: ChatRepository
    lateinit var notifications: Notifications

    override fun onReceive(context: Context?, intent: Intent) {
        repository = ChatRepository()
        notifications = Notifications(context!!)
        saveMessage(intent)
        notifyDeletedMessage(intent)
    }

    private fun saveMessage(intent: Intent) {
        val title = intent.getStringExtra("title") ?: ""
        val text = intent.getStringExtra("text") ?: ""
        val time = intent.getLongExtra("time", 0)
        val app = intent.getStringExtra("app") ?: ""
        val id = getRandomNum()

        val chat = Chat(id, title, text, time, app)
        applicationScope.launch {
            if (title.contains("messages") || title.contains(":")) {
                var group = title.substringBefore(":")
                val user = title.substringAfter(": ")
                if (group.endsWith("messages)")) group = group.split(" (")[0]

                val groupChat = Chat(id, group, "$user: $text", time, app, isGroup = true)
                if (text.isValidTitle()) repository.saveMessage(groupChat)
            } else {
                if (title.isValidTitle()) repository.saveMessage(chat)
            }
        }
    }

    private fun notifyDeletedMessage(intent: Intent) {
        val isDelete = intent.getBooleanExtra("isDeleted", false)
        val title = intent.getStringExtra("title") ?: ""
        val app = intent.getStringExtra("app") ?: ""

        if (isDelete) {
            applicationScope.launch {
                val lastMessage = repository.lastMessage(title)
                lastMessage?.let {
                    if (!lastMessage.isDeleted) {
                        notifications.notify(title, "$title deleted a message", "Click here to see",app)
                        repository.messageIsDeleted(lastMessage.id)
                    }
                }
            }
        }
    }
}