package vendingmachine.utils.coin;

import java.util.List;

@FunctionalInterface
public interface CoinGenerator {

    int generate(List<Integer> coinAmount);
}
