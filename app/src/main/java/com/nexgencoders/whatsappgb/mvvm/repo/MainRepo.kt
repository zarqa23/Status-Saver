package com.nexgencoders.whatsappgb.mvvm.repo

import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.database.DatabaseRepository
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity
import com.nexgencoders.whatsappgb.model.CaptionDataModel
import com.nexgencoders.whatsappgb.model.HomeDataModel
import com.nexgencoders.whatsappgb.utils.HomeConst.CAPTION
import com.nexgencoders.whatsappgb.utils.HomeConst.DELETED_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.DIRECT_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.EDIT_STATUS
import com.nexgencoders.whatsappgb.utils.HomeConst.QR_CODE
import com.nexgencoders.whatsappgb.utils.HomeConst.REPLY_MESSAGE
import com.nexgencoders.whatsappgb.utils.HomeConst.SEARCH_MESSAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepo {
    suspend fun getHomeData(onSuccess: (data: ArrayList<HomeDataModel>) -> Unit) =
        withContext(Dispatchers.IO) {
            val data: ArrayList<HomeDataModel> = ArrayList()
            data.apply {
                add(HomeDataModel(R.drawable.deleted_msg, "Deleted Message", DELETED_MESSAGE))
                add(HomeDataModel(R.drawable.ic_qr, "QR Code", QR_CODE))
                add(HomeDataModel(R.drawable.ic_edit_status, "Edit Status", EDIT_STATUS))
                add(HomeDataModel(R.drawable.ic_direct_msg, "Direct Chat", DIRECT_MESSAGE))
                add(HomeDataModel(R.drawable.ic_caption, "Caption", CAPTION))
                add(HomeDataModel(R.drawable.ic_search_msg, "Search Message", SEARCH_MESSAGE))
                add(HomeDataModel(R.drawable.ic_reply_msg, "Reply Message", REPLY_MESSAGE))
//                add(HomeDataModel(R.drawable.ic_chatting, "Chatting", CHATTING))

            }

            withContext(Dispatchers.Main) {
                onSuccess.invoke(data)
            }
        }
    suspend fun getCategoryData(onSuccess: (data: ArrayList<CaptionDataModel>) -> Unit) =
        withContext(Dispatchers.IO) {
            val data: ArrayList<CaptionDataModel> = ArrayList()
            data.apply {
                add(CaptionDataModel(R.drawable.love, "Love"))
                add(CaptionDataModel(R.drawable.laugh, "Laugh"))
                add(CaptionDataModel(R.drawable.motivational, "Motivation"))
                add(CaptionDataModel(R.drawable.flirty, "Flirty"))
                add(CaptionDataModel(R.drawable.heartbreak, "Heartbroken"))
                add(CaptionDataModel(R.drawable.sad, "Sad"))
                add(CaptionDataModel(R.drawable.pain, "Pain"))
                add(CaptionDataModel(R.drawable.cry, "Cry"))
                add(CaptionDataModel(R.drawable.religious, "Religious"))
                add(CaptionDataModel(R.drawable.friendship, "Friendship"))
                add(CaptionDataModel(R.drawable.success, "Success"))
                add(CaptionDataModel(R.drawable.happiness, "Happiness"))
                add(CaptionDataModel(R.drawable.life, "Life"))
                add(CaptionDataModel(R.drawable.loneliness, "Loneliness"))
                add(CaptionDataModel(R.drawable.positivity, "Positivity"))
                add(CaptionDataModel(R.drawable.self_love, "Self-love"))
                add(CaptionDataModel(R.drawable.inpiration, "Inspiration"))
                add(CaptionDataModel(R.drawable.forgiveness, "Forgiveness"))
                add(CaptionDataModel(R.drawable.gratitude, "Gratitude"))
                add(CaptionDataModel(R.drawable.growth, "Growth"))
                add(CaptionDataModel(R.drawable.determination, "Determination"))
                add(CaptionDataModel(R.drawable.dream, "Dreams"))
                add(CaptionDataModel(R.drawable.family, "Family"))
                add(CaptionDataModel(R.drawable.hope, "Hope"))
                add(CaptionDataModel(R.drawable.regret, "Regret"))
                add(CaptionDataModel(R.drawable.movingon, "Moving On"))
                add(CaptionDataModel(R.drawable.faith, "Faith"))
                add(CaptionDataModel(R.drawable.trust, "Trust"))
            }

            withContext(Dispatchers.Main) {
                onSuccess.invoke(data)
            }
        }
    suspend fun insertFavouriteItemToDB(item: ScannedQrCodeEntity) =
        DatabaseRepository.database.scannedQrCodeDao.insertData(item)

    fun getAllQrCode() =
        DatabaseRepository.database.scannedQrCodeDao.getAllScannedQrCode()

}