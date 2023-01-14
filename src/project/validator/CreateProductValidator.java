package project.validator;

import project.dao.ProductDao;
import project.dto.CreateProductDto;
import project.util.LocalDateFormatter;

public class CreateProductValidator implements Validator<CreateProductDto> {

    private static final CreateProductValidator INSTANCE = new CreateProductValidator();
    private final ProductDao productDao = ProductDao.getInstance();

    public static CreateProductValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(CreateProductDto object) {
        var validationResult = new ValidationResult();
        if (object.getCategory().equals("")) {
            validationResult.add(Error.of("invalid.category", "Category is invalid"));
        }
        if (object.getName().equals("") || !isUnique(object.getName())) {
            validationResult.add(Error.of("invalid.name", "Name is invalid"));
        }
        if (object.getAuthor().equals("")) {
            validationResult.add(Error.of("invalid.author", "Author is invalid"));
        }
        if (object.getPublisher().equals("")) {
            validationResult.add(Error.of("invalid.publisher", "Publisher is invalid"));
        }
        if (object.getPublishingYear().equals("") || !LocalDateFormatter.isValid(object.getPublishingYear())) {
            validationResult.add(Error.of("invalid.publishingYear", "Publishing Year is invalid"));
        }
        if (object.getImage().equals("")) {
            validationResult.add(Error.of("invalid.image", "Image is invalid"));
        }
        if (object.getPrice().equals("")) {
            validationResult.add(Error.of("invalid.price", "Price is invalid"));
        }
        if (object.getRemainingAmount().equals("")) {
            validationResult.add(Error.of("invalid.remainingAmount", "Remaining amount is invalid"));
        }
        if (object.getPageCount().equals("")) {
            validationResult.add(Error.of("invalid.pageCount", "Page Count is invalid"));
        }
        return validationResult;
    }

    public boolean isUnique(String name) {
        return productDao.findAll().stream()
                .noneMatch(product -> product.getName().equals(name));
    }
}
