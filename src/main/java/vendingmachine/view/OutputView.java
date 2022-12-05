package vendingmachine.view;

import vendingmachine.dto.view.output.PrintCustomerBalanceDto;
import vendingmachine.dto.view.output.PrintExceptionMessageDto;
import vendingmachine.dto.view.output.PrintInsertMoneyDto;
import vendingmachine.dto.view.output.PrintMachineBalanceDto;

public class OutputView {

    private static final String LINE_FEED = "";

    private OutputView() {
    }

    private static class OutputViewSingletonHelper {
        private static final OutputView OUTPUT_VIEW = new OutputView();
    }

    public static OutputView getInstance() {
        return OutputViewSingletonHelper.OUTPUT_VIEW;
    }

    public void printMachineBalance(final PrintMachineBalanceDto dto) {
        print(LINE_FEED);
        print(OutputViewMessage.GUIDE_MACHINE_BALANCE.findFullMessage());
        dto.getCoinLog().forEach(this::print);
    }

    public void printInsertMoney(final PrintInsertMoneyDto dto) {
        print(LINE_FEED);
        print(OutputViewMessage.PRINT_INSERT_MONEY.findFullMessage(dto.getInsertMoney()));
    }

    public void printCustomerBalance(final PrintCustomerBalanceDto dto) {
        print(OutputViewMessage.GUIDE_CUSTOMER_BALANCE.findFullMessage());
        dto.getCustomerBalanceLog().forEach(this::print);
    }

    public void printExceptionMessage(final PrintExceptionMessageDto dto) {
        print(LINE_FEED);
        print(OutputViewMessage.PRINT_EXCEPTION_MESSAGE.findFullMessage(dto.getMessage()));
    }

    private void print(String message) {
        System.out.println(message);
    }

    private enum OutputViewMessage {
        GUIDE_MACHINE_BALANCE("자판기가 보유한 동전"),
        PRINT_INSERT_MONEY("투입 금액: %d원"),
        GUIDE_CUSTOMER_BALANCE("잔돈"),
        PRINT_EXCEPTION_MESSAGE("[ERROR] %s");

        private final String message;

        OutputViewMessage(final String message) {
            this.message = message;
        }

        private String findFullMessage(Object... replaces) {
            return String.format(message, replaces);
        }
    }
}
