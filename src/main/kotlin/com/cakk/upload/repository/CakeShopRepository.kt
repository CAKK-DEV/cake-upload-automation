package com.cakk.upload.repository

import com.cakk.upload.entity.CakeShop
import org.springframework.data.jpa.repository.JpaRepository

interface CakeShopRepository : JpaRepository<CakeShop, Long> {

    fun findByShopName(name: String): CakeShop?
}
