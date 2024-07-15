package com.cakk.upload.repository

import com.cakk.upload.entity.Cake
import org.springframework.data.jpa.repository.JpaRepository

interface CakeRepository : JpaRepository<Cake, Long>
