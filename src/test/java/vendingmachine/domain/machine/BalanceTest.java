package vendingmachine.domain.machine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import vendingmachine.domain.coin.Coin;
import vendingmachine.helper.stub.StubCoinGenerator;
import vendingmachine.utils.coin.CoinGenerator;
import vendingmachine.utils.coin.RandomCoinGenerator;

class BalanceTest {

    private final CoinGenerator generator = new RandomCoinGenerator();

    @Nested
    @DisplayName("int balanceAmount, CoinGenerator generator를 매개변수로 받는 생성자는")
    class DescribeIntAndCoinGeneratorConstructorTest {

        @Nested
        @DisplayName("만약 자판기가 가지고 있는 잔금 balanceAmount, 동전 생성 전략 generator를 전달하면")
        class ContextWithBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {100, 5000, 16000})
            @DisplayName("자판기가 가지고 있는 잔금 balance와 잔금 총액 balanceAmount을 초기화한 Balance를 반환한다")
            void it_returns_balance(int balanceAmount) {
                assertThatCode(() -> new Balance(balanceAmount, generator)).doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 잔금과 유효한 동전 생성 전략을 전달하면")
        class ContextWithInvalidBalanceAmountAndGeneratorTest {

            @ParameterizedTest
            @ValueSource(ints = {101, 5003, 15005})
            @DisplayName("IllegalArgumentException 예외가 발생한다")
            void it_throws_exception(int invalidBalanceAmount) {
                assertThatThrownBy(() -> new Balance(invalidBalanceAmount, generator))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("최소 동전 단위여야 합니다.");
            }
        }
    }

    @Nested
    @DisplayName("calculateBalance 메소드는")
    class DescribeCalculateBalanceMethodTest {

        @Nested
        @DisplayName("만약 잔돈으로 거슬러줘야 하는 돈 money가 주어지면")
        class ContextWithMoneyTest {

            @ParameterizedTest
            @MethodSource("vendingmachine.domain.machine.arguments.BalanceTestArgument#calculateBalanceTestArgument")
            @DisplayName("자판기가 가지고 있는 동전의 최소 개수로 잔돈을 반환한다")
            void it_returns_balance(List<Integer> coins, int balanceAmount, Map<Coin, Integer> expected) {
                CoinGenerator generator = new StubCoinGenerator(coins);

                Balance balance = new Balance(220, generator);

                Map<Coin, Integer> actual = balance.calculateBalance(balanceAmount);

                actual.keySet().forEach(coin ->
                        assertThat(actual.get(coin)).isSameAs(expected.get(coin)));
            }
        }
    }
}