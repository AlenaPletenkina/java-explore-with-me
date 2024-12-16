package ru.practicum.ewmservice.category.controller;

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
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получил запрос на получение списка категорий.");
        return categoryService.getCategories(from,size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Получил запрос на получение категории по id.");
        return categoryService.getCategoryById(catId);
    }
}
