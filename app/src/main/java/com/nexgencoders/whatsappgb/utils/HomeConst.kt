package com.nexgencoders.whatsappgb.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import java.io.File

object HomeConst {
    const val EDIT_STATUS = "editStatus"
    const val QR_CODE = "QrCode"
    const val CHATTING = "chatting"
    const val CAPTION = "caption"
    const val DIRECT_MESSAGE = "directMessage"
    const val SEARCH_MESSAGE = "searchMessage"
    const val DELETED_MESSAGE = "deletedMessage"
    const val REPLY_MESSAGE = "replyMessage"

    const val WHATSAPP_WEB_LINK = "https://web.whatsapp.com"
    const val WHATSAPP_PACKAGE = "com.whatsapp"
    const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
    const val PRIVACY_POLICY = "https://www.nexgencoders.org/"

    // Directory for statuses

    val whatsappStatusDirectory = File(
        Environment.getExternalStorageDirectory().toString() +
                File.separator + "WhatsApp/Media/.Statuses"
    )

    val whatsappBusinessStatusDirectory = File(
        Environment.getExternalStorageDirectory().toString() +
                File.separator + "WhatsApp Business/Media/.Statuses"
    )

    fun isWhatsAppInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun isWhatsAppBusinessInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
