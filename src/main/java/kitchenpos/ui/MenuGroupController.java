package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupController {
    private final MenuGroupService menuGroupService;

    public MenuGroupController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroup> create(@RequestBody MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroup created = menuGroupService.create(menuGroupCreateRequest);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }
}