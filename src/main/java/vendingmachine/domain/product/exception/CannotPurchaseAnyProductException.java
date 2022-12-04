package vendingmachine.domain.product.exception;

public class CannotPurchaseAnyProductException extends RuntimeException {

    public CannotPurchaseAnyProductException() {
        super("어떠한 상품도 구매할 수 없습니다.");
    }
}
