package com.nexgencoders.whatsappgb.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexgencoders.whatsappgb.database.entity.ScannedQrCodeEntity
import com.nexgencoders.whatsappgb.model.CaptionDataModel
import com.nexgencoders.whatsappgb.model.HomeDataModel
import com.nexgencoders.whatsappgb.mvvm.repo.MainRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val repository = MainRepo()
    val homeData = MutableLiveData<ArrayList<HomeDataModel>>()
    val category = MutableLiveData<ArrayList<CaptionDataModel>>()
    fun getHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHomeData {
                homeData.value = it
            }
        }
    }

    fun getCategoryData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategoryData {
                category.value = it
            }
        }
    }


    fun addItemToDB(item: ScannedQrCodeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavouriteItemToDB(item)
        }
    }

    fun getAllQrCode() = repository.getAllQrCode()


}