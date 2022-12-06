package project.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Product() {
    }

    public Product(Long id, Category category, String name, String description, String author, String publisher, LocalDate publishingYear, String image, Integer price, Integer remainingAmount, Integer pageCount, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.image = image;
        this.price = price;
        this.remainingAmount = remainingAmount;
        this.pageCount = pageCount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(LocalDate publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishingYear=" + publishingYear +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", remainingAmount=" + remainingAmount +
                ", pageCount=" + pageCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
