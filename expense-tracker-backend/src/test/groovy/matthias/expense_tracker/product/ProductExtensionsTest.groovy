package matthias.expense_tracker.product

import matthias.expense_tracker.api.models.AddProductRequest
import matthias.expense_tracker.api.models.UpdateProductRequest
import matthias.expense_tracker.category.CategoryEntity
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static matthias.expense_tracker.product.ProductExtensionsKt.toDTO
import static matthias.expense_tracker.product.ProductExtensionsKt.toEntity

class ProductExtensionsTest extends Specification {

    static def idFixtures = (0..9).collect { randomUUID() }

    def "Should map ProductEntity to ProductDTO"() {
        given:
            def categoryEntity = new CategoryEntity(idFixtures[0]).tap { name = "Food" }
            def productEntity = new ProductEntity(idFixtures[1]).tap {
                category = categoryEntity
                name = "Bread"
                amount = 1d
                price = 1.99d
            }

        when:
            def result = toDTO(productEntity)

        then:
            verifyAll(result) {
                id == idFixtures[1]
                verifyAll(category) {
                    id == idFixtures[0]
                    name == "Food"
                }
                name == "Bread"
                amount == 1d
                price == 1.99d
                description == null
            }
    }

    def "Should map AddProductRequest to ProductEntity"() {
        given:
            def addProductRequest = new AddProductRequest(
                idFixtures[0],
                "Bread",
                1d,
                1.99d,
                null,
                null
            )

        when:
            def result = toEntity(addProductRequest)

        then:
            verifyAll(result) {
                id != null
                category.id == idFixtures[0]
                name == "Bread"
                amount == 1d
                price == 1.99d
                description == null
            }
    }

    def "Should map UpdateProductRequest to ProductEntity"() {
        given:
            def updateProductRequest = new UpdateProductRequest(
                idFixtures[0],
                "Bread",
                1d,
                1.99d,
                idFixtures[1],
                null
            )

        when:
            def result = toEntity(updateProductRequest)

        then:
            verifyAll(result) {
                id == idFixtures[1]
                category.id == idFixtures[0]
                name == "Bread"
                amount == 1d
                price == 1.99d
                description == null
            }
    }
}
