package vendingmachine.dto.view.output;

public class PrintExceptionMessageDto {

    private final String message;

    public PrintExceptionMessageDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
