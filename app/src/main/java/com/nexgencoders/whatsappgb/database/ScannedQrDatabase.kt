package com.example.quotesapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nexgencoders.whatsappgb.database.dao.ScannedQrCodeDao
import com.nexgencoders.whatsappgb.database.dao.UserDao
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity
import com.nexgencoders.whatsappgb.model.Chat


@Database(
    entities = [ScannedQrCodeEntity::class, Chat::class], exportSchema = false, version = 2
)
abstract class ScannedQrDatabase : RoomDatabase() {

    abstract val scannedQrCodeDao: ScannedQrCodeDao
    abstract fun userDao(): UserDao


    companion object {
        @Volatile
        private var instance: ScannedQrDatabase? = null

        private const val DBNAME = "ScannedQrCodeDatabase_v1"

        fun databaseInstance(context: Context): ScannedQrDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, ScannedQrDatabase::class.java, DBNAME
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}