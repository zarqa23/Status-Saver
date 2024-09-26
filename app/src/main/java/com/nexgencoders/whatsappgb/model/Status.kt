package com.nexgencoders.whatsappgb.model

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.util.Objects


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Status(
    var file: @RawValue File? = null,
    var title: String? = null,
    var path: String? = null,
    var isVideo: Boolean,
    var isApi30: Boolean,
    var documentFileUri: Uri? = null
) : Parcelable {
    companion object {
        private const val MP4 = ".mp4"
    }

    // Constructor for file-based status
    constructor(file: File, title: String?, path: String?) : this(
        file = file,
        title = title,
        path = path,
        isApi30 = false,
        isVideo = file.name.endsWith(MP4)
    )

    constructor(documentFile: DocumentFile) : this(
        isApi30 = true,
        documentFileUri = documentFile.uri,
        isVideo = documentFile.name?.endsWith(MP4) == true
    )
}

