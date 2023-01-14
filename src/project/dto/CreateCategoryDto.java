package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCategoryDto {

    String name;
    String parentCategory;
}
