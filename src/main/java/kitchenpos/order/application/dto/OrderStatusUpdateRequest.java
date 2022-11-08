package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusUpdateRequest {

    private final String orderStatus;

    public OrderStatusUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus toOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}