package com.nexgencoders.whatsappgb

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.nexgencoders.whatsappgb.database.DatabaseRepository

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        DatabaseRepository.initDatabase(context = this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        var app: MyApplication? = null
    }

}