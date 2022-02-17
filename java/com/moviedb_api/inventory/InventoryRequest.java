package com.moviedb_api.inventory;

public class InventoryRequest {
    String productId;

    String status;

    Integer qty;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public Integer getQuantity() {
        return qty;
    }

    public String getProductId() {
        return productId;
    }
}
