package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateDto;
import kitchenpos.dto.ProductDto;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductCreateDto request) {
        final Product product = request.toDomain();

        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        final Product savedProduct = productRepository.save(product);

        return ProductDto.toDto(savedProduct);
    }

    public List<ProductDto> list() {
        final List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }
}
