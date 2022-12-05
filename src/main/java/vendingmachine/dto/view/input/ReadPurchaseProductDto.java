package vendingmachine.dto.view.input;

public class ReadPurchaseProductDto {

    private final String productName;

    public ReadPurchaseProductDto(final String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
