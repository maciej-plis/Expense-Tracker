package matthias.expense_tracker.purchase

import matthias.expense_tracker.common.jpa.TransactionExecutor
import matthias.expense_tracker.openapi.model.AddEditProductRequest
import matthias.expense_tracker.openapi.model.AddEditPurchaseRequest
import matthias.expense_tracker.openapi.model.PurchaseDto
import matthias.expense_tracker.openapi.model.PurchaseItemDto
import matthias.expense_tracker.product.toEntity
import matthias.expense_tracker.shop.ShopEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
internal class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val transactionEx: TransactionExecutor
) {

    fun getPurchases(pageable: Pageable): Page<PurchaseDto> {
        return purchaseRepository.findAll(pageable).map(PurchaseEntity::toDTO)
    }

    fun getPurchaseOrThrow(purchaseId: UUID): PurchaseDto {
        return purchaseRepository.findByIdOrThrow(purchaseId).toDTO()
    }

    fun addPurchases(request: AddEditPurchaseRequest): PurchaseDto {
        val savedPurchase = transactionEx.executeInTx {
            return@executeInTx purchaseRepository.save(request.toEntity())
        }
        purchaseRepository.clearContext()
        return getPurchaseOrThrow(savedPurchase.id)
    }

    fun updatePurchase(purchaseId: UUID, request: AddEditPurchaseRequest): PurchaseDto {
        val updatedPurchase = transactionEx.executeInTx {
            purchaseRepository.deletePurchaseProducts(purchaseId)
            return@executeInTx purchaseRepository.getReferenceById(purchaseId).applyUpdate(request);
        }
        purchaseRepository.clearContext()
        return getPurchaseOrThrow(updatedPurchase.id)
    }

    fun removePurchase(purchaseId: UUID) {
        transactionEx.executeInTx {
            purchaseRepository.deleteById(purchaseId)
        }
    }

    fun removePurchases(ids: List<UUID>) {
        transactionEx.executeInTx {
            purchaseRepository.deleteAllById(ids)
        }
    }

    private fun PurchaseEntity.applyUpdate(request: AddEditPurchaseRequest): PurchaseEntity {
        val updatedProducts = request.products.map(AddEditProductRequest::toEntity)
        shop = ShopEntity(request.shopId)
        date = request.date
        products.addAll(updatedProducts)
        return this
    }

    fun getPurchaseItems(pageable: Pageable): Page<PurchaseItemDto> {
        return purchaseRepository.getPurchaseItems(pageable).map(PurchaseItemView::toDTO)
    }
}