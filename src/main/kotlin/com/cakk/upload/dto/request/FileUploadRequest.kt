package com.cakk.upload.dto.request

import org.springframework.web.multipart.MultipartFile

data class FileUploadRequest(
    val file: MultipartFile,
    val fileName: String
)
