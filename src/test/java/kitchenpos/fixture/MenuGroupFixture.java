package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final MenuGroup 메뉴그룹_두마리메뉴 = new MenuGroup("두마리메뉴");
    public static final MenuGroup 메뉴그룹_한마리메뉴 = new MenuGroup("한마리메뉴");
    public static final MenuGroup 메뉴그룹_신메뉴 = new MenuGroup("신메뉴");

    public static MenuGroup 메뉴그룹(String name) {
        return new MenuGroup(name);
    }
}
