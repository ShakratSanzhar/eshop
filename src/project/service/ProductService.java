package project.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import project.dao.ProductDao;
import project.dto.CreateProductDto;
import project.dto.ProductFilter;
import project.dto.ReadProductDto;
import project.exception.ValidationException;
import project.mapper.CreateProductMapper;
import project.validator.CreateProductValidator;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductService {

    private static final ProductService INSTANCE = new ProductService();
    private final ProductDao productDao = ProductDao.getInstance();
    private final CreateProductValidator createProductValidator = CreateProductValidator.getInstance();
    private final CreateProductMapper createProductMapper = CreateProductMapper.getInstance();
    private final ImageService imageService = ImageService.getInstance();

    public List<ReadProductDto> findAllProducts() {
        return productDao.findAll().stream()
                .map(product -> ReadProductDto.builder()
                        .id(product.getId())
                        .categoryName(product.getCategory().getName())
                        .name(product.getName())
                        .description(product.getDescription())
                        .author(product.getAuthor())
                        .publisher(product.getPublisher())
                        .publishingYear(product.getPublishingYear().getYear())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .remainingAmount(product.getRemainingAmount())
                        .pageCount(product.getPageCount())
                        .build()
                ).collect(Collectors.toList());
    }

    public ReadProductDto getProductById(Long id) {
        return productDao.findById(id).stream()
                .map(product -> ReadProductDto.builder()
                        .id(product.getId())
                        .categoryName(product.getCategory().getName())
                        .name(product.getName())
                        .description(product.getDescription())
                        .author(product.getAuthor())
                        .publisher(product.getPublisher())
                        .publishingYear(product.getPublishingYear().getYear())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .remainingAmount(product.getRemainingAmount())
                        .pageCount(product.getPageCount())
                        .build()
                ).findFirst().get();
    }

    @SneakyThrows
    public Long create(CreateProductDto productDto) {
        var validationResult = createProductValidator.isValid(productDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var productEntity = createProductMapper.mapFrom(productDto);
        imageService.upload(productEntity.getImage(), productDto.getImage().getInputStream());
        productDao.save(productEntity);
        return productEntity.getId();
    }

    public List<ReadProductDto> findProductsByFilter(ProductFilter productFilter) {
        return productDao.findAll(productFilter).stream()
                .map(product -> ReadProductDto.builder()
                        .id(product.getId())
                        .categoryName(product.getCategory().getName())
                        .name(product.getName())
                        .description(product.getDescription())
                        .author(product.getAuthor())
                        .publisher(product.getPublisher())
                        .publishingYear(product.getPublishingYear().getYear())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .remainingAmount(product.getRemainingAmount())
                        .pageCount(product.getPageCount())
                        .build()
                ).collect(Collectors.toList());
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }
}
