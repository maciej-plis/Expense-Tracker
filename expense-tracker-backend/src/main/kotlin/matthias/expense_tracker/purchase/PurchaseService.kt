package matthias.expense_tracker.purchase

import matthias.expense_tracker.api.models.AddPurchaseRequest
import matthias.expense_tracker.api.models.PurchaseDTO
import matthias.expense_tracker.api.models.UpdatePurchaseRequest
import matthias.expense_tracker.common.jpa.TransactionExecutor
import matthias.expense_tracker.product.toEntity
import matthias.expense_tracker.shop.ShopEntity
import org.springframework.stereotype.Service
import java.util.*


@Service
internal class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val txExecutor: TransactionExecutor
) {

    fun getPurchaseOrThrow(purchaseId: UUID): PurchaseDTO = txExecutor.readTx {
        return@readTx purchaseRepository.findByIdOrThrow(purchaseId).toDTO()
    }

    fun addPurchase(request: AddPurchaseRequest): UUID = txExecutor.tx {
        val purchaseEntity = purchaseRepository.save(request.toEntity())
        return@tx purchaseEntity.id
    }

    fun updatePurchase(purchaseId: UUID, request: UpdatePurchaseRequest): Unit = txExecutor.tx {
        purchaseRepository.getReferenceById(purchaseId).update(request)
    }

    fun removePurchase(purchaseId: UUID): Unit = txExecutor.tx {
        purchaseRepository.deleteById(purchaseId)
    }

    fun removePurchases(ids: List<UUID>): Unit = txExecutor.tx {
        purchaseRepository.deleteAllById(ids)
    }

    private fun PurchaseEntity.update(request: UpdatePurchaseRequest) {
        val productRequestMap = request.products.associateTo(mutableMapOf()) { it.id to it.toEntity() }
        shop = ShopEntity(request.shopId)
        date = request.date
        products.removeAll { it.id !in productRequestMap.keys }
        products.forEach {
            val productRequest = productRequestMap.remove(it.id)!!
            it.name = productRequest.name
            it.price = productRequest.price
            it.amount = productRequest.amount
            it.category = productRequest.category
            it.description = productRequest.description
        }
        products.addAll(productRequestMap.values)
    }
}
