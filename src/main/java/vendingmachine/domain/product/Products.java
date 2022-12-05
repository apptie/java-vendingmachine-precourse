package vendingmachine.domain.product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Products {

    private static final String PRODUCTS_SEPARATOR = ";";

    private final List<Product> products;

    public Products(final String productsInfo) {
        String[] infos = productsInfo.split(PRODUCTS_SEPARATOR);

        this.products = Arrays.stream(infos)
            .map(Product::new)
            .distinct()
            .collect(Collectors.toList());

        validateProductsCount(infos);
    }

    private void validateProductsCount(final String[] info) {
        if (info.length != products.size()) {
            throw new IllegalArgumentException(ProductsExceptionMessage.INVALID_PRODUCTS_SIZE.message);
        }
    }

    public int purchaseProduct(final String productName, final int money) {
        Product target = products.stream()
                .filter(product -> product.isPurchaseProduct(productName))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException(ProductsExceptionMessage.INVALID_PRODUCT_NAME.message));

        return target.purchaseProduct(money);
    }

    public boolean isCanPurchaseAnything(final int money) {
        return products.stream().anyMatch(product -> product.isCanBuy(money));
    }

    private enum ProductsExceptionMessage {
        INVALID_PRODUCTS_SIZE("유효하지 않은 형식이거나 중복된 상품명입니다."),
        INVALID_PRODUCT_NAME("존재하지 않는 상품명입니다.");

        private final String message;

        ProductsExceptionMessage(String message) {
            this.message = message;
        }
    }
}
