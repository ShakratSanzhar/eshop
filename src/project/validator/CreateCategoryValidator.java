package project.validator;

import project.dao.CategoryDao;
import project.dto.CreateCategoryDto;

public class CreateCategoryValidator implements Validator<CreateCategoryDto> {

    private static final CreateCategoryValidator INSTANCE = new CreateCategoryValidator();
    private final CategoryDao categoryDao = CategoryDao.getInstance();

    public static CreateCategoryValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(CreateCategoryDto object) {
        var validationResult = new ValidationResult();
        if (object.getName().equals("") || !isUnique(object.getName())) {
            validationResult.add(Error.of("invalid.name", "Name is invalid"));
        }
        if (object.getParentCategory().equals("")) {
            validationResult.add(Error.of("invalid.parentCategory", "ParentCategory is invalid"));
        }
        return validationResult;
    }

    public boolean isUnique(String name) {
        return categoryDao.findAll().stream()
                .noneMatch(category -> category.getName().equals(name));
    }
}
