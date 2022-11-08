package kitchenpos.fixture.domain;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }
}