package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문상품(Menu menu, long quantity) {
        return new OrderLineItem(menu.getId(), MenuSnapShot.make(menu), quantity);
    }

    public static OrderLineItem 주문상품(final Long menuId, final Menu menu, final long quantity) {
        return new OrderLineItem(menuId, MenuSnapShot.make(menu), quantity);
    }
}