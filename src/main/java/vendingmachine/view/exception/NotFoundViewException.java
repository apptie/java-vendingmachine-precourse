package vendingmachine.view.exception;

public class NotFoundViewException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "지정한 View를 찾을 수 없습니다.";

    public NotFoundViewException() {
        super(DEFAULT_MESSAGE);
    }
}
