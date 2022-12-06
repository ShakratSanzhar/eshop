package project.dto;

import project.entity.Product;

public class ProductDto {

    private Product product;
    private Integer quantity;

    public ProductDto() {
    }

    public ProductDto(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
