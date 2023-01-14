package project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.entity.Order;
import project.entity.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDto {

    private Order order;
    private Product product;
    private Integer quantity;

}
