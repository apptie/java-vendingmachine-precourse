package vendingmachine.dto.view.input;

public class ReadProductsInfoDto {

    private final String productsInfo;

    public ReadProductsInfoDto(final String productsInfo) {
        this.productsInfo = productsInfo;
    }

    public String getProductsInfo() {
        return productsInfo;
    }
}
