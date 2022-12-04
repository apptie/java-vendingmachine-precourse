package vendingmachine.domain.machine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import vendingmachine.domain.coin.Coin;
import vendingmachine.domain.product.Products;
import vendingmachine.helper.stub.StubCoinGenerator;
import vendingmachine.utils.coin.CoinGenerator;
import vendingmachine.utils.coin.RandomCoinGenerator;

class MachineInfoTest {

    private final CoinGenerator generator = new RandomCoinGenerator();

    @Nested
    @DisplayName("int chargeAmount, CoinGenerator generator를 매개변수로 받는 생성자는")
    class DescribeIntAndCoinGeneratorMethodTest {

        @Nested
        @DisplayName("만약 자판기가 가지고 있는 잔금 balanceAmount, 동전 생성 전략 generator를 전달하면")
        class ContextWithBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {100, 5000, 16000})
            @DisplayName("자판기가 가지고 있는 잔금 balance와 잔금 총액 balanceAmount을 초기화한 Balance를 반환한다")
            void it_returns_balance(int balanceAmount) {
                assertThatCode(() -> new MachineInfo(balanceAmount, generator)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 잔금과 유효한 동전 생성 전략을 전달하면")
        class ContextWithInvalidBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {101, 5003, 15005})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidBalanceAmount) {
                assertThatThrownBy(() -> new MachineInfo(invalidBalanceAmount, generator))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("최소 동전 단위여야 합니다.");
            }
        }
    }

    private final MachineInfo machineInfo = new MachineInfo(10000, generator);

    @Nested
    @DisplayName("registryProducts 메소드는")
    class DescribeRegistryProductsMethodTest {

        @Nested
        @DisplayName("만약 유효한 등록 상품 정보를 전달하면")
        class ContextWithProductsInfoTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,500,1]", "[a,500,1];[b,500,1]"})
            @DisplayName("Products를 초기화한다")
            void it_init_products(String productsInfo) {
                assertThatCode(() -> machineInfo.registryProducts(productsInfo)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 형식의 상품 정보를 전달하면")
        class ContextWithInvalidProductsInfoFormatTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a:500:1", "[a,500,1][b,500,1"})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(String invalidProductsInfo) {
                assertThatThrownBy(() -> machineInfo.registryProducts(invalidProductsInfo))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("유효하지 않은 형식입니다.");
            }
        }

        @Nested
        @DisplayName("만약 0 이하의 가격을 가진 상품 정보를 전달하면")
        class ContextWithInvalidPriceOrAmountTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,0,1]", "[a,-100,1]"})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(String invalidProductsInfo) {
                assertThatThrownBy(() -> machineInfo.registryProducts(invalidProductsInfo))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("가격은 0보다 커야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("purchaseProduct 메소드는")
    class DescribePurchaseProductMethodTest {

        private MachineInfo machineInfo;

        @BeforeEach
        void init() {
            machineInfo = new MachineInfo(10000, generator);
            machineInfo.registryProducts("[a,500,1];[b,500,1]");
        }

        @Nested
        @DisplayName("만약 유효한 구매 상품명과 투입 금액을 전달하면")
        class ContextWithProductNameAndMoneyTest {

            @Test
            @DisplayName("상품을 구매하고 남은 금액을 반환한다")
            void it_returns_leftMoney() {
                int actual = machineInfo.purchaseProduct("a", 600);

                assertThat(actual).isSameAs(100);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 구매 상품명과 투입 금액을 전달하면")
        class ContextWithInvalidProductNameAndMoneyTest {

            @Test
            @DisplayName("IllegalArgumentExceptin 예외가 발생한다")
            void it_throws_exception() {
                assertThatThrownBy(() -> machineInfo.purchaseProduct("c", 600))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("존재하지 않는 상품명입니다.");
            }
        }

        @Nested
        @DisplayName("만약 구매하고자 하는 상품의 재고가 없다면")
        class ContextWhenInvalidProductAmountTest {

            @Test
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception() {
                machineInfo.purchaseProduct("a", 1200);

                assertThatThrownBy(() -> machineInfo.purchaseProduct("a", 700))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("해당 상품은 구매할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("isCanPurchaseAnything 메소드는")
    class DescribeIsCanPurchaseAnythingMethodTest {

        private MachineInfo machineInfo;

        @BeforeEach
        void init() {
            machineInfo = new MachineInfo(10000, generator);
            machineInfo.registryProducts("[a,500,1];[b,500,1]");
        }

        @Nested
        @DisplayName("만약 투입 금액을 전달하면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "500:true",
                        "400:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("구매할 수 있는 상품이 존재하는지 여부를 반환한다")
            void it_returns_canPurchase(int money, boolean expected) {
                boolean actual = machineInfo.isCanPurchaseAnything(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("calculateCustomerBalance 메소드는")
    class DescribeCalculateCustomerBalanceMethodTest {

        @Nested
        @DisplayName("만약 투입 금액 balanceAmount가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @MethodSource("vendingmachine.domain.machine.arguments.BalanceTestArgument#calculateBalanceTestArgument")
            @DisplayName("자판기가 가지고 있는 동전의 최소 개수로 잔돈을 반환한다")
            void it_returns_balance(List<Integer> coins, int balanceAmount, Map<Coin, Integer> expected) {
                CoinGenerator generator = new StubCoinGenerator(coins);

                MachineInfo machineInfo = new MachineInfo(220, generator);

                Map<Coin, Integer> actual = machineInfo.calculateCustomerBalance(balanceAmount);

                actual.keySet().forEach(coin ->
                        assertThat(actual.get(coin)).isSameAs(expected.get(coin)));
            }
        }
    }

    /*

    @Nested
    @DisplayName("만약 유효한 구매 상품명 어떠한 상품도 구매할 수 없는 투입 금액을 전달하면")
    class ContextWithProductNameAndInvalidMoneyTest {

        @Test
        @DisplayName("CannotPurchaseAnyProductException 예외가 발생한다")
        void it_throws_exception() {
            assertThatThrownBy(() -> products.purchaseProduct("a", 100))
                .isInstanceOf(CannotPurchaseAnyProductException.class)
                .hasMessageContaining("어떠한 상품도 구매할 수 없습니다.");
        }
    }

     */
}