package kitchenpos;

import static kitchenpos.OrderTableFixtures.createOrderTable;
import static kitchenpos.OrderTableFixtures.createOrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    private TableGroupFixtures() {
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(null, LocalDateTime.now(), List.of(createOrderTable(), createOrderTable()));
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDateTime, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDateTime, orderTables);
    }

    public static TableGroupResponse createTableGroupResponse() {
        return new TableGroupResponse(
                1L,
                LocalDateTime.now(),
                List.of(createOrderTableResponse(), createOrderTableResponse())
        );
    }
}