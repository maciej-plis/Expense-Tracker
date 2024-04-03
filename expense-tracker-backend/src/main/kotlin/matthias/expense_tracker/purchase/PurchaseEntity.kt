package matthias.expense_tracker.purchase

import jakarta.persistence.*
import jakarta.persistence.CascadeType.*
import jakarta.persistence.FetchType.EAGER
import matthias.expense_tracker.common.jpa.AuditEntity
import matthias.expense_tracker.product.ProductEntity
import matthias.expense_tracker.shop.ShopEntity
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode.SUBSELECT
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "purchase")
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE purchase SET deleted = true where id = ?")
class PurchaseEntity : AuditEntity {

    constructor() : super()
    constructor(id: UUID?) : super(id)

    @Column(nullable = false)
    lateinit var date: LocalDate

    @ManyToOne(optional = false, fetch = EAGER)
    @JoinColumn(name = "shop_id", nullable = false)
    lateinit var shop: ShopEntity

    @Fetch(SUBSELECT)
    @OneToMany(cascade = [PERSIST, MERGE, REFRESH, DETACH], orphanRemoval = false)
    @JoinColumn(name = "purchase_id", nullable = false, updatable = false)
    @OrderColumn(columnDefinition = "int2", name = "ordinal", nullable = false)
    lateinit var products: MutableList<ProductEntity>

    @Column(nullable = false)
    var deleted: Boolean = false
}
