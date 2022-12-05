package vendingmachine.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vendingmachine.dto.view.input.ReadInsertMoneyDto;
import vendingmachine.dto.view.input.ReadMachineBalanceDto;
import vendingmachine.dto.view.input.ReadProductsInfoDto;
import vendingmachine.dto.view.input.ReadPurchaseProductDto;
import vendingmachine.dto.view.output.PrintCustomerBalanceDto;
import vendingmachine.dto.view.output.PrintExceptionMessageDto;
import vendingmachine.dto.view.output.PrintInsertMoneyDto;
import vendingmachine.dto.view.output.PrintMachineBalanceDto;
import vendingmachine.view.exception.NotFoundViewException;

public class IOViewResolver {

    private final Map<Class<?>, Consumer<Object>> outputViewMappings = new HashMap<>();
    private final Map<Class<?>, Supplier<Object>> inputViewMappings = new HashMap<>();

    public IOViewResolver(final InputView inputView, final OutputView outputView) {
        initInputViewMappings(inputView);
        initOutputViewMappings(outputView);
    }

    private void initInputViewMappings(final InputView inputView) {
        inputViewMappings.put(ReadInsertMoneyDto.class, inputView::readInsertMoney);
        inputViewMappings.put(ReadMachineBalanceDto.class, inputView::readMachineBalance);
        inputViewMappings.put(ReadProductsInfoDto.class, inputView::readProductsInfo);
        inputViewMappings.put(ReadPurchaseProductDto.class, inputView::readPurchaseProduct);
    }

    private void initOutputViewMappings(final OutputView outputView) {
        outputViewMappings.put(PrintCustomerBalanceDto.class, dto ->
                outputView.printCustomerBalance((PrintCustomerBalanceDto) dto));
        outputViewMappings.put(PrintInsertMoneyDto.class, dto ->
                outputView.printInsertMoney((PrintInsertMoneyDto) dto));
        outputViewMappings.put(PrintMachineBalanceDto.class, dto ->
                outputView.printMachineBalance((PrintMachineBalanceDto) dto));
        outputViewMappings.put(PrintExceptionMessageDto.class, dto ->
                outputView.printExceptionMessage((PrintExceptionMessageDto) dto));
    }

    public <T> T resolveInputView(final Class<T> type) {
        try {
            return type.cast(inputViewMappings.get(type).get());
        } catch (NullPointerException e) {
            throw new NotFoundViewException();
        }
    }

    public void resolveOutputView(final Object dto) {
        try {
            outputViewMappings.get(dto.getClass()).accept(dto);
        } catch (NullPointerException e) {
            throw new NotFoundViewException();
        }
    }
}
