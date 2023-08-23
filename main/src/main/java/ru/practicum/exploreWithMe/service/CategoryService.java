package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.category.CategoryDto;
import ru.practicum.exploreWithMe.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryDto categoryDto);

    void removeCategory(Integer id);

    Category updateCategory(Integer id, CategoryDto categoryDto);

    List<Category> getAllCategories(Integer from, Integer size);

    Category getCategory(Integer id);
}
