package vendingmachine.dto.view.input;

public class ReadInsertMoneyDto {

    private final int insertMoney;

    public ReadInsertMoneyDto(final int insertMoney) {
        this.insertMoney = insertMoney;
    }

    public int getInsertMoney() {
        return insertMoney;
    }
}
