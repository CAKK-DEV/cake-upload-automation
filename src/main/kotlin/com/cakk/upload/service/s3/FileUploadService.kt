package com.cakk.upload.service.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.cakk.upload.dto.request.FileUploadRequest
import com.cakk.upload.dto.request.FilesUploadRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileUploadService(
    private val s3Client: AmazonS3,
    @Value("\${cloud.aws.s3-bucket}")
    private val bucketName: String
) {

    fun uploadAll(dto: FilesUploadRequest): MutableList<String> {
        val fileUrls: MutableList<String> = mutableListOf()

        dto.files.indices.forEach { idx ->
            val file: MultipartFile = dto.files[idx]
            val fileName: String = getPath(dto.fileNames[idx])

            fileUrls.add(putS3(file, fileName))
        }

        return fileUrls
    }

    fun upload(dto: FileUploadRequest): String {
        val fileName: String = getPath(dto.fileName)

        return putS3(dto.file, fileName)
    }

    private fun putS3(file: MultipartFile, fileName: String): String {
        s3Client.putObject(
            PutObjectRequest(bucketName, fileName, file.inputStream, getObjectMetadata(file))
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )

        return getS3Url(fileName)
    }

    private fun getS3Url(fileName: String): String {
        return s3Client.getUrl(bucketName, fileName).toString()
    }

    private fun getObjectMetadata(multipartFile: MultipartFile): ObjectMetadata {
        val objectMetaData = ObjectMetadata()
        objectMetaData.contentType = multipartFile.contentType
        objectMetaData.contentLength = multipartFile.size

        return objectMetaData
    }

    private fun getPath(filename: String): String {
        return "prod-app-api/%s".format(filename)
    }
}
