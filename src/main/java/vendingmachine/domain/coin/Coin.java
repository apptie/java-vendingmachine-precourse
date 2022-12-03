package vendingmachine.domain.coin;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vendingmachine.domain.coin.exception.WrongCoinAmountException;
import vendingmachine.domain.coin.exception.WrongGeneratorException;
import vendingmachine.utils.coin.CoinGenerator;

public enum Coin {
    COIN_500(500),
    COIN_100(100),
    COIN_50(50),
    COIN_10(10);

    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("###,###원");

    private final int amount;

    Coin(final int amount) {
        this.amount = amount;
    }

    public static Map<Coin, Integer> initMachineBalance(int balanceAmount, CoinGenerator generator) {
        validateMoney(balanceAmount);
        Map<Coin, Integer> balances = new EnumMap<>(Coin.class);

        while (balanceAmount > 0) {
            Coin randomCoin = generateRandomChargeCoin(balanceAmount, generator);
            balances.put(randomCoin, balances.getOrDefault(randomCoin, 0) + 1);
            balanceAmount -= randomCoin.amount;
        }
        return Collections.unmodifiableMap(balances);
    }

    public static void validateMoney(int money) {
        if ((money % minChargeAmount()) != 0) {
            throw new IllegalArgumentException("최소 동전 단위여야 합니다.");
        }
    }

    private static Coin generateRandomChargeCoin(int chargeAmount, CoinGenerator generator) {
        Coin randomCoin = findCoin(generator.generate(getCoinsAmount()));

        while (chargeAmount - randomCoin.amount < 0) {
            randomCoin = findCoin(generator.generate(getCoinsAmount()));
        }
        return randomCoin;
    }

    private static Coin findCoin(int amount) {
        return Arrays.stream(Coin.values())
                .filter(coin -> coin.amount == amount)
                .findAny()
                .orElseThrow(WrongGeneratorException::new);
    }

    private static List<Integer> getCoinsAmount() {
        return Arrays.stream(Coin.values())
                .map(coin -> coin.amount)
                .collect(Collectors.toList());
    }

    private static int minChargeAmount() {
        return Arrays.stream(Coin.values())
                .mapToInt(coin -> coin.amount)
                .min()
                .orElseThrow(WrongCoinAmountException::new);
    }

    public boolean isCanGivenBalance(int money) {
        return money > amount;
    }

    public int calculateCoinCount(int money) {
        return money / amount;
    }

    public int calculateAfterBalanceMoney(int money) {
        return money % amount;
    }

    @Override
    public String toString() {
        return AMOUNT_FORMAT.format(amount);
    }
}
