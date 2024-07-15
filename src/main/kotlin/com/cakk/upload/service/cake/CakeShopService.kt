package com.cakk.upload.service.cake

import com.cakk.upload.dto.request.FileUploadRequest
import com.cakk.upload.dto.request.FilesUploadRequest
import com.cakk.upload.entity.Cake
import com.cakk.upload.entity.CakeShop
import com.cakk.upload.repository.CakeRepository
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
    private val cakeRepository: CakeRepository,
    private val fileUploadService: FileUploadService
) {

    @Transactional
    fun process(shopName: String, cakeFiles: List<CakeFile>) {
        val cakeShop: CakeShop
        println("[LOG] ::::::::: $shopName 이라는 이름의 케이크 샵을 검색합니다.")

        try {
            cakeShop = getCakeShop(shopName)
            println("[LOG] ::::::::: $shopName 이라는 이름의 케이크 샵을 찾았습니다.")
        } catch (e: EntityNotFoundException) {
            println("[ERROR] ::::::: $shopName 이 존재하지 않습니다.")
            return
        }

        val thumbnail = cakeFiles.find { it.isThumbnail() }
        thumbnail?.let {
            println("[LOG] ::::::::: $shopName 의 썸네일 파일이 존재합니다.")
            try {
                saveShopThumbnail(cakeShop, it.fileName, it.image)
                println("[LOG] ::::::::: $shopName 의 썸네일이 정상 저장 되었습니다.")
            } catch (e: Exception) {
                println("[ERROR] ::::::: $shopName 의 썸네일 저장 중 오류가 발생했습니다.")
                println(e.message)
            }
        }

        val cakes = cakeFiles.filter { !it.isThumbnail() }
        try {
            println("[LOG] ::::::::: $shopName 의 케이크 파일은 총 ${cakes.size}개 입니다.")
            saveShopCakes(cakeShop, cakes.map { it.fileName }, cakes.map { it.image })
            println("[LOG] ::::::::: $shopName 의 케이크가 정상 저장 되었습니다.")
        } catch (e: Exception) {
            println("[ERROR] ::::::: $shopName 의 케이크 저장 중 오류가 발생했습니다.")
        }
    }

    private fun saveShopThumbnail(shop: CakeShop, fileName: String, image: MultipartFile) {
        val uploadRequest = FileUploadRequest(image, fileName)
        val thumbnailUrl: String = fileUploadService.upload(uploadRequest)

        shop.updateThumbnailUrl(thumbnailUrl)
    }

    private fun saveShopCakes(shop: CakeShop, fileNames: List<String>, images: List<MultipartFile>) {
        val uploadRequest = FilesUploadRequest(images, fileNames)
        val cakeUrl: MutableList<String> = fileUploadService.uploadAll(uploadRequest)

        cakeRepository.saveAll(cakeUrl.map { Cake(it, 0, shop) })
    }

    private fun getCakeShop(shopName: String): CakeShop {
        return cakeShopRepository.findByShopName(shopName) ?: throw EntityNotFoundException()
    }
}
