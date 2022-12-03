package vendingmachine.utils.coin;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;

public class RandomCoinGenerator implements CoinGenerator {

    @Override
    public int generate(List<Integer> coinAmount) {
        return Randoms.pickNumberInList(coinAmount);
    }
}
