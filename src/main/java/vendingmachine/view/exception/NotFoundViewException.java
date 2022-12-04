package vendingmachine.view.exception;

public class NotFoundViewException extends RuntimeException {

    public NotFoundViewException() {
        super("지정한 View를 찾을 수 없습니다.");
    }
}
