package vendingmachine.helper.stub;

import java.util.List;
import vendingmachine.utils.coin.CoinGenerator;

public class StubCoinGenerator implements CoinGenerator {

    private final List<Integer> coins;
    private int index;

    public StubCoinGenerator(List<Integer> coins) {
        this.coins = coins;
        index = 0;
    }

    @Override
    public int generate(List<Integer> coinAmount) {
        if (index == coins.size()) {
            index = 0;
        }
        return coins.get(index++);
    }
}
