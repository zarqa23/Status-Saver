package com.nexgencoders.whatsappgb.model

import androidx.annotation.Keep

@Keep
data class DeletedMessage(
    val id: String ="0",
    val message: String = "",
    val isDeleted: Boolean = false,
    val time: Long,
)
