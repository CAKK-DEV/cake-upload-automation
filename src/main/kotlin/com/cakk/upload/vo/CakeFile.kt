package com.cakk.upload.vo

import org.springframework.web.multipart.MultipartFile

data class CakeFile(
    val fileName: String,
    val image: MultipartFile
) {
    fun isThumbnail(): Boolean {
        return fileName.contains("썸네일")
    }
}
