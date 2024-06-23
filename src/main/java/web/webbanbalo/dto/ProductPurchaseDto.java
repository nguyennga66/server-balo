package web.webbanbalo.dto;

public class ProductPurchaseDto {
    private int id;
    private int purchaseCount;

    public ProductPurchaseDto(int id, int purchaseCount) {
        this.id = id;
        this.purchaseCount = purchaseCount;
    }

    public ProductPurchaseDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
}
