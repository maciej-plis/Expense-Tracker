package matthias.expense_tracker.product

import matthias.expense_tracker.api.models.AddProductRequest
import matthias.expense_tracker.api.models.ProductDTO
import matthias.expense_tracker.api.models.UpdateProductRequest
import matthias.expense_tracker.category.CategoryEntity
import matthias.expense_tracker.category.toDTO
import java.util.*

fun ProductEntity.toDTO() = ProductDTO(
    id = id,
    category = category.toDTO(),
    name = name,
    amount = amount,
    price = price,
    description = description
)

fun AddProductRequest.toEntity(id: UUID? = null) = ProductEntity(id).also {
    it.category = CategoryEntity(categoryId)
    it.name = name
    it.amount = amount
    it.price = price
    it.description = description
}

fun UpdateProductRequest.toEntity(id: UUID? = null) = ProductEntity(id).also {
    it.category = CategoryEntity(categoryId)
    it.name = name
    it.amount = amount
    it.price = price
    it.description = description
}