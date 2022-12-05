package vendingmachine.utils.machine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MachineStatusTest {

    @Nested
    @DisplayName("runnable 메소드는")
    class DescribeRunnableMethodTest {

        @Nested
        @DisplayName("만약 호출하면")
        class ContextWithoutParameterTest {

            @ParameterizedTest
            @CsvSource(
                    value = {
                        "INPUT_MACHINE_BALANCE:true",
                        "INPUT_PRODUCTS:true",
                        "INSERT_MONEY:true",
                        "PURCHASE_PRODUCT:true",
                        "MACHINE_EXIT:false"
                    },
                    delimiter = ':'
            )
            @DisplayName("자판기가 계속 실행되는지 여부를 반환한다")
            void it_returns_runnable(MachineStatus machineStatus, boolean expected) {
                boolean actual = machineStatus.runnable();

                assertThat(actual).isSameAs(expected);
            }
        }
    }
}