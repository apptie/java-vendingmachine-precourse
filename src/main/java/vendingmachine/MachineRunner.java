package vendingmachine;

import vendingmachine.controller.VendingMachineController;
import vendingmachine.utils.machine.MachineStatus;
import vendingmachine.view.IOViewResolver;
import vendingmachine.view.InputView;
import vendingmachine.view.OutputView;

public final class MachineRunner {

    private MachineRunner() {
    }

    public static void run() {
        IOViewResolver ioViewResolver = new IOViewResolver(InputView.getInstance(), OutputView.getInstance());
        VendingMachineController controller = new VendingMachineController(ioViewResolver);

        MachineStatus machineStatus = MachineStatus.INPUT_MACHINE_BALANCE;
        while (machineStatus.runnable()) {
            machineStatus = controller.process(machineStatus);
        }
    }
}
