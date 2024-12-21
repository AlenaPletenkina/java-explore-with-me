package ru.practicum.ewmservice.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto categoryDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
