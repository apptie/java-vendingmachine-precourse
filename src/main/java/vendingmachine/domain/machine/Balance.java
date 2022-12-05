package vendingmachine.domain.machine;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vendingmachine.domain.coin.Coin;
import vendingmachine.utils.coin.CoinGenerator;

public class Balance {

    private final Map<Coin, Integer> balance;
    private final int balanceAmount;

    public Balance(final int balanceAmount, final CoinGenerator generator) {
        this.balance = Coin.initMachineBalance(balanceAmount, generator);
        this.balanceAmount = balanceAmount;
    }

    public Map<Coin, Integer> calculateBalance(final int money) {
        if (money >= balanceAmount) {
            return balance;
        }

        List<Coin> balanceCoin = Arrays.stream(Coin.values())
                .filter(coin -> balance.getOrDefault(coin, 0) > 0)
                .collect(Collectors.toList());

        return calculateCustomerBalance(money, balanceCoin);
    }

    private Map<Coin, Integer> calculateCustomerBalance(int money, List<Coin> balanceCoin) {
        Map<Coin, Integer> customerBalance = new EnumMap<>(Coin.class);

        for (Coin coin : balanceCoin) {
            customerBalance.put(coin, coin.calculateCoinCount(money));
            money = coin.calculateAfterBalanceMoney(money);
        }

        return customerBalance;
    }

    public Map<Coin, Integer> getBalance() {
        return balance;
    }
}
