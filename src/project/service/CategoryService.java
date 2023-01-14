package project.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import project.dao.CategoryDao;
import project.dto.CreateCategoryDto;
import project.dto.ReadCategoryDto;
import project.entity.Category;
import project.exception.ValidationException;
import project.mapper.CreateCategoryMapper;
import project.validator.CreateCategoryValidator;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryService {

    private static final CategoryService INSTANCE = new CategoryService();
    private final CategoryDao categoryDao = CategoryDao.getInstance();
    private final CreateCategoryValidator createCategoryValidator = CreateCategoryValidator.getInstance();
    private final CreateCategoryMapper createCategoryMapper = CreateCategoryMapper.getInstance();

    public List<ReadCategoryDto> findAllSubCategories() {
        return categoryDao.findAll().stream()
                .filter(category -> category.getParentCategory() != null)
                .map(category -> ReadCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()).collect(Collectors.toList());
    }

    public List<ReadCategoryDto> findAllCategories() {
        return categoryDao.findAll().stream()
                .map(category -> ReadCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()).collect(Collectors.toList());
    }

    public Integer create(CreateCategoryDto categoryDto) {
        var validationResult = createCategoryValidator.isValid(categoryDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var categoryEntity = createCategoryMapper.mapFrom(categoryDto);
        categoryDao.save(categoryEntity);
        return categoryEntity.getId();
    }

    public Integer getCategoryIdByName(String name) {
        var categoryEntity = categoryDao.findAll().stream()
                .filter(category -> category.getName().equals(name))
                .findFirst();
        return categoryEntity.map(Category::getId).orElse(null);
    }

    public static CategoryService getInstance() {
        return INSTANCE;
    }
}
