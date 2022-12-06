package project.dto;

import project.entity.Category;

import java.time.LocalDate;

public record ProductFilter(int limit, int offset, Category category, String name, String description, String author, String publisher, LocalDate publishingYear, Integer price) {
}
