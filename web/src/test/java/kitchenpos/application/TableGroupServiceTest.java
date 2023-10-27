package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static kitchenpos.fixture.TableGroupFixture.테이블그룹;
import static kitchenpos.fixture.TableGroupFixture.테이블그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성할_수_있다() {
            // given
            final var 테이블1 = orderTableRepository.save(빈테이블());
            final var 테이블2 = orderTableRepository.save(빈테이블());
            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블2.getId()));

            // when
            final var response = tableGroupService.create(request);

            // then
            assertThat(tableGroupRepository.findById(response.getId())).isPresent();
            assertThat(테이블1.getTableGroupId()).isEqualTo(response.getId());
            assertThat(테이블2.getTableGroupId()).isEqualTo(response.getId());
        }

        @Test
        void 존재하지_않는_테이블이면_생성할_수_없다() {
            // given
            final var wrongTableId = 999L;
            final var request = 테이블그룹_생성_요청(List.of(wrongTableId));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블이 포함되어 있습니다.");
        }

        @Test
        void 묶으려는_테이블이_2개미만이면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableRepository.save(빈테이블());

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("묶으려는 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 테이블이_비어있지_않으면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableRepository.save(비지않은_테이블());
            final var 테이블2 = orderTableRepository.save(빈테이블());

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블2.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블만 그룹화 할 수 있습니다.");
        }

        @Test
        void 이미_테이블_그룹을_가진_테이블이면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableRepository.save(빈테이블());
            final var 테이블2 = orderTableRepository.save(빈테이블());
            final var 테이블3 = orderTableRepository.save(빈테이블());

            final var 테이블그룹 = tableGroupRepository.save(테이블그룹());
            테이블1.group(테이블그룹.getId());
            테이블2.group(테이블그룹.getId());

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블3.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 테이블 그룹에 포함된 테이블입니다.");
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 테이블_그룹을_삭제할_수_있다() {
            // given
            final var 테이블1 = orderTableRepository.save(빈테이블());
            final var 테이블2 = orderTableRepository.save(빈테이블());

            final var 테이블그룹 = tableGroupRepository.save(테이블그룹());
            테이블1.group(테이블그룹.getId());
            테이블2.group(테이블그룹.getId());

            // when
            tableGroupService.ungroup(테이블그룹.getId());

            // then
            assertThat(orderTableRepository.findAllByTableGroupId(테이블그룹.getId())).isEmpty();
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.EXCLUDE, names = "COMPLETION")
        void 주문상태가_COMPLETION이_아니면_삭제할_수_없다(OrderStatus orderStatus) {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블1 = orderTableRepository.save(빈테이블());
            final var 테이블2 = orderTableRepository.save(빈테이블());

            final var 테이블그룹 = tableGroupRepository.save(테이블그룹());
            테이블1.group(테이블그룹.getId());
            테이블2.group(테이블그룹.getId());

            final var order = 주문(테이블1, List.of(주문상품(후라이드메뉴, 1)));
            order.changeOrderStatus(orderStatus);
            orderRepository.save(order);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("완료되지 않은 주문입니다.");
        }
    }
}