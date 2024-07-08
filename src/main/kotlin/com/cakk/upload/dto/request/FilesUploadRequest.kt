package com.cakk.upload.dto.request

import org.springframework.web.multipart.MultipartFile

data class FilesUploadRequest(
    val files: List<MultipartFile>,
    val fileNames: List<String>
)
