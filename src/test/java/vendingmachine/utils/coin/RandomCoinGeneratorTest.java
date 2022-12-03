package vendingmachine.utils.coin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;

class RandomCoinGeneratorTest {

    @Nested
    @DisplayName("generate 메소드는")
    class DescribeGenerateMethodTest {

        @Nested
        @DisplayName("만약 무작위로 선택할 동전 금액 List<Integer>가 주어지면")
        class ContextWithListTest {

            @RepeatedTest(10)
            @DisplayName("무작위 동전 금액을 반환한다")
            void it_returns_coinAmount() {
                List<Integer> coinAmount = Arrays.asList(500, 100, 50, 10);
                CoinGenerator generator = new RandomCoinGenerator();

                int actual = generator.generate(coinAmount);

                assertThat(coinAmount.contains(actual)).isTrue();
            }
        }
    }
}