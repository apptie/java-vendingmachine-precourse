package vendingmachine.domain.product;

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

class ProductsTest {

    @Nested
    @DisplayName("String productsInfo를 매개변수로 받는 생성자는")
    class DescribeStringMethodTest {

        @Nested
        @DisplayName("만약 유효한 등록 상품 정보를 전달하면")
        class ContextWithProductsInfoTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,500,1]", "[a,500,1];[b,500,1]"})
            @DisplayName("List<Product>를 초기화한 Products를 반환한다")
            void it_returns_products(String productsInfo) {
                assertThatCode(() -> new Products(productsInfo)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 형식의 상품 정보를 전달하면")
        class ContextWithInvalidProductsInfoFormatTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a:500:1", "[a,500,1][b,500,1"})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(String invalidProductsInfo) {
                assertThatThrownBy(() -> new Products(invalidProductsInfo))
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
                assertThatThrownBy(() -> new Products(invalidProductsInfo))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("가격은 0보다 커야 합니다.");
            }
        }

        @Nested
        @DisplayName("만약 중복된 상품 정보를 전달하면")
        class ContextWithInvalidDuplicateProductNameTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,100,1];[a,200,1]", "[a,100,1];[b,200,1];[b,200,1]"})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(String invalidProductsInfo) {
                assertThatThrownBy(() -> new Products(invalidProductsInfo))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("유효하지 않은 형식이거나 중복된 상품명입니다.");
            }
        }
    }

    @Nested
    @DisplayName("purchaseProduct 메소드는")
    class DescribePurchaseProductMethodTest {

        private Products products;

        @BeforeEach
        void initProducts() {
            this.products = new Products("[a,500,1];[b,500,1]");
        }

        @Nested
        @DisplayName("만약 유효한 구매 상품명과 투입 금액을 전달하면")
        class ContextWithProductNameAndMoneyTest {

            @Test
            @DisplayName("상품을 구매하고 남은 금액을 반환한다")
            void it_returns_leftMoney() {
                int actual = products.purchaseProduct("a", 600);

                assertThat(actual).isSameAs(100);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 구매 상품명과 투입 금액을 전달하면")
        class ContextWithInvalidProductNameAndMoneyTest {

            @Test
            @DisplayName("IllegalArgumentExceptin 예외가 발생한다")
            void it_throws_exception() {
                assertThatThrownBy(() -> products.purchaseProduct("c", 600))
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
                products.purchaseProduct("a", 1200);

                assertThatThrownBy(() -> products.purchaseProduct("a", 700))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("해당 상품은 구매할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("isCanPurchaseAnything 메소드는")
    class DescribeIsCanPurchaseAnythingMethodTest {

        private Products products;

        @BeforeEach
        void initProducts() {
            this.products = new Products("[a,500,1];[b,500,1]");
        }

        @Nested
        @DisplayName("만약 투입 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "500:true",
                        "400:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("상품 목록에서 구매할 수 있는 상품이 있는지 여부를 반환한다")
            void it_returns_canPurchase(int money, boolean expected) {
                boolean actual = products.isCanPurchaseAnything(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }
}