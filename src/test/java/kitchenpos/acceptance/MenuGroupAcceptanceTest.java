package kitchenpos.acceptance;

import static kitchenpos.ui.MenuGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * 메뉴 그룹을 관리한다.
     * <p>
     * When 메뉴 그룹 생성 요청.
     * Then 메뉴 그룹이 생성 된다.
     * <p>
     * Given 메뉴 그룹이 생성 되어 있다.
     * When 메뉴 그룹 전체 조회 요청.
     * Then 전체 메뉴 그룹을 반환한다.
     */
    @DisplayName("메뉴 그룹 관리")
    @TestFactory
    Stream<DynamicTest> manageMenuGroup() throws Exception {
        // 메뉴 그룹 생성
        Long menuGroupId = createMenuGroup();
        assertThat(menuGroupId).isNotNull();

        return Stream.of(
                dynamicTest("메뉴 그룹 전체 조회", () -> {
                    List<MenuGroup> menuGroups = getAll(MenuGroup.class, API_MENU_GROUPS);
                    MenuGroup lastOrder = getLastItem(menuGroups);

                    assertThat(lastOrder.getId()).isEqualTo(menuGroupId);
                })
        );
    }

    private Long createMenuGroup() throws Exception {
        MenuGroup menuGroup = menuGroupFactory.create("추천메뉴");

        String request = objectMapper.writeValueAsString(menuGroup);
        return post(request, API_MENU_GROUPS);
    }
}
