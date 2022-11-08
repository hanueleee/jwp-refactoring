package kitchenpos.ui;

import java.net.URI;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupController {
    private final TableGroupService tableGroupService;

    public TableGroupController(TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroup> create(@RequestBody TableGroupCreateRequest tableGroupCreateRequest) {
        final TableGroup created = tableGroupService.create(tableGroupCreateRequest);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}