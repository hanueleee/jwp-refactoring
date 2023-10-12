package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static final Product 후라이드_16000 = 상품("후라이드", 16000L);
    public static final Product 양념치킨_16000 = 상품("양념치킨", 16000L);
    public static final Product 순살치킨_16000 = 상품("순살치없킨", 16000L);

    public static Product 상품(String name, Long price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}