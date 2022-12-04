package vendingmachine.dto.view.input;

public class ReadMachineBalanceDto {

    private final int machineBalance;

    public ReadMachineBalanceDto(int machineBalance) {
        this.machineBalance = machineBalance;
    }

    public int getMachineBalance() {
        return machineBalance;
    }
}
