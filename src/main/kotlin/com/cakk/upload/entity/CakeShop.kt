package com.cakk.upload.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point
import java.time.LocalDateTime

@Entity
@Table(name = "cake_shop")
class CakeShop(
    shopName: String,
    thumbnailUrl: String?,
    shopAddress: String?,
    shopBio: String?,
    shopDescription: String?,
    location: Point
) : AuditEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id", nullable = false)
    val id: Long? = null

    @Column(name = "thumbnail_url", length = 200)
    var thumbnailUrl: String? = thumbnailUrl

    @Column(name = "shop_name", length = 30, nullable = false)
    var shopName: String = shopName

    @Column(name = "shop_address", length = 50)
    var shopAddress: String? = shopAddress

    @Column(name = "shop_bio", length = 40)
    var shopBio: String? = shopBio

    @Column(name = "shop_description", length = 500)
    var shopDescription: String? = shopDescription

    @Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
    var location: Point = location

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false, columnDefinition = "MEDIUMINT")
    var likeCount: Int = 0

    @ColumnDefault("0")
    @Column(name = "heart_count", nullable = false, columnDefinition = "MEDIUMINT")
    var heartCount: Int = 0

    @ColumnDefault("false")
    @Column(name = "linked_flag", nullable = false, columnDefinition = "TINYINT(1)")
    var linkedFlag: Boolean = false

    @ColumnDefault("null")
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    @OneToMany(mappedBy = "cakeShop", cascade = [CascadeType.PERSIST])
    val cakes: HashSet<Cake> = HashSet();

    fun updateThumbnailUrl(thumbnailUrl: String) {
        this.thumbnailUrl = thumbnailUrl
    }

    fun addCakes(cakeUrls: List<String>) {
        cakeUrls.forEach {
            val cake = Cake(it, 0, this)
            cakes.add(cake)
        }
    }
}
