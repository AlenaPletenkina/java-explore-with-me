package ru.practicum.ewmservice.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.exception.CategoryNotFoundException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(Category.builder()
                .name(newCategoryDto.getName())
                .build());
        return CategoryMapper.categoryDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Integer catId) {
        boolean exists = categoryRepository.existsById(catId);
        if (!exists) {
            throw new CategoryNotFoundException();
        }
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow();
        category.setName(categoryDto.getName());
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .skip(from)
                .limit(size)
                .map(CategoryMapper::categoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) {
        Category category = categoryRepository.findById(catId).orElseThrow();
        return CategoryMapper.categoryDto(category);
    }
}
