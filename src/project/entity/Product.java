package project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private Category category;
    private String name;
    private String description;
    private String author;
    private String publisher;
    private LocalDate publishingYear;
    private String image;
    private Integer price;
    private Integer remainingAmount;
    private Integer pageCount;
    private LocalDateTime createdAt;
}
