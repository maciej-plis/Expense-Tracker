package com.matthias.fintracker.product

import com.matthias.fintracker.category.CategoryEntity
import com.matthias.fintracker.common.jpa.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.util.*


@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE product SET deleted = true where id = ?")
@Table(name = "product")
class ProductEntity : BaseEntity {

    constructor() : super()
    constructor(id: UUID?) : super(id)

    @Column(columnDefinition = "citext", nullable = false)
    lateinit var name: String

    @Column(nullable = false)
    var price: Double = 0.0

    @Column(nullable = false)
    var amount: Double = 0.0

    @ManyToOne(optional = false, fetch = EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    lateinit var category: CategoryEntity

    @Column(nullable = true)
    var description: String? = null

    @Column(nullable = false)
    var deleted: Boolean = false
}
