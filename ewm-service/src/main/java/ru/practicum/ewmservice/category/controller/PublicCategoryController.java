package ru.practicum.ewmservice.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class PublicCategoryController {
    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получил запрос на получение списка категорий.");
        return categoryService.getCategories(from,size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Получил запрос на получение категории по id.");
        return categoryService.getCategoryById(catId);
    }
}
