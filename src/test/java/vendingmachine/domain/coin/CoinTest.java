package vendingmachine.domain.coin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import vendingmachine.utils.coin.CoinGenerator;
import vendingmachine.utils.coin.RandomCoinGenerator;

class CoinTest {

    @Nested
    @DisplayName("initMachineBalance 메소드는")
    class DescribeInitMachineBalanceMethodTest {

        private CoinGenerator generator;

        @Nested
        @DisplayName("만약 자판기가 가지고 있는 금액 balanceAmount와 무작위 동전 생성 전략 generator가 주어지면")
        class ContextWithBalanceAmountAndGeneratorTest {

            @BeforeEach
            void initGenerator() {
                generator = new RandomCoinGenerator();
            }

            @ParameterizedTest
            @ValueSource(ints = {100, 1500, 3300, 3210})
            @DisplayName("금액만큼의 동전을 무작위로 생성해 반환한다")
            void it_returns_balances(int balanceAmount) {
                assertThatCode(() -> Coin.initMachineBalance(balanceAmount, generator))
                        .doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 balanceAmount와 유효한 generator가 주어지면")
        class ContextWithInvalidBalanceAmountAndGeneratorTest {

            @BeforeEach
            void initGenerator() {
                generator = new RandomCoinGenerator();
            }

            @ParameterizedTest
            @ValueSource(ints = {101, 203, 305, 22117, 1409})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidBalanceAmount) {
                assertThatThrownBy(() -> Coin.initMachineBalance(invalidBalanceAmount, generator))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("자판기가 보유하고 있는 금액은 최소 동전 단위여야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("validateBalanceAmount 메소드는")
    class DescribeValidateCoinAmountMethodTest {

        @Nested
        @DisplayName("동전의 최소 금액 단위의 유효한 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @ValueSource(ints = {100, 2000, 30000})
            @DisplayName("검증에 성공한다")
            void it_success_validate(int money) {
                assertThatCode(() -> Coin.validateBalanceAmount(money)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("유효하지 않은 금액 money가 주어지면")
        class ContextWithInvalidMoneyTest {

            @ParameterizedTest
            @ValueSource(ints = {101, 203, 305, 407, 509})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidMoney) {
                assertThatThrownBy(() -> Coin.validateBalanceAmount(invalidMoney))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("자판기가 보유하고 있는 금액은 최소 동전 단위여야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("isCanGivenBalance 메소드는")
    class DescribeIsCanGivenBalanceMethodTest {

        @Nested
        @DisplayName("만약 잔돈으로 거슬러줘야 하는 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "COIN_500:1000:true",
                        "COIN_500:400:false",
                        "COIN_100:200:true",
                        "COIN_100:50:false",
                        "COIN_50:100:true",
                        "COIN_50:10:false",
                        "COIN_10:20:true",
                        "COIN_10:5:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("해당 동전으로 잔돈을 거슬러줄 수 있는지 여부를 반환한다")
            void it_returns_canGivenBalance(Coin coin, int money, boolean expected) {
                boolean actual = coin.isCanGivenBalance(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("calculateCoinCount 메소드는")
    class DescribeCalculateCoinCountMethodTest {

        @Nested
        @DisplayName("만약 잔돈으로 지급해야 할 금액 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "COIN_500:1100:2",
                        "COIN_100:310:3",
                        "COIN_50:240:4",
                        "COIN_10:30:3"
                    },
                    delimiter = ':'
            )
            @DisplayName("잔돈을 지급할 때 몇 개의 동전이 필요한지 반환한다")
            void it_returns_coinCount(Coin coin, int money, int expected) {
                int actual = coin.calculateCoinCount(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("calculateAfterBalanceMoney 메소드는")
    class DescribeCalculateAfterBalanceMoneyMethodTest {

        @Nested
        @DisplayName("만약 잔돈으로 거슬러줘야 할 money를 전달하면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "COIN_500:1100:100",
                        "COIN_100:310:10",
                        "COIN_50:240:40",
                        "COIN_10:30:0"
                    },
                    delimiter = ':'
            )
            @DisplayName("잔돈으로 거슬러준 이후의 남은 금액을 반환한다")
            void it_returns_leftMoney(Coin coin, int money, int expected) {
                int actual = coin.calculateAfterBalanceMoney(money);

                assertThat(actual).isSameAs(expected);
            }
        }
    }

    @Nested
    @DisplayName("toString 메소드는")
    class DescribeToStringMethodTest {

        @Nested
        @DisplayName("만약 호출하면")
        class ContextWithtoutParameter {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "COIN_500:500원",
                        "COIN_100:100원",
                        "COIN_50:50원",
                        "COIN_10:10원"
                    },
                    delimiter = ':'
            )
            @DisplayName("동전의 금액을 반환한다")
            void it_returns_coinAmount(Coin coin, String expected) {
                String actual = coin.toString();

                assertThat(actual).contains(expected);
            }
        }
    }
}