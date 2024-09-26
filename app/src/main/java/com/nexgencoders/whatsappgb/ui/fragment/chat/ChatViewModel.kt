package com.nexgencoders.whatsappgb.ui.fragment.chat

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nexgencoders.whatsappgb.model.Chat
import com.nexgencoders.whatsappgb.model.WhatsAppContact
import com.nexgencoders.whatsappgb.mvvm.repo.ChatRepository

class ChatViewModel: ViewModel() {
    private val repository: ChatRepository = ChatRepository()

    val getChatList: LiveData<List<Chat>> = liveData {
        val response = repository.fetchChats()
        emitSource(response)
    }

    fun  getChatByUser(user: String,app: String): LiveData<List<Chat>> = liveData {
        val response = repository.fetchMessagesByUser(user,app)
        emitSource(response)
    }

    fun getWhatsAppContacts(contentResolver: ContentResolver): LiveData<List<WhatsAppContact>> {
        return repository.getAllWhatsAppContactsLiveData(contentResolver)
    }
    fun getBusinessWha(contentResolver: ContentResolver): LiveData<List<WhatsAppContact>> {
        return repository.getWhatsappBusiness(contentResolver)
    }
}