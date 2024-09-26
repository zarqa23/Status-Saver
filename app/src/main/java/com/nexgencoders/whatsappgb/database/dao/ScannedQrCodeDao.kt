package com.nexgencoders.whatsappgb.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity

@Dao
interface ScannedQrCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(qrEntity: ScannedQrCodeEntity)

    @Update
    suspend fun updateData(qrEntity: ScannedQrCodeEntity): Int

//    @Query("DELETE FROM ScannedQrCodeEntity WHERE quoteID = :quoteId")
//    suspend fun deleteFavouriteByQuoteId(quoteId: Int)


    @Query("SELECT * FROM ScannedQrCodeEntity")
    fun getAllScannedQrCode(): LiveData<List<ScannedQrCodeEntity>>


}