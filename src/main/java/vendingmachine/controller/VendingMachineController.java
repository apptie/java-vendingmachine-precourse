package vendingmachine.controller;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import vendingmachine.domain.machine.VendingMachine;
import vendingmachine.dto.view.input.ReadInsertMoneyDto;
import vendingmachine.dto.view.input.ReadMachineBalanceDto;
import vendingmachine.dto.view.input.ReadProductsInfoDto;
import vendingmachine.dto.view.input.ReadPurchaseProductDto;
import vendingmachine.dto.view.output.PrintCustomerBalanceDto;
import vendingmachine.dto.view.output.PrintExceptionMessageDto;
import vendingmachine.dto.view.output.PrintInsertMoneyDto;
import vendingmachine.dto.view.output.PrintMachineBalanceDto;
import vendingmachine.utils.coin.CoinGenerator;
import vendingmachine.utils.coin.RandomCoinGenerator;
import vendingmachine.utils.machine.MachineStatus;
import vendingmachine.view.IOViewResolver;
import vendingmachine.view.exception.NotFoundViewException;

public class VendingMachineController {

    private final Map<MachineStatus, Supplier<MachineStatus>> vendingMachineMappings =
            new EnumMap<>(MachineStatus.class);
    private final IOViewResolver ioViewResolver;
    private VendingMachine vendingMachine;

    public VendingMachineController(final IOViewResolver ioViewResolver) {
        this.ioViewResolver = ioViewResolver;
        initVendingMachineMappings();
    }

    private void initVendingMachineMappings() {
        vendingMachineMappings.put(MachineStatus.INPUT_MACHINE_BALANCE, this::registryMachineBalance);
        vendingMachineMappings.put(MachineStatus.INPUT_PRODUCTS, this::registryProducts);
        vendingMachineMappings.put(MachineStatus.INSERT_MONEY, this::insertMoney);
        vendingMachineMappings.put(MachineStatus.PURCHASE_PRODUCT, this::purchaseProduct);
    }

    public MachineStatus process(final MachineStatus machineStatus) {
        try {
            return vendingMachineMappings.get(machineStatus).get();
        } catch (IllegalArgumentException e) {
            ioViewResolver.resolveOutputView(new PrintExceptionMessageDto(e.getMessage()));
            return machineStatus;
        } catch (NotFoundViewException | NullPointerException e) {
            System.out.println(e.getMessage());
            return MachineStatus.MACHINE_EXIT;
        }
    }

    private MachineStatus registryMachineBalance() {
        ReadMachineBalanceDto dto = ioViewResolver.resolveInputView(ReadMachineBalanceDto.class);
        CoinGenerator generator = new RandomCoinGenerator();

        vendingMachine = new VendingMachine(dto.getMachineBalance(), generator);
        ioViewResolver.resolveOutputView(new PrintMachineBalanceDto(vendingMachine.getBalance()));
        return MachineStatus.INPUT_PRODUCTS;
    }

    private MachineStatus registryProducts() {
        ReadProductsInfoDto dto = ioViewResolver.resolveInputView(ReadProductsInfoDto.class);

        vendingMachine.registryProducts(dto.getProductsInfo());
        return MachineStatus.INSERT_MONEY;
    }

    private MachineStatus insertMoney() {
        ReadInsertMoneyDto dto = ioViewResolver.resolveInputView(ReadInsertMoneyDto.class);

        vendingMachine.insertMoney(dto.getInsertMoney());
        return MachineStatus.PURCHASE_PRODUCT;
    }

    private MachineStatus purchaseProduct() {
        ioViewResolver.resolveOutputView(new PrintInsertMoneyDto(vendingMachine.getMoney()));

        if (!vendingMachine.isCanPurchaseAnything()) {
            ioViewResolver.resolveOutputView(new PrintCustomerBalanceDto(vendingMachine.calculateCustomerBalance()));
            return MachineStatus.MACHINE_EXIT;
        }

        ReadPurchaseProductDto dto = ioViewResolver.resolveInputView(ReadPurchaseProductDto.class);
        vendingMachine.purchaseProduct(dto.getProductName());
        return MachineStatus.PURCHASE_PRODUCT;
    }
}
