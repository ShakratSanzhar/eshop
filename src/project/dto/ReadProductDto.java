package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadProductDto {

    Long id;
    String categoryName;
    String name;
    String description;
    String author;
    String publisher;
    Integer publishingYear;
    String image;
    Integer price;
    Integer remainingAmount;
    Integer pageCount;
}
