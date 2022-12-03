package vendingmachine.domain.coin.exception;

public class WrongGeneratorException extends RuntimeException {

    public WrongGeneratorException() {
        super("Generator는 자판기가 가질 수 있는 동전을 생성해야 합니다.");
    }
}
