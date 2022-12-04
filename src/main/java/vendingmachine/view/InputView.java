package vendingmachine.view;

import camp.nextstep.edu.missionutils.Console;
import vendingmachine.dto.view.input.ReadInsertMoneyDto;
import vendingmachine.dto.view.input.ReadMachineBalanceDto;
import vendingmachine.dto.view.input.ReadProductsInfoDto;
import vendingmachine.dto.view.input.ReadPurchaseProductDto;

public class InputView {

    private static final String LINE_FEED = "";

    private InputView() {
    }

    private static class InputViewSingletonHelper {
        private static final InputView INPUT_VIEW = new InputView();
    }

    public static InputView getInstance() {
        return InputViewSingletonHelper.INPUT_VIEW;
    }

    public ReadMachineBalanceDto readMachineBalance() {
        print(InputViewMessage.GUIDE_MACHINE_BALANCE.message);

        try {
            int machineBalance = Integer.parseInt(processInput());
            return new ReadMachineBalanceDto(machineBalance);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("금액은 숫자 형식 또는 2147483640 이하의 값이여야 합니다.", e);
        }
    }

    public ReadProductsInfoDto readProductsInfo() {
        print(LINE_FEED);
        print(InputViewMessage.GUIDE_PRODUCTS.message);

        return new ReadProductsInfoDto(processInput());
    }

    public ReadInsertMoneyDto readInsertMoney() {
        print(LINE_FEED);
        print(InputViewMessage.GUIDE_INSERT_MONEY.message);

        try {
            int insertMoney = Integer.parseInt(processInput());
            return new ReadInsertMoneyDto(insertMoney);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("투입 금액은 숫자 형식 또는 2147483640 이하의 값이여야 합니다.", e);
        }
    }

    public ReadPurchaseProductDto readPurchaseProduct() {
        print(InputViewMessage.GUIDE_PURCHASE_PRODUCT.message);

        return new ReadPurchaseProductDto(processInput());
    }

    private String processInput() {
        return Console.readLine();
    }

    private void print(String message) {
        System.out.println(message);
    }

    private enum InputViewMessage {
        GUIDE_MACHINE_BALANCE("자판기가 보유하고 있는 금액을 입력해 주세요."),
        GUIDE_PRODUCTS("상품명과 가격, 수량을 입력해 주세요."),
        GUIDE_INSERT_MONEY("투입 금액을 입력해 주세요."),
        GUIDE_PURCHASE_PRODUCT("구매할 상품명을 입력해 주세요.");

        private final String message;

        InputViewMessage(String message) {
            this.message = message;
        }
    }
}
