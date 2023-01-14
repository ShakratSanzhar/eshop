package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCartDto {

    Object user;
    Integer price;
}
