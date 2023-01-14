package project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.entity.Cart;
import project.entity.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProductDto {

    private Cart cart;
    private Product product;
    private Integer quantity;
}
