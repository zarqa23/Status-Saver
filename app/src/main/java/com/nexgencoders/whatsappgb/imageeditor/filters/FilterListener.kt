package com.nexgencoders.whatsappgb.imageeditor.filters

import com.nexgencoders.mylibrary.PhotoFilter


interface FilterListener {
    fun onFilterSelected(photoFilter: PhotoFilter)
}