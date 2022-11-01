package kitchenpos.application.dto.response;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this(
            menuGroup.getId(),
            menuGroup.getName()
        );
    }

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}