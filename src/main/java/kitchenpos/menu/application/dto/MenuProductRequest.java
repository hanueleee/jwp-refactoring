package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(final Product product) {
        return new MenuProduct(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}