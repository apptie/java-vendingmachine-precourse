package vendingmachine.domain.machine;

import java.util.Map;
import vendingmachine.domain.coin.Coin;
import vendingmachine.utils.coin.CoinGenerator;

public class VendingMachine {

    private final MachineInfo machineInfo;
    private int money;

    public VendingMachine(int balanceAmount, CoinGenerator generator) {
        this.machineInfo = new MachineInfo(balanceAmount, generator);
    }

    public Map<Coin, Integer> getBalance() {
        return machineInfo.getBalance();
    }

    public void insertMoney(int money) {
        Coin.validateMoney(money);

        this.money = money;
    }

    public void registryProducts(String productsInfo) {
        machineInfo.registryProducts(productsInfo);
    }

    public boolean isCanPurchaseAnything() {
        return machineInfo.isCanPurchaseAnything(money);
    }

    public void purchaseProduct(String productName) {
        money = machineInfo.purchaseProduct(productName, money);
    }

    public Map<Coin, Integer> calculateCustomerBalance() {
        return machineInfo.calculateCustomerBalance(money);
    }

    public int getMoney() {
        return money;
    }
}
