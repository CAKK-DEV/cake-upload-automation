package com.cakk.upload.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.time.LocalDateTime

@Entity
@Table(name = "cake")
class Cake(
    cakeImageUrl: String,
    heartCount: Int,
    cakeShop: CakeShop
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cake_id", nullable = false)
    val id: Long? = null

    @Column(name = "cake_image_url", nullable = false, length = 200)
    var cakeImageUrl: String? = cakeImageUrl

    @Column(name = "heart_count", nullable = false, columnDefinition = "MEDIUMINT")
    var heartCount: Int? = heartCount

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "shop_id", nullable = false)
    val cakeShop: CakeShop = cakeShop

    @ColumnDefault("null")
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
}
