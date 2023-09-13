/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.2.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package matthias.expense_tracker.openapi.api

import matthias.expense_tracker.openapi.model.AddCategoryRequest
import matthias.expense_tracker.openapi.model.CategoryDto
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.enums.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.security.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.beans.factory.annotation.Autowired


import kotlin.collections.List
import kotlin.collections.Map

@RequestMapping("\${api.base-path:}")
interface CategoriesApi {

    @Operation(
        summary = "Add new product category",
        operationId = "addProductCategory",
        description = "",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "(OK) Success response containing added product category",
                content = [Content(schema = Schema(implementation = CategoryDto::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "(BAD_REQUEST) Failure response when request body is invalid",
                content = [Content(schema = Schema(implementation = kotlin.String::class))]
            )
        ]
    )
    @RequestMapping(
        method = [RequestMethod.POST],
        value = ["/api/categories"],
        produces = ["application/json"],
        consumes = ["application/json"]
    )
    fun addProductCategory(
        @Parameter(
            description = "Request containing new product category to be added",
            required = true
        ) @RequestBody addCategoryRequest: AddCategoryRequest
    ): ResponseEntity<CategoryDto> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @Operation(
        summary = "Get list of product categories",
        operationId = "getProductCategories",
        description = "",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "(OK) Success response containing product categories",
                content = [Content(schema = Schema(implementation = CategoryDto::class))]
            )
        ]
    )
    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/api/categories"],
        produces = ["application/json"]
    )
    fun getProductCategories(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
