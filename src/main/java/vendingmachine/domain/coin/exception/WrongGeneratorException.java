package vendingmachine.domain.coin.exception;

public class WrongGeneratorException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Generator는 자판기가 가질 수 있는 동전을 생성해야 합니다.";

    public WrongGeneratorException() {
        super(DEFAULT_MESSAGE);
    }
}
