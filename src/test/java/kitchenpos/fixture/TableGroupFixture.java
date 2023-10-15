package kitchenpos.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 테이블그룹(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}