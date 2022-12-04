package vendingmachine.domain.machine.arguments;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import vendingmachine.domain.coin.Coin;

public final class BalanceTestArgument {

    private static final int COIN_500_INDEX = 0;
    private static final int COIN_100_INDEX = 1;
    private static final int COIN_50_INDEX = 2;
    private static final int COIN_10_INDEX = 3;

    private BalanceTestArgument() {
    }

    static Stream<Arguments> calculateBalanceTestArgument() {
        return Stream.of(
                Arguments.of(createCoins(100, 50, 10, 10, 50), 60, createBalance(createCoins(0, 0, 1, 1))),
                Arguments.of(createCoins(100, 50, 10, 10, 50), 30, createBalance(createCoins(0, 0, 0, 3)))
        );
    }

    private static List<Integer> createCoins(int... coins) {
        return IntStream.of(coins).boxed().collect(Collectors.toList());
    }

    private static Map<Coin, Integer> createBalance(List<Integer> coinCount) {
        Map<Coin, Integer> balance = new EnumMap<>(Coin.class);

        balance.put(Coin.COIN_500, coinCount.get(COIN_500_INDEX));
        balance.put(Coin.COIN_100, coinCount.get(COIN_100_INDEX));
        balance.put(Coin.COIN_50, coinCount.get(COIN_50_INDEX));
        balance.put(Coin.COIN_10, coinCount.get(COIN_10_INDEX));

        return balance;
    }
}
