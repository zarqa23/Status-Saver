package com.nexgencoders.whatsappgb.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nexgencoders.whatsappgb.R


fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun Context.startActivity(activityClass: Class<*>, extras: Map<String, Any>? = null) {
    val intent = Intent(this, activityClass)
    extras?.let {
        for ((key, value) in it) {
            when (value) {
                is String -> intent.putExtra(key, value)
                is Int -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                is Float -> intent.putExtra(key, value)
                is Double -> intent.putExtra(key, value)
                else -> throw IllegalArgumentException("Unsupported extra type")
            }
        }
    }

    this.startActivity(intent)
}

fun Context.startActivity(activityClass: Class<*>) {
    val intent = Intent(this, activityClass)
    this.startActivity(intent)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Window.setBarIcons(useDarkIcons: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For Android 11 and above
        val controller = this.insetsController
        if (controller != null) {
            controller.setSystemBarsAppearance(
                if (useDarkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // For Android 6 (M) to Android 10
        this.decorView.systemUiVisibility = if (useDarkIcons) {
            this.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            this.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Context.showStatusPermissionDialog(action: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(this)
    val inflater = LayoutInflater.from(this)
    val dialogView = inflater.inflate(R.layout.status_permission_dialog, null)

    builder.setView(dialogView)
    val dialog = builder.create()

    val btnNext = dialogView.findViewById<TextView>(R.id.btnNext)
    val btnClose = dialogView.findViewById<ImageView>(R.id.btnClose)

    btnClose.setOnClickListener {
        action.invoke(true)
        dialog.dismiss()
    }
    btnNext.setOnClickListener {
        action.invoke(false)
        dialog.dismiss()
    }

    dialog.show()

    dialog.window?.apply {
        setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setGravity(Gravity.CENTER)
    }
}

fun Context.showNotificationPermissionDialog(action: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(this)
    val inflater = LayoutInflater.from(this)
    val dialogView = inflater.inflate(R.layout.notification_permission_dialog, null)

    builder.setView(dialogView)
    val dialog = builder.create()

    val btnAllow = dialogView.findViewById<TextView>(R.id.btnAllow)
    val btnClose = dialogView.findViewById<TextView>(R.id.btnClose)

    btnClose.setOnClickListener {
        action.invoke(true)
        dialog.dismiss()
    }
    btnAllow.setOnClickListener {
        action.invoke(false)
        dialog.dismiss()
    }

    dialog.show()

    dialog.window?.apply {
        setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setGravity(Gravity.CENTER)
    }
}

fun Context.showWarning(action: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    val inflater = LayoutInflater.from(this)
    val dialogView = inflater.inflate(R.layout.warning_dialog, null)

    builder.setView(dialogView)
    val dialog = builder.create()

    val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
    val btnOk = dialogView.findViewById<TextView>(R.id.btnOk)

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
    btnOk.setOnClickListener {
        action.invoke()
        dialog.dismiss()
    }

    dialog.show()

    dialog.window?.apply {
        setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setGravity(Gravity.CENTER)
    }
}








