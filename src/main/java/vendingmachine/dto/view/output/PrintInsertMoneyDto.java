package vendingmachine.dto.view.output;

public class PrintInsertMoneyDto {

    private final int insertMoney;

    public PrintInsertMoneyDto(int insertMoney) {
        this.insertMoney = insertMoney;
    }

    public int getInsertMoney() {
        return insertMoney;
    }
}
