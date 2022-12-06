package project.dto;

import project.entity.Cart;
import project.entity.Product;

public class CartProductDto {

    private Cart cart;
    private Product product;
    private Integer quantity;

    public CartProductDto() {
    }

    public CartProductDto(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
        return "CartProductDto{" +
                "cart=" + cart +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
