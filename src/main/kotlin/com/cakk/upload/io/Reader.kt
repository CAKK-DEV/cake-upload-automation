package com.cakk.upload.io

import com.cakk.upload.vo.CakeFile
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.util.*


class Reader {

    fun readCakes(): Map<String, Array<CakeFile>> {
        val shopDirectoryList = getSubDirectoryNames()
        val shopToCakesMap: MutableMap<String, Array<CakeFile>> = HashMap()

        println("-------------------------------------------------------------------------------")
        shopDirectoryList.forEach { shopName ->
            try {
                val oldDir = File("shops/$shopName")
                oldDir.isDirectory || throw RuntimeException("directory not found")
                println("[LOG] ::::::::: $shopName 의 파일을 읽어옵니다.")
                val files: Array<File> = oldDir.listFiles() ?: throw RuntimeException()
                val cakeFiles: Array<CakeFile> = files.map { CakeFile(it.name, fileToMultipartFile(it)) }.toTypedArray()
                println("[LOG] ::::::::: $shopName 의 파일은 총 ${cakeFiles.size}개 입니다")
                shopToCakesMap[shopName] = cakeFiles
            } catch (e: Exception) {
                if (e.message == "directory not found") {
                    println("[ERROR] ::::::: $shopName 은 폴더가 아닙니다.")
                } else {
                    println("[ERROR] ::::::: $shopName 폴더를 읽어오는 중 오류가 발생했습니다.")
                }
            }
            println("-------------------------------------------------------------------------------")
        }

        return shopToCakesMap
    }

    private fun getSubDirectoryNames(): MutableList<String> {
        val parentDir = File("shops/")
        return parentDir.listFiles()?.map { it.name }?.toMutableList() ?: mutableListOf()
    }

    private fun fileToMultipartFile(file: File): MultipartFile {
        val filename = file.name.split(".").first() + ".jpeg"
        val inputStream = FileInputStream(file)

        return MockMultipartFile(
            filename,
            filename,
            "image/jpeg",
            inputStream
        )
    }
}
