package ru.practicum.ewmservice.category.service;

import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);

}
