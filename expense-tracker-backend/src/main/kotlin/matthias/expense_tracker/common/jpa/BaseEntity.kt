package matthias.expense_tracker.common.jpa

import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(id: UUID? = null) {

    @Id
    @Column(nullable = false, unique = true)
    open var id: UUID = id ?: randomUUID()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}