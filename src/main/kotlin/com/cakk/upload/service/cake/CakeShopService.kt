package com.cakk.upload.service.cake

import com.cakk.upload.dto.request.FileUploadRequest
import com.cakk.upload.dto.request.FilesUploadRequest
import com.cakk.upload.entity.CakeShop
import com.cakk.upload.repository.CakeShopRepository
import com.cakk.upload.service.s3.FileUploadService
import com.cakk.upload.vo.CakeFile
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CakeShopService(
    private val cakeShopRepository: CakeShopRepository,
    private val fileUploadService: FileUploadService
) {

    @Transactional
    fun process(shopName: String, cakeFiles: List<CakeFile>) {
        val cakeShop = getCakeShop(shopName)

        val thumbnail = cakeFiles.find { it.isThumbnail() }
        thumbnail?.let { saveShopThumbnail(cakeShop, it.fileName, it.image) }

        val cakes = cakeFiles.filter { !it.isThumbnail() }
        saveShopCakes(cakeShop, cakes.map { it.fileName }, cakes.map { it.image })
    }

    private fun saveShopThumbnail(shop: CakeShop, fileName: String, image: MultipartFile) {
        val uploadRequest = FileUploadRequest(image, fileName)
        val thumbnailUrl: String = fileUploadService.upload(uploadRequest)

        shop.updateThumbnailUrl(thumbnailUrl)
    }

    private fun saveShopCakes(shop: CakeShop, fileNames: List<String>, images: List<MultipartFile>) {
        val uploadRequest = FilesUploadRequest(images, fileNames)
        val cakeUrl: MutableList<String> = fileUploadService.uploadAll(uploadRequest)

        shop.addCakes(cakeUrl)
    }

    private fun getCakeShop(shopName: String): CakeShop {
        return cakeShopRepository.findByShopName(shopName) ?: throw EntityNotFoundException("[LOG]:::$shopName 가게를 찾을 수 없습니다.")
    }
}
