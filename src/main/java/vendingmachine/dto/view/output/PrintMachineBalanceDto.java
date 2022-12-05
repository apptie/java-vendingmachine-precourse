package vendingmachine.dto.view.output;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vendingmachine.domain.coin.Coin;

public class PrintMachineBalanceDto {

    private static final String BASE_MESSAGE = "%s - %d개";

    private final List<String> coinLog;

    public PrintMachineBalanceDto(final Map<Coin, Integer> balance) {
        coinLog = Arrays.stream(Coin.values())
                .map(coin -> mapToLog(coin.toString(), balance.getOrDefault(coin, 0)))
                .collect(Collectors.toList());
    }

    private String mapToLog(final String coinAmount, final int balance) {
        return String.format(BASE_MESSAGE, coinAmount, balance);
    }

    public List<String> getCoinLog() {
        return coinLog;
    }
}
