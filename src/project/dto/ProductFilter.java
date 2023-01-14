package project.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductFilter {

     Integer limit;
     Integer offset;
     Integer categoryId;
     String name;
     String author;
     String publisher;
     String price;
}
