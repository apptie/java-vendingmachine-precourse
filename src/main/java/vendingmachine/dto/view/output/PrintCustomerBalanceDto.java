package vendingmachine.dto.view.output;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import vendingmachine.domain.coin.Coin;

public class PrintCustomerBalanceDto {

    private static final String BASE_MESSAGE = "%s - %d개";

    private final List<String> customerBalanceLog;

    public PrintCustomerBalanceDto(final Map<Coin, Integer> balance) {
        customerBalanceLog = Arrays.stream(Coin.values())
                .filter(coin -> !Objects.isNull(balance.get(coin)))
                .map(coin -> mapToLog(coin.toString(), balance.getOrDefault(coin, 0)))
                .collect(Collectors.toList());
    }

    private String mapToLog(final String coinAmount, final int balance) {
        return String.format(BASE_MESSAGE, coinAmount, balance);
    }

    public List<String> getCustomerBalanceLog() {
        return customerBalanceLog;
    }
}
