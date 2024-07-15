package com.cakk.upload

import com.cakk.upload.io.Reader
import com.cakk.upload.service.cake.CakeShopService
import com.cakk.upload.vo.CakeFile
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import java.util.*
import kotlin.system.exitProcess

@SpringBootApplication
class Application

@PostConstruct
fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}

fun main(args: Array<String>) {
    started()

    val context: ApplicationContext = runApplication<Application>(*args)
    println("[LOG] ::::::::: 케이크 업로드를 시작합니다.\n")
    val cakeShopService = context.getBean(CakeShopService::class.java)

    // 읽어오기
    val reader = Reader()
    println("[LOG] ::::::::: 파일을 읽어옵니다.\n")
    val shopToCakesMap: Map<String, Array<CakeFile>> = reader.readCakes()

    // 케이크 샵에 해당하는 케이크들 저장
    println()
    println("[LOG] ::::::::: 케이크 저장을 시작합니다.\n")
    shopToCakesMap.forEach { (shopName, cakeFiles) ->
        println("-------------------------------------------------------------------------------")
        cakeShopService.process(shopName, cakeFiles.toList())
        println("-------------------------------------------------------------------------------")
    }

    exitProcess(0)
}
