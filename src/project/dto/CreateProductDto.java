package project.dto;

import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class CreateProductDto {

    String category;
    String name;
    String description;
    String author;
    String publisher;
    String publishingYear;
    Part image;
    String price;
    String remainingAmount;
    String pageCount;
    LocalDateTime createdAt;
}
