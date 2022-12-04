package vendingmachine.domain.product;

import java.util.Objects;
import vendingmachine.domain.coin.Coin;

public class Product {

    private static final String PRODUCT_INFO_SEPARATOR = ",";
    private static final char OPEN_BRACKET = '[';
    private static final char CLOSE_BRACKET = ']';
    private static final int START_PRODUCT_INFO_INDEX = 1;
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_AMOUNT_INDEX = 2;

    private final String name;
    private final int price;
    private int amount;

    public Product(String productInfo) {
        String[] info = mapToWithoutFormat(productInfo);

        this.name = info[PRODUCT_NAME_INDEX];
        try {
            this.price = processProductPrice(info[PRODUCT_PRICE_INDEX]);
            this.amount = processProductAmount(info[PRODUCT_AMOUNT_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("수량 및 가격은 숫자여야 합니다.", e);
        }
    }

    private int processProductPrice(String targetPrice) {
        int productPrice = mapToNumber(targetPrice);

        if (productPrice <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }
        Coin.validateMoney(productPrice);
        return productPrice;
    }

    private int processProductAmount(String targetAmount) {
        int productAmount = mapToNumber(targetAmount);

        if (productAmount <= 0) {
            throw new IllegalArgumentException("재고는 0보다 커야 합니다.");
        }
        return productAmount;
    }

    private int mapToNumber(String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("수량 및 가격은 숫자여야 합니다.", e);
        }
    }

    private String[] mapToWithoutFormat(String productInfo) {
        int lastIndex = productInfo.length() - 1;

        validateProductInfo(productInfo, lastIndex);
        return productInfo.substring(START_PRODUCT_INFO_INDEX, lastIndex).split(",");
    }

    private void validateProductInfo(String productInfo, int lastIndex) {
        if (!isValidateProductInfoFormat(productInfo, lastIndex)) {
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
    }

    private boolean isValidateProductInfoFormat(String productInfo, int lastIndex) {
        return productInfo.charAt(0) == OPEN_BRACKET
                && productInfo.charAt(lastIndex) == CLOSE_BRACKET
                && productInfo.contains(PRODUCT_INFO_SEPARATOR);
    }

    public boolean isCanBuy(int money) {
        return money >= price && amount > 0;
    }

    public boolean isPurchaseProduct(String productName) {
        return this.name.equals(productName);
    }

    public int purchaseProduct(int money) {
        if (!isCanBuy(money)) {
            throw new IllegalArgumentException("해당 상품은 구매할 수 없습니다.");
        }
        this.amount -= 1;
        return money - price;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        Product targetProduct = (Product) target;
        return Objects.equals(name, targetProduct.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
