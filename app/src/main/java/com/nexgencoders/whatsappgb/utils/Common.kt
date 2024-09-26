package com.nexgencoders.whatsappgb.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.model.Status
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Common {
    const val GRID_COUNT: Int = 2

    private const val CHANNEL_NAME = "statussaver"
    var APP_DIR: String? = null

    fun copyFile(status: Status, context: Context, container: ConstraintLayout) {
        val file = APP_DIR?.let { File(it) }
        if (file != null) {
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Snackbar.make(container, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        val fileName: String

        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateTime = sdf.format(Date())

        fileName = if (status.isVideo) {
            "VID_$currentDateTime.mp4"
        } else {
            "IMG_$currentDateTime.jpg"
        }

        val destFile = File(file.toString() + File.separator + fileName)

        try {
            if (status.isApi30) {
                val values = ContentValues()
                val destinationUri: Uri?
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/Status Saver"
                )

                val collectionUri: Uri
                if (status.isVideo) {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "video/*")
                    collectionUri = MediaStore.Video.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )
                } else {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
                    collectionUri = MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )
                }

                destinationUri = context.contentResolver.insert(collectionUri, values)

                val inputStream = context.contentResolver.openInputStream(
                    status.documentFileUri!!
                )
                val outputStream = context.contentResolver.openOutputStream(
                    destinationUri!!
                )
                IOUtils.copy(inputStream, outputStream)

                showNotification(context, container, status, fileName, destinationUri)
            } else {
                FileUtils.copyFile(status.file, destFile)
                destFile.setLastModified(System.currentTimeMillis())
                if (file != null) {
                    SingleMediaScanner(context, file)
                }

                val data = FileProvider.getUriForFile(
                    context, "com.nexgencoders.whatsappgb.provider",
                    File(destFile.absolutePath)
                )

                showNotification(context, container, status, fileName, data)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showNotification(
        context: Context, container: ConstraintLayout, status: Status,
        fileName: String, data: Uri?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context)
        }

        val intent = Intent(Intent.ACTION_VIEW)

        if (status.isVideo) {
            intent.setDataAndType(data, "video/*")
        } else {
            intent.setDataAndType(data, "image/*")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification =
            NotificationCompat.Builder(context, CHANNEL_NAME)

        notification.setSmallIcon(R.drawable.ic_download)
            .setContentTitle(fileName)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) notification.setContentText(
            "File Saved to " +
                    Environment.DIRECTORY_DCIM + "/Status Saver"
        )
        else notification.setContentText("File Saved to$APP_DIR")

        val notificationManager = checkNotNull(
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )

        notificationManager.notify(Random().nextInt(), notification.build())

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) Snackbar.make(
            container,
            "Saved to $APP_DIR",
            Snackbar.LENGTH_LONG
        ).show()
        else Snackbar.make(
            container, "Saved to " + Environment.DIRECTORY_DCIM + "/Status Saver",
            Snackbar.LENGTH_LONG
        ).show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun makeNotificationChannel(context: Context) {
        val channel =
            NotificationChannel(CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT)
        channel.setShowBadge(true)

        val notificationManager = checkNotNull(
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )

        notificationManager.createNotificationChannel(channel)
    }



    suspend fun <T> Task<T>.await(): T {
        return suspendCoroutine { continuation ->
            addOnSuccessListener { result ->
                continuation.resume(result)
            }
            addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun generateQRCode(text: String,size:Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()

        return try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun isNumberInList(number: String, list: List<String>): Boolean {
        return list.contains(number)
    }

    fun normalizePhoneNumber(phoneNumber: String): String {
        return phoneNumber.replace("[^+0-9]".toRegex(), "")
    }


    fun Context.readRawJsonFile(rawResourceId: Int): String {
        val inputStream = resources.openRawResource(rawResourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }

        reader.close()
        return stringBuilder.toString()
    }

    fun Context.copyToClip(data: String) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", data)
        clipboard?.setPrimaryClip(clip)
    }

    fun Context.shareText(text: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(shareIntent, "Share via")
        startActivity(chooser)
    }

    fun Context.shareApp() {
        val appPackageName = this.packageName
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this app")
            putExtra(Intent.EXTRA_TEXT, "Check out this app: https://play.google.com/store/apps/details?id=$appPackageName")
        }
        this.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun Context.showMoreApps() {
        val uri = Uri.parse("market://developer?id=YourDeveloperName") // Replace with your Developer name
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        try {
            this.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=YourDeveloperName")))
        }
    }

    fun Activity.sendFeedback() {
        val feedbackIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("your.email@example.com")) // Your email here
            putExtra(Intent.EXTRA_SUBJECT, "Feedback for ${this@sendFeedback.getString(R.string.app_name)}")
        }
        try {
            this.startActivity(feedbackIntent)
        } catch (e: ActivityNotFoundException) {
            this.toast("No email app found")
        }
    }

    fun Context.rateUs() {
        val uri = Uri.parse("market://details?id=${this.packageName}")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        try {
            this.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${this.packageName}")))
        }
    }

}
