package project.dto;

import project.entity.Product;

public class ProductsInCartDto {

    private Product product;
    private Integer quantity;

    public ProductsInCartDto() {
    }

    public ProductsInCartDto(Product product, Integer quantity) {
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
