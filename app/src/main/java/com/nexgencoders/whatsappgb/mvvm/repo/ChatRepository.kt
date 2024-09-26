package com.nexgencoders.whatsappgb.mvvm.repo

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nexgencoders.whatsappgb.database.DatabaseRepository
import com.nexgencoders.whatsappgb.model.Chat
import com.nexgencoders.whatsappgb.model.DeletedMessage
import com.nexgencoders.whatsappgb.model.WhatsAppContact
import com.nexgencoders.whatsappgb.utility.isValidTitle
import com.nexgencoders.whatsappgb.utils.Common.normalizePhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class ChatRepository {
    private val database: DatabaseRepository = DatabaseRepository
    suspend fun saveMessage(chat: Chat) {
        withContext(Dispatchers.IO) {
            if (chat.message.isValidTitle()) {
                val lastMessage = database.database.userDao().getLastMessage(chat.user)
                if (lastMessage != null) {
                    if (chat.message != lastMessage.message && chat.time != lastMessage.time) database.database.userDao()
                        .save(chat)
                } else {
                    database.database.userDao().save(chat)
                }
            }
        }
    }

    suspend fun fetchChats(): LiveData<List<Chat>> {
        return withContext(Dispatchers.IO) {
            database.database.userDao().getChats()
        }
    }

    suspend fun fetchMessagesByUser(user: String, app: String): LiveData<List<Chat>> {
        return withContext(Dispatchers.IO) {
            database.database.userDao().getMessagesByUser(user, app)
        }
    }

    suspend fun lastMessage(user: String): DeletedMessage? {
        return withContext(Dispatchers.IO) {
            database.database.userDao().getLastMessage(user)
        }
    }

    suspend fun messageIsDeleted(id: String) {
        withContext(Dispatchers.IO) {
            database.database.userDao().messageIsDeleted(id)
        }
    }

    @SuppressLint("Range")
    fun getAllWhatsAppContactsLiveData(contentResolver: ContentResolver): LiveData<List<WhatsAppContact>> {
        val whatsappContactsLiveData = MutableLiveData<List<WhatsAppContact>>()

        CoroutineScope(Dispatchers.IO).launch {
            val whatsappContacts = mutableListOf<WhatsAppContact>()

            // Query to get contact IDs for WhatsApp contacts
            val contactCursor = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                arrayOf(ContactsContract.RawContacts.CONTACT_ID),
                ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?",
                arrayOf("com.whatsapp"),
                null
            )

            if (contactCursor != null && contactCursor.count > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        val whatsappContactId = contactCursor.getString(
                            contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)
                        )

                        if (whatsappContactId != null) {
                            // Query for contact name using the contact ID
                            val nameCursor = contentResolver.query(
                                ContactsContract.Contacts.CONTENT_URI,
                                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                                ContactsContract.Contacts._ID + " = ?",
                                arrayOf(whatsappContactId),
                                null
                            )

                            var contactName: String? = null
                            if (nameCursor != null && nameCursor.moveToFirst()) {
                                contactName = nameCursor.getString(
                                    nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                                )
                                nameCursor.close()
                            }

                            // Query for contact number using the contact ID
                            val whatsAppContactCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(whatsappContactId),
                                null
                            )

                            if (whatsAppContactCursor != null && whatsAppContactCursor.moveToFirst()) {
                                val number = whatsAppContactCursor.getString(
                                    whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                contactName?.let { name ->
                                    whatsappContacts.add(
                                        WhatsAppContact(
                                            name,
                                            normalizePhoneNumber(number)
                                        )
                                    )
                                }
                                whatsAppContactCursor.close()
                            }
                        }
                    } while (contactCursor.moveToNext())
                }
                contactCursor.close()
            }
            val sortedContacts = whatsappContacts.sortedBy { it.name }
            withContext(Dispatchers.Main) {
                whatsappContactsLiveData.value = sortedContacts
            }
        }

        return whatsappContactsLiveData
    }

    @SuppressLint("Range")
    fun getWhatsappBusiness(contentResolver: ContentResolver): LiveData<List<WhatsAppContact>> {
        val whatsappContactsLiveData = MutableLiveData<List<WhatsAppContact>>()

        CoroutineScope(Dispatchers.IO).launch {
            val whatsappContacts = mutableListOf<WhatsAppContact>()
            val contactCursor = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                arrayOf(ContactsContract.RawContacts.CONTACT_ID),
                ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?",
                arrayOf("com.whatsapp.w4b"),
                null
            )

            if (contactCursor != null && contactCursor.count > 0) {
                if (contactCursor.moveToFirst()) {
                    do {
                        val whatsappContactId = contactCursor.getString(
                            contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)
                        )

                        if (whatsappContactId != null) {
                            // Query for contact name using the contact ID
                            val nameCursor = contentResolver.query(
                                ContactsContract.Contacts.CONTENT_URI,
                                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                                ContactsContract.Contacts._ID + " = ?",
                                arrayOf(whatsappContactId),
                                null
                            )

                            var contactName: String? = null
                            if (nameCursor != null && nameCursor.moveToFirst()) {
                                contactName = nameCursor.getString(
                                    nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                                )
                                nameCursor.close()
                            }

                            // Query for contact number using the contact ID
                            val whatsAppContactCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(whatsappContactId),
                                null
                            )

                            if (whatsAppContactCursor != null && whatsAppContactCursor.moveToFirst()) {
                                val number = whatsAppContactCursor.getString(
                                    whatsAppContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                                contactName?.let { name ->
                                    whatsappContacts.add(
                                        WhatsAppContact(
                                            name,
                                            normalizePhoneNumber(number)
                                        )
                                    )
                                }
                                whatsAppContactCursor.close()
                            }
                        }
                    } while (contactCursor.moveToNext())
                }
                contactCursor.close()
            }
            val sortedContacts = whatsappContacts.sortedBy { it.name }
            withContext(Dispatchers.Main) {
                whatsappContactsLiveData.value = sortedContacts
            }
        }

        return whatsappContactsLiveData
    }

}