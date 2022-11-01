package kitchenpos.dto;

import java.math.BigDecimal;

public class ProductSaveRequest {

    private String name;
    private BigDecimal price;

    private ProductSaveRequest() {
    }

    public ProductSaveRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}