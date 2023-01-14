package project.mapper;

import project.dao.CategoryDao;
import project.dto.CreateProductDto;
import project.entity.Product;
import project.util.LocalDateFormatter;

public class CreateProductMapper implements Mapper<CreateProductDto, Product> {

    private static final CreateProductMapper INSTANCE = new CreateProductMapper();
    private static final String IMAGE_FOLDER = "products/";
    private final CategoryDao categoryDao = CategoryDao.getInstance();

    @Override
    public Product mapFrom(CreateProductDto object) {
        var categoryEntity = categoryDao.findAll().stream()
                .filter(category -> category.getName().equals(object.getCategory()))
                .findFirst();
        return Product.builder()
                .category(categoryEntity.get())
                .name(object.getName())
                .description(object.getDescription())
                .author(object.getAuthor())
                .publisher(object.getPublisher())
                .publishingYear(LocalDateFormatter.format(object.getPublishingYear()))
                .image(IMAGE_FOLDER + object.getImage().getSubmittedFileName())
                .price(Integer.valueOf(object.getPrice()))
                .remainingAmount(Integer.valueOf(object.getRemainingAmount()))
                .pageCount(Integer.valueOf(object.getPageCount()))
                .createdAt(object.getCreatedAt())
                .build();
    }

    public static CreateProductMapper getInstance() {
        return INSTANCE;
    }
}
