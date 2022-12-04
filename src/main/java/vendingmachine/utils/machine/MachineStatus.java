package vendingmachine.utils.machine;

public enum MachineStatus {
    INPUT_MACHINE_BALANCE,
    INPUT_PRODUCTS,
    INSERT_MONEY,
    PURCHASE_PRODUCT,
    MACHINE_EXIT;

    public boolean runnable() {
        return this != MACHINE_EXIT;
    }
}
