package vendingmachine.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @Nested
    @DisplayName("String productInfo를 매개변수로 받는 생성자는")
    class DescribeWithStringConstructorTest {

        @Nested
        @DisplayName("만약 유효한 productInfo가 주어지면")
        class ContextWithProductInfoTest {

            @ParameterizedTest
            @ValueSource(strings = {"[a,100,1]", "[b,200,2]", "[c,300,3]"})
            @DisplayName("name, price, amount를 초기화한 Product를 반환한다")
            void it_returns_product(String productInfo) {
                assertThatCode(() -> new Product(productInfo)).doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("isCanBuy 메소드는")
    class DescribeIsCanBuyMethodTest {

        private final Product product = new Product("[a,500,1]");

        @Nested
        @DisplayName("만약 잔돈으로 거슬러줘야 하는 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "[a,500,1]:600:true",
                        "[a,500,1]:400:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("해당 상품을 구매할 수 있는지 유무를 반환한다")
            void it_returns_canBuy(String productInfo, int money, boolean expected) {
                Product product = new Product(productInfo);

                boolean actual = product.isCanBuy(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("isPurchaseProduct 메소드는")
    class DescribeIsPurchaseProductMethodTest extends DefaultProductField {

        @Nested
        @DisplayName("만약 상품 이름 productName이 주어지면")
        class ContextWithProductNameTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "a:true",
                        "b:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("해당 상품이 구매하고자 하는 상품인지 여부를 반환한다")
            void it_returns_isPurchaseProduct(String productName, boolean expected) {
                boolean actual = product.isPurchaseProduct(productName);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("purchaseProduct 메소드는")
    class DescribePurchaseProductMethodTest extends DefaultProductField {

        @Nested
        @DisplayName("만약 상품을 구매할 수 있는 투입 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @Test
            @DisplayName("상품을 구매한 뒤 남은 금액을 반환한다")
            void it_returns_leftMoney() {
                int leftMoney = product.purchaseProduct(600);

                assertThat(leftMoney).isSameAs(100);
            }
        }

        @Nested
        @DisplayName("만약 상품을 구매할 수 없는 투입 금액 money가 주어지면")
        class ContextWithInvalidMoneyTest {

            @Test
            @DisplayName("CannotPurchaseProductException 예외가 발생한다")
            void it_throws_exception() {
                assertThatThrownBy(() -> product.purchaseProduct(400))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("해당 상품은 구매할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("equals 메소드는")
    class DescribeEqualsMethodTest extends DefaultProductField {

        @Nested
        @DisplayName("만약 Product가 주어지면")
        class ContextWithProductTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "[a,700,2]:true",
                        "[b,500,1]:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("같은 상품인지 유무를 반환한다")
            void it_returns_equals(String productInfo, boolean expected) {
                Product targetProduct = new Product(productInfo);

                boolean actual = product.equals(targetProduct);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    private class DefaultProductField {

        protected final Product product = new Product("[a,500,1]");
    }
}