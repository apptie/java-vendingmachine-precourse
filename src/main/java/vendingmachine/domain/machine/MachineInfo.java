package vendingmachine.domain.machine;

import java.util.Map;
import vendingmachine.domain.coin.Coin;
import vendingmachine.domain.product.Products;
import vendingmachine.utils.coin.CoinGenerator;

public class MachineInfo {

    private final Balance balance;
    private Products products;

    public MachineInfo(final int balanceAmount, final CoinGenerator generator) {
        this.balance = new Balance(balanceAmount, generator);
    }

    public void registryProducts(final String productInput) {
        products = new Products(productInput);
    }

    public int purchaseProduct(final String productName, final int money) {
        return products.purchaseProduct(productName, money);
    }

    public boolean isCanPurchaseAnything(final int money) {
        return products.isCanPurchaseAnything(money);
    }

    public Map<Coin, Integer> calculateCustomerBalance(final int balanceAmount) {
        return balance.calculateBalance(balanceAmount);
    }

    public Map<Coin, Integer> getBalance() {
        return balance.getBalance();
    }
}
