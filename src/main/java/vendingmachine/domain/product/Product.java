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

    public Product(final String productInfo) {
        String[] info = mapToWithoutFormat(productInfo);

        this.name = info[PRODUCT_NAME_INDEX];
        this.price = processProductPrice(info[PRODUCT_PRICE_INDEX]);
        this.amount = processProductAmount(info[PRODUCT_AMOUNT_INDEX]);
    }

    private int processProductPrice(final String targetPrice) {
        int productPrice = mapToNumber(targetPrice);

        if (productPrice <= 0) {
            throw new IllegalArgumentException(ProductExceptionMessage.INVALID_PRICE_VALUE.message);
        }
        Coin.validateMoney(productPrice);
        return productPrice;
    }

    private int processProductAmount(final String targetAmount) {
        int productAmount = mapToNumber(targetAmount);

        if (productAmount <= 0) {
            throw new IllegalArgumentException(ProductExceptionMessage.INVALID_AMOUNT_VALUE.message);
        }
        return productAmount;
    }

    private int mapToNumber(final String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ProductExceptionMessage.INVALID_NUMBER_FORMAT.message, e);
        }
    }

    private String[] mapToWithoutFormat(final String productInfo) {
        int lastIndex = productInfo.length() - 1;

        validateProductInfo(productInfo, lastIndex);
        return productInfo.substring(START_PRODUCT_INFO_INDEX, lastIndex).split(PRODUCT_INFO_SEPARATOR);
    }

    private void validateProductInfo(final String productInfo, final int lastIndex) {
        if (!isValidateProductInfoFormat(productInfo, lastIndex)) {
            throw new IllegalArgumentException(ProductExceptionMessage.INVALID_INFO_FORMAT.message);
        }
    }

    private boolean isValidateProductInfoFormat(final String productInfo, final int lastIndex) {
        return productInfo.charAt(0) == OPEN_BRACKET
                && productInfo.charAt(lastIndex) == CLOSE_BRACKET
                && productInfo.contains(PRODUCT_INFO_SEPARATOR);
    }

    public boolean isCanBuy(final int money) {
        return money >= price && amount > 0;
    }

    public boolean isPurchaseProduct(final String productName) {
        return this.name.equals(productName);
    }

    public int purchaseProduct(final int money) {
        if (!isCanBuy(money)) {
            throw new IllegalArgumentException(ProductExceptionMessage.INVALID_PURCHASE_PRODUCT.message);
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

    private enum ProductExceptionMessage {
        INVALID_NUMBER_FORMAT("수량 및 가격은 숫자여야 합니다."),
        INVALID_PRICE_VALUE("가격은 0보다 커야 합니다."),
        INVALID_AMOUNT_VALUE("재고는 0보다 커야 합니다."),
        INVALID_INFO_FORMAT("유효하지 않은 형식입니다."),
        INVALID_PURCHASE_PRODUCT("해당 상품은 구매할 수 없습니다.");

        private final String message;

        ProductExceptionMessage(String message) {
            this.message = message;
        }
    }
}
