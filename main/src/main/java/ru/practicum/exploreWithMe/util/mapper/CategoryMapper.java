package ru.practicum.exploreWithMe.util.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.exploreWithMe.dto.category.CategoryDto;
import ru.practicum.exploreWithMe.model.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
