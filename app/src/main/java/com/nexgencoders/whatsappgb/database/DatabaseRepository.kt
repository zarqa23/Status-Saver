package com.nexgencoders.whatsappgb.database

import android.content.Context
import com.example.quotesapp.db.ScannedQrDatabase

object DatabaseRepository {
    lateinit var database: ScannedQrDatabase
    fun initDatabase(context: Context) {
        database = ScannedQrDatabase.databaseInstance(context)!!
    }
}