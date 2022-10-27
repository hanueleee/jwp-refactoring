package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup savedMenuGroup) {
        return new MenuGroupResponse(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}