package kitchenpos.dao.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.order.OrderLineItem;

public class FakeOrderLineItemDao implements OrderLineItemDao {

    private long id = 0L;
    private final Map<Long, OrderLineItem> orderLineItems = new HashMap<>();

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        final OrderLineItem savedOrderLineItem = new OrderLineItem(
            ++id, entity.getOrderId(), entity.getMenuId(), entity.getQuantity()
        );
        orderLineItems.put(savedOrderLineItem.getSeq(), savedOrderLineItem);
        return savedOrderLineItem;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        return Optional.ofNullable(orderLineItems.get(id));
    }

    @Override
    public List<OrderLineItem> findAll() {
        return List.copyOf(orderLineItems.values());
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems.values()) {
            if (orderLineItem.getOrderId().equals(orderId)) {
                savedOrderLineItems.add(orderLineItem);
            }
        }
        return savedOrderLineItems;
    }
}