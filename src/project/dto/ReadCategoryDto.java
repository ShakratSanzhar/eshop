package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadCategoryDto {

    Integer id;
    String name;
}
