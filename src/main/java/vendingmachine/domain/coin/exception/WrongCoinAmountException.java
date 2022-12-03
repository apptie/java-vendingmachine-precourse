package vendingmachine.domain.coin.exception;

public class WrongCoinAmountException extends RuntimeException {

    public WrongCoinAmountException() {
        super("자판기가 가질 수 있는 동전은 중복될 수 없습니다.");
    }
}
