package ru.practicum.ewmservice.category.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;
import ru.practicum.ewmservice.category.service.CategoryService;

@RestController
@RequestMapping("admin/categories")
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final String path = "/{catId}";

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Поступил запрос на создание категории.");
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping(path)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Получил запрос на удаление категории.");
        categoryService.deleteCategory(catId);
    }

    @PatchMapping(path)
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                      @PathVariable Integer catId) {
        log.info("Получил запрос на обновление категории.");
        return categoryService.updateCategory(categoryDto, catId);
    }
}
