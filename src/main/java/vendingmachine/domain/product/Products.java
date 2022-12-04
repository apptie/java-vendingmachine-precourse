package vendingmachine.domain.product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import vendingmachine.domain.product.exception.CannotPurchaseAnyProductException;

public class Products {

    private static final String PRODUCTS_SEPARATOR = ";";

    private final List<Product> products;

    public Products(String productsInfo) {
        String[] infos = productsInfo.split(PRODUCTS_SEPARATOR);

        this.products = Arrays.stream(infos)
            .map(Product::new)
            .collect(Collectors.toList());

        validateProductsCount(infos);
    }

    private void validateProductsCount(String[] info) {
        if (info.length != products.size()) {
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
    }

    public int purchaseProduct(String productName, int money) {
        if (!isCanBuyAnything(money)) {
            throw new CannotPurchaseAnyProductException();
        }

        Product target = products.stream()
                .filter(product -> product.isPurchaseProduct(productName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품명입니다."));

        return target.purchaseProduct(money);
    }

    private boolean isCanBuyAnything(int money) {
        return products.stream().anyMatch(product -> product.isCanBuy(money));
    }
}
