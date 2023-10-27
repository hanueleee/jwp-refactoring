package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_생성_요청;
import static kitchenpos.fixture.OrderFixture.주문상태_변경_요청;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ServiceTest;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Nested
    class 주문하기 {

        @Test
        void 주문을_할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());
            final var 주문상품 = 주문상품(후라이드메뉴, 3);

            final var request = 주문_생성_요청(테이블.getId(), List.of(주문상품));

            // when
            final var response = orderService.create(request);

            // then
            final var findOrder = orderRepository.findById(response.getId()).get();
            assertThat(findOrder.getOrderTableId()).isEqualTo(테이블.getId());
            assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            assertThat(findOrder.getOrderLineItems().size()).isEqualTo(1);
        }

        @Test
        void 존재하지_않는_메뉴면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            final var wrongMenuId = 999L;

            final var 테이블 = orderTableRepository.save(비지않은_테이블());
            final var 주문상품 = 주문상품(wrongMenuId, 후라이드메뉴, 3);

            final var request = 주문_생성_요청(테이블.getId(), List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴입니다.");
        }

        @Test
        void 주문상품이_없으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());

            final var request = 주문_생성_요청(테이블.getId(), List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품이 존재하지 않습니다.");
        }

        @Test
        void 주문상품_속_메뉴는_중복될_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());
            final var 주문상품1 = 주문상품(후라이드메뉴, 3);
            final var 주문상품2 = 주문상품(후라이드메뉴, 3);
            final var 주문상품3 = 주문상품(후라이드메뉴, 3);

            final var request = 주문_생성_요청(테이블.getId(), List.of(주문상품1, 주문상품2, 주문상품3));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 속 메뉴는 중복되면 안됩니다");
        }

        @Test
        void 테이블이_존재하지_않으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var wrongTableId = 999L;
            final var 주문상품 = 주문상품(후라이드메뉴, 3);

            final var request = 주문_생성_요청(wrongTableId, List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 테이블입니다.");
        }

        @Test
        void 테이블이_비어있으면_주문할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 빈테이블 = orderTableRepository.save(빈테이블());
            final var 주문상품 = 주문상품(후라이드메뉴, 3);

            final var request = 주문_생성_요청(빈테이블.getId(), List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블입니다.");
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());

            final var order = 주문(테이블, List.of(주문상품(후라이드메뉴, 1)));
            orderRepository.save(order);

            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when
            final var updated = orderService.changeOrderStatus(order.getId(), request);

            // then
            assertThat(updated.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }

        @Test
        void 주문이_존재하지_않을_경우_변경할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var wrongOrderId = 999L;
            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(wrongOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 주문입니다.");
        }

        @Test
        void 해당_주문이_이미_완료_상태일_경우_변경할_수_없다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());

            final var order = 주문(테이블, List.of(주문상품(후라이드메뉴, 1)));
            order.changeOrderStatus(OrderStatus.COMPLETION);
            orderRepository.save(order);

            final var request = 주문상태_변경_요청(OrderStatus.MEAL);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 완료된 주문입니다.");
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회할_수_있다() {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블 = orderTableRepository.save(비지않은_테이블());

            final var order1 = 주문(테이블, List.of(주문상품(후라이드메뉴, 1)));
            orderRepository.save(order1);

            final var order2 = 주문(테이블, List.of(주문상품(후라이드메뉴, 3)));
            orderRepository.save(order2);

            final var 주문목록 = List.of(order1, order2);
            final var expected = 주문목록.stream()
                    .map(OrderDto::toDto)
                    .collect(Collectors.toList());

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}