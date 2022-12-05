package vendingmachine.domain.machine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import vendingmachine.utils.coin.CoinGenerator;
import vendingmachine.utils.coin.RandomCoinGenerator;

class VendingMachineTest {

    private final CoinGenerator generator = new RandomCoinGenerator();

    @Nested
    @DisplayName("int balanceAmount, CoinGenerator generator를 매개변수로 받는 생성자는")
    class DescribeWithIntCoinGeneratorConstructorTest {

        @Nested
        @DisplayName("만약 자판기가 가지고 있는 잔금 balanceAmount, 동전 생성 전략 generator를 전달하면")
        class ContextWithBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {100, 5000, 16000})
            @DisplayName("자판기가 가지고 있는 잔금 balance와 잔금 총액 balanceAmount을 초기화한 Balance를 반환한다")
            void it_returns_balance(int balanceAmount) {
                assertThatCode(() -> new VendingMachine(balanceAmount, generator)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 잔금과 유효한 동전 생성 전략을 전달하면")
        class ContextWithInvalidBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {101, 5003, 15005})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidBalanceAmount) {
                assertThatThrownBy(() -> new VendingMachine(invalidBalanceAmount, generator))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("최소 동전 단위여야 합니다.");
            }
        }

        @Nested
        @DisplayName("만약 금액이 0 이하인 유효하지 않은 잔금과 유효한 동전 생성 전략을 전달하면")
        class ContextWithInvalidLessThanZeroBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {0, -100})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidBalanceAmount) {
                assertThatThrownBy(() -> new VendingMachine(invalidBalanceAmount, generator))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("금액은 0 이상의 값이여야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("insertMoney 메소드는")
    class DescribeInsertMoneyMethodTest extends DefaultVendingMachineField {

        @Nested
        @DisplayName("만약 유효한 투입 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @ValueSource(ints = {100, 5000, 10000})
            @DisplayName("투입 금액 필드 money를 초기화한다")
            void it_init_money(int money) {
                assertThatCode(() -> vendingMachine.insertMoney(money)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 최소 동전 단위가 아닌 유효하지 않은 투입 금액 money가 주어지면")
        class ContextWithInvalidCoinAmountMoneyTest {

            @ParameterizedTest
            @ValueSource(ints = {101, 5003, 10005})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidMoney) {
                assertThatThrownBy(() -> vendingMachine.insertMoney(invalidMoney))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("금액은 최소 동전 단위여야 합니다.");
            }
        }

        @Nested
        @DisplayName("만약 0 이하의 유효하지 않은 투입 금액 money가 주어지면")
        class ContextWithInvalidLessThanZeroMoneyTest {

            @ParameterizedTest
            @ValueSource(ints = {0, -100})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidMoney) {
                assertThatThrownBy(() -> vendingMachine.insertMoney(invalidMoney))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("금액은 0 이상의 값이여야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("registryProducts 메소드는")
    class DescribeRegistryProductsMethodTest extends DefaultVendingMachineField {

        @Nested
        @DisplayName("만약 유효한 등록 상품 정보를 전달하면")
        class ContextWithProductsInfoTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,500,1]", "[a,500,1];[b,500,1]"})
            @DisplayName("Products를 초기화한다")
            void it_init_products(String productsInfo) {
                assertThatCode(() -> vendingMachine.registryProducts(productsInfo)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 형식의 상품 정보를 전달하면")
        class ContextWithInvalidProductsInfoFormatTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a:500:1", "[a,500,1][b,500,1"})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(String invalidProductsInfo) {
                assertThatThrownBy(() -> vendingMachine.registryProducts(invalidProductsInfo))
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
                assertThatThrownBy(() -> vendingMachine.registryProducts(invalidProductsInfo))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("가격은 0보다 커야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("isCanPurchaseAnything 메소드는")
    class DescribeIsCanPurchaseAnythingMethodTest extends DefaultVendingMachineField {

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
                vendingMachine.insertMoney(money);
                vendingMachine.registryProducts("[a,500,1];[b,500,1]");
                boolean actual = vendingMachine.isCanPurchaseAnything();

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("purchaseProduct 메소드는")
    class DescribePurchaseProductMethodTest {

        private VendingMachine vendingMachine;

        @BeforeEach
        void initVendingMachine() {
            vendingMachine = new VendingMachine(1000, generator);
            vendingMachine.insertMoney(600);
            vendingMachine.registryProducts("[a,500,1];[b,500,1]");
        }

        @Nested
        @DisplayName("만약 유효한 구매 상품명과 투입 금액을 전달하면")
        class ContextWithProductNameAndMoneyTest {

            @Test
            @DisplayName("상품을 구매한다")
            void it_process_purchaseProduct() {
                assertThatCode(() -> vendingMachine.purchaseProduct("a")).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 구매 상품명과 투입 금액을 전달하면")
        class ContextWithInvalidProductNameAndMoneyTest {

            @Test
            @DisplayName("IllegalArgumentExceptin 예외가 발생한다")
            void it_throws_exception() {
                assertThatThrownBy(() -> vendingMachine.purchaseProduct("c"))
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
                vendingMachine.purchaseProduct("a");

                assertThatThrownBy(() -> vendingMachine.purchaseProduct("a"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("해당 상품은 구매할 수 없습니다.");
            }
        }
    }

    private class DefaultVendingMachineField {

        protected final VendingMachine vendingMachine = new VendingMachine(10000, generator);
    }
}