package project.mapper;

import project.dao.CategoryDao;
import project.dto.CreateCategoryDto;
import project.entity.Category;

public class CreateCategoryMapper implements Mapper<CreateCategoryDto, Category> {

    private static final CreateCategoryMapper INSTANCE = new CreateCategoryMapper();

    private final CategoryDao categoryDao = CategoryDao.getInstance();

    @Override
    public Category mapFrom(CreateCategoryDto object) {
        var categoryEntity = categoryDao.findAll().stream()
                .filter(category -> category.getName().equals(object.getParentCategory()))
                .findFirst();
        if (categoryEntity.isPresent()) {
            return Category.builder()
                    .name(object.getName())
                    .parentCategory(categoryEntity.get())
                    .build();
        } else {
            return Category.builder()
                    .name(object.getName())
                    .parentCategory(null)
                    .build();
        }
    }

    public static CreateCategoryMapper getInstance() {
        return INSTANCE;
    }
}
