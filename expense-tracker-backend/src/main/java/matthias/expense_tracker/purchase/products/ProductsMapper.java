package matthias.expense_tracker.purchase.products;

import matthias.expense_tracker.openapi.model.ProductDto;
import matthias.expense_tracker.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ProductsMapper {

    @Mapping(target = "id", ignore = true)
    ProductEntity fromDto(ProductDto productDto);

    List<ProductEntity> fromDtos(List<ProductDto> productDtos);

    ProductDto toDto(ProductEntity productEntity);

    List<ProductDto> toDtos(List<ProductEntity> productEntities);
}
