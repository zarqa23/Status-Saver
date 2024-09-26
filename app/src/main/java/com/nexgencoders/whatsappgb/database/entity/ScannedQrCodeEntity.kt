package com.nexgencoders.whatsappgb.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScannedQrCodeEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var qrCode: String = "",
)
