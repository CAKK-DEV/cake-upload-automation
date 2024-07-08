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

        shopDirectoryList.forEach { shopName ->
            val oldDir = File("shops/$shopName")
            val files: Array<File> = oldDir.listFiles() ?: throw RuntimeException("$shopName 폴터가 비어있습니다.")
            val cakeFiles: Array<CakeFile> = files.map { CakeFile(it.name, fileToMultipartFile(it)) }.toTypedArray()

            shopToCakesMap[shopName] = cakeFiles
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
